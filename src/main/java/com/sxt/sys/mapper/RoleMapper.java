package com.sxt.sys.mapper;

import com.sxt.sys.domain.Role;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 老雷
 * @since 2020-07-20
 */
public interface RoleMapper extends BaseMapper<Role> {
/**
 * 根据角色Id删除sys_role_permission
 */
	public void deleteRolePermissionByRid(Serializable id);
	/**
	 * 根据角色Id删除sys_role_user
	 */
	public void deleteRoleUSerByRid(Serializable id);
	
	/**
	 * 根据角色ID查询当前角色拥有的所有的权限或菜单ID
	 * @param roleId
	 * @return
	 */
	List<Integer> queryRolePermissionIdsByRid(Integer roleId);

	/**
	 * 保存角色和菜单权限之间的关系
	 * @param rid
	 * @param pid
	 */
	void saveRolePermission(@Param("rid")Integer rid, @Param("pid")Integer pid);
	/**
	 * 根据用户ID删除用户中间表的数据
	 */
	void deleteRoleUserById(Serializable id);
	
	/**
	 * 查询当前用户拥有的角色ID集合
	 * @param id
	 * @return
	 */
	List<Integer> queryUserRoleIdsByUid(Integer id);
	/**
	 * 保存用户和角色的关系
	 */
	void insertUserRole(@Param("uid")Integer uid,@Param("rid")Integer rid);
}
