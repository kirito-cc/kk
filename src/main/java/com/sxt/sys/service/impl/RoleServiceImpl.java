package com.sxt.sys.service.impl;

import com.sxt.sys.domain.Role;
import com.sxt.sys.mapper.RoleMapper;
import com.sxt.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 老雷
 * @since 2020-07-20
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
	@Override
	public boolean removeById(Serializable id) {
		/**
		 * 根据角色Id删除sys_role_permission
		 */
		this.getBaseMapper().deleteRolePermissionByRid(id);
		/**
		 * 根据角色Id删除sys_role_user
		 */
		this.getBaseMapper().deleteRoleUSerByRid(id);
		return super.removeById(id);
	}


	/**
	 * 根据角色ID查询当前角色拥有的所有的权限或菜单ID
	 */
	@Override
	public List<Integer> queryRolePermissionIdsByRid(Integer roleId) {
		
		return this.getBaseMapper().queryRolePermissionIdsByRid(roleId);
	}

	@Override
	public void saveRolePermission(Integer roleId, Integer[] ids) {
		RoleMapper roleMapper = this.getBaseMapper();
		/*
		 * 根据rid删除sys_role_permission
		 */
		roleMapper.deleteRolePermissionByRid(roleId);
		if(ids!=null&&ids.length>0) {
			for (Integer pid : ids) {
				roleMapper.saveRolePermission(roleId,pid);
			}
		}
	}
	/**
	 * 查询当前用户拥有的角色ID集合
	 * @param id
	 * @return
	 */
	@Override
	public List<Integer> queryUserRoleIdsByUid(Integer id){
		return this.getBaseMapper().queryUserRoleIdsByUid(id);
	}

}
