package com.sxt.sys.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.common.TreeNode;
import com.sxt.sys.domain.Permission;
import com.sxt.sys.domain.Role;
import com.sxt.sys.service.IPermissionService;
import com.sxt.sys.service.IRoleService;
import com.sxt.sys.vo.RoleVo;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2020-07-20
 */
@RestController
@RequestMapping("/role")
public class RoleController {
	@Autowired 
	IRoleService roleService;
	
	@Autowired
	IPermissionService permissionService;
	
	@RequestMapping("loadAllRole")
	public DataGridView loadAllRole(RoleVo roleVo) {
		IPage<Role>page = new Page<Role>(roleVo.getPage(),roleVo.getLimit());
		QueryWrapper<Role>queryWrapper = new QueryWrapper<Role>();
		queryWrapper.like(StringUtils.isNotBlank(roleVo.getName()), "name",roleVo.getName());
		queryWrapper.like(StringUtils.isNotBlank(roleVo.getRemark()), "remark",roleVo.getRemark());
		queryWrapper.eq(roleVo.getAvailable()!=null, "available", roleVo.getAvailable());
		queryWrapper.orderByDesc("createtime");
		this.roleService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(),page.getRecords());
		
	}
	/**
	 * 添加
	 */
	@RequestMapping("addRole")
	public ResultObj addRole(RoleVo roleVo) {
		try {
			roleVo.setCreatetime(new Date());
			this.roleService.save(roleVo);
			return new ResultObj().ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().ADD_ERROR;
		}
	}
	/**
	 * 更新
	 */
	@RequestMapping("updateRole")
	public ResultObj updateRole(RoleVo roleVo) {
		try{
			this.roleService.updateById(roleVo);
			return new ResultObj().UPDATE_SUCCESS;
		
	}catch (Exception e) {
		e.printStackTrace();
		return new ResultObj().UPDATE_ERROR;
	}
	}
		/**
		 * 删除
		 */
	@RequestMapping("deleteRole")
	public ResultObj deleteRole(Integer id) {
		try {
			this.roleService.removeById(id);
			return new ResultObj().DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().DELETE_ERROR;
		}
	}
	
	
	/**
	 * 根据角色ID加载菜单和权限的树的json串
	 */
	@RequestMapping("initPermissionByRoleId")
	public DataGridView initPermissionByRoleId(Integer roleId) {
		/**
		 * 查询所有可用的权限和菜单
		 */
		QueryWrapper<Permission>queryWrapper = new QueryWrapper<Permission>();
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		List<Permission>allPermissions = permissionService.list(queryWrapper);
		
		/**
		 * 1,根据角色Id查询当前角色所用的菜单Id和权限
		 * 2，根据查询出来的菜单Id查询权限和菜单数据
		 */
		List<Integer>currentRolePermissions = this.roleService.queryRolePermissionIdsByRid(roleId);
	    List<Permission>carrentPermissions = null;
	    if(currentRolePermissions.size()>0) {
	    	queryWrapper.in("id", currentRolePermissions);
	    	carrentPermissions = permissionService.list(queryWrapper);
	    }else {
			carrentPermissions = new ArrayList<Permission>();
		}
	    //构造List《TreeNode>
	    List<TreeNode>nodes = new ArrayList<TreeNode>();
	    for (Permission p1 : allPermissions) {
			String checkArr = "0";
			for (Permission p2 : carrentPermissions) {
				if(p1.getId()==p2.getId()) {
					checkArr = "1";
					break;
				}
			}
			Boolean spread = (p1.getOpen()==null||p1.getOpen()==1)?true:false;
			nodes.add(new TreeNode(p1.getId(), p1.getPid(), p1.getTitle(), spread,  checkArr));
			
		}
	return new DataGridView(nodes);
	
	
	}
	
	
	/**
	 * 保存角色和菜单权限的关系
	 */
	@RequestMapping("saveRolePermission")
	public ResultObj savaRolePermission(Integer rid, Integer[]ids) {
		try {
			this.roleService.saveRolePermission(rid, ids);
			return new  ResultObj().DISPATCH_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().DISPATCH_ERROR;
		}
	}
	
	
	

}

