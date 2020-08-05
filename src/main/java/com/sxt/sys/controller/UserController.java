package com.sxt.sys.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.PinyinUtil;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.Role;
import com.sxt.sys.domain.User;
import com.sxt.sys.service.IDeptService;
import com.sxt.sys.service.IRoleService;
import com.sxt.sys.service.IUserService;
import com.sxt.sys.vo.UserVo;

import cn.hutool.core.util.IdUtil;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2020-07-09
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	IUserService userService;
	@Autowired
	IDeptService deptService;
	@Autowired
	IRoleService roleService;
	/**
	 * 用户全查询
	 */
	
	@RequestMapping("loadAllUser")
	public DataGridView loadAllUser(UserVo userVo) {
		IPage<User>page = new Page<User>(userVo.getPage(),userVo.getLimit());
		QueryWrapper<User>queryWrapper = new QueryWrapper<User>();
		queryWrapper.eq(StringUtils.isNotBlank(userVo.getName()), "loginname", userVo.getName()).or().eq(StringUtils.isNotBlank(userVo.getName()),"name", userVo.getName());
	    queryWrapper.eq(StringUtils.isNotBlank(userVo.getAddress()),"address", userVo.getAddress());
	    queryWrapper.eq("type", Constast.USER_TYPE_NORMAL);
	    queryWrapper.eq(userVo.getDeptid()!=null,"deptid", userVo.getDeptid());
	    this.userService.page(page, queryWrapper);
	    List<User>list = page.getRecords();
	    for (User user : list) {
			Integer id = user.getDeptid();
			if(id!=null) {
				Dept dept = deptService.getById(id);
				user.setDeptname(dept.getTitle());
			}
			Integer mgr = user.getMgr();
			if(mgr!=null) {
				User one = userService.getById(mgr);
				user.setLeadername(one.getName());
			}
		}
	    return new DataGridView(page.getTotal(),list);
	}
	
	/**
	 * 加载最大的排序码
	 * @param deptVo
	 * @return
	 * @RequestMapping("loadUserMaxOrderNum")

	 */
	
	public Map<String,Object>loadUserMaxOrderNum(){
Map<String, Object> map=new HashMap<String, Object>();
		
		QueryWrapper<User> queryWrapper=new QueryWrapper<>();
		queryWrapper.orderByDesc("ordernum");
		IPage<User> page=new Page<>(1, 1);
		List<User> list = this.userService.page(page, queryWrapper).getRecords();
		if(list.size()>0) {
			map.put("value", list.get(0).getOrdernum()+1);
		}else {
			map.put("value", 1);
		}
		return map;
	}
	/**
	 * 根据部门ID查询用户
	 */
	@RequestMapping("loadUserByDeptId")
	public DataGridView loadUserByDeptId(Integer deptId) {
		QueryWrapper<User>queryWrapper = new QueryWrapper<User>();
		queryWrapper.eq(deptId!=null, "deptid",deptId);
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		queryWrapper.eq("type", Constast.USER_TYPE_NORMAL);
		List<User>list = this.userService.list(queryWrapper);
		return new DataGridView(list);
	}
	/**
	 * 把用户名转成拼音
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("changeChineseToPinyin")
	public Map<String,Object> changeChineseToPinyin(String username) {
		Map<String,Object>map = new HashMap<String, Object>();
		if (null!=username) {
			map.put("valule", PinyinUtil.getPingYin(username));
		} else {
			map.put("value", "");

		}
		return map;
	}

	/**
	 * 添加用户
	 */
	@RequestMapping("addUser")
	public ResultObj addUser(UserVo userVo) {
		try {
			userVo.setType(Constast.USER_TYPE_NORMAL);
			userVo.setHiredate(new Date());
			String salt = IdUtil.simpleUUID().toUpperCase();
			userVo.setSalt(salt);
			userVo.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD, salt, 2).toString());
			this.userService.save(userVo);
			return new ResultObj().ADD_SUCCESS;
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().ADD_ERROR;
		}
	}
	/**
	 * 根据用户ID查询一个用户
	 */
	@RequestMapping("loadUserById")
	public DataGridView loadUserById(Integer id) {
		return new DataGridView(this.userService.getById(id));
	}
	/**
	 * 修改用户
	 */
	@RequestMapping("updateUser")
	public ResultObj updateUser(UserVo userVo) {
		try {
			this.userService.updateById(userVo);
			return new ResultObj().UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().UPDATE_ERROR;
		}
	}
	/**
	 * 删除用户
	 */
	@RequestMapping("deleteUser")
	public ResultObj deleteUser(Integer id) {
		try {
			this.userService.removeById(id);
			return new ResultObj().DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().DELETE_ERROR;
		}
	}
	/**
	 * 重置密码
	 */
	@RequestMapping("resetPwd")
	public ResultObj resetPwd(Integer id) {
		try {
			User user = new User();
		    user.setId(id);
			String salt = IdUtil.simpleUUID().toUpperCase();
			user.setSalt(salt);
			user.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD, salt, 2).toString());
			this.userService.updateById(user);
			return new ResultObj().RESET_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().RESET_ERROR;
		}
	}
	/**
	 * 根据用户ID查询角色并选中已拥有的角色
	 */
	@RequestMapping("initRoleByUserId")
	public DataGridView initRoleByUserId(Integer id) {
		//查询所有可用的角色
		QueryWrapper<Role>queryWrapper = new QueryWrapper<Role>();
		queryWrapper.eq("available",Constast.AVAILABLE_TRUE);
		List<Map<String,Object>>listMaps = this.roleService.listMaps(queryWrapper);
		//2,查询当前用户拥有的角色ID集合
		List<Integer>currentUserRoleIds = this.roleService.queryUserRoleIdsByUid(id);
         for (Map<String,Object> map : listMaps) {
        	 Boolean LAY_CHECKED=false;
        	 Integer roleId = (Integer)map.get("id");
        	 for (Integer rid: currentUserRoleIds) {
        		 if(rid==roleId) {
        			 LAY_CHECKED=true;
        			 break;
        		 }
				
			}
        		map.put("LAY_CHECKED", LAY_CHECKED);
}
	return new DataGridView(Long.valueOf(listMaps.size()),listMaps);
	}
	/**
	 * 保存用户和角色之间的关系
	 */
	@RequestMapping("saveUserRole")
	public ResultObj saveUserRole(Integer uid,Integer[]ids) {
		try {
			this.userService.saveUserRole(uid,ids);
			return new ResultObj().DISPATCH_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().DISPATCH_ERROR;
		}
	}
}

