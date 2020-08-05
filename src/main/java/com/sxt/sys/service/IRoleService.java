package com.sxt.sys.service;

import com.sxt.sys.domain.Role;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 老雷
 * @since 2020-07-20
 */
public interface IRoleService extends IService<Role> {

	/**
	 * 根据角色ID查询当前角色拥有的所有的权限或菜单ID
	 * @param roleId
	 * @return
	 */
	List<Integer> queryRolePermissionIdsByRid(Integer roleId);

	/**
	 * 保存角色和菜单权限之间的关系
	 * @param roleId
	 * @param ids
	 */
	void saveRolePermission(Integer roleId, Integer[] ids);
	
	/**
	 * 查询当前用户拥有的角色ID集合
	 * @param id
	 * @return
	 */
	List<Integer> queryUserRoleIdsByUid(Integer id);

}
