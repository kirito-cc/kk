package com.sxt.sys.realm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxt.sys.common.ActiveUser;
import com.sxt.sys.common.Constast;
import com.sxt.sys.domain.Permission;
import com.sxt.sys.domain.User;
import com.sxt.sys.service.IPermissionService;
import com.sxt.sys.service.IRoleService;
import com.sxt.sys.service.IUserService;


public class UserRealm extends AuthorizingRealm {

	@Autowired
	@Lazy//只有在使用的时候才会加载
	private IUserService userService;
	
	@Autowired
	@Lazy
	private IRoleService roleService;
	@Autowired
	@Lazy
	private IPermissionService permissionService;

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("loginname", token.getPrincipal().toString());
		User user = userService.getOne(queryWrapper);
		if (null != user) {
			ActiveUser activerUser = new ActiveUser();
			activerUser.setUser(user);
			
			//根据用户Id查询percode
			//查询所有菜单
			QueryWrapper<Permission>qw = new QueryWrapper<Permission>();
			qw.eq("type", Constast.TYPE_PERMISSION);
			qw.eq("available", Constast.AVAILABLE_TRUE);
			//根据用户id+角色+权限去查询
			Integer id = user.getId();
			//根据用户Id查询角色
			List<Integer>currentUserRoleIds = this.roleService.queryUserRoleIdsByUid(id);
			//根据角色ID取到权限和菜单Id
		    Set<Integer>pids = new HashSet<Integer>();
		    for (Integer rid : currentUserRoleIds) {
				List<Integer>permissionIds = this.roleService.queryRolePermissionIdsByRid(rid);
				pids.addAll(permissionIds);
				
			}
			
			List<Permission>list = new ArrayList<Permission>();
			//根据角色Id查询权限
			if(pids.size()>0) {
				qw.in("id", pids);
				list = this.permissionService.list(qw);
			}
			List<String>percodes = new ArrayList<String>();
			for (Permission permission : list) {
				percodes.add(permission.getPercode());
			}
			
			activerUser.setPermissions(percodes);
			
			
			ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser, user.getPwd(), credentialsSalt,
					this.getName());
			return info;
		}
		return null;
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		ActiveUser activeUser = (ActiveUser)principals.getPrimaryPrincipal();
		User user  = activeUser.getUser();
		List<String>permissions = activeUser.getPermissions();
		if(user.getType()==Constast.USER_TYPE_SUPER) {
			authorizationInfo.addStringPermission("*:*");
		}else {
			if(null!=permissions&&permissions.size()>0) {
				authorizationInfo.addStringPermissions(permissions);
			}
		}
		return authorizationInfo;
	}

}
