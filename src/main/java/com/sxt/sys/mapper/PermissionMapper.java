package com.sxt.sys.mapper;

import com.sxt.sys.domain.Permission;

import java.io.Serializable;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 老雷
 * @since 2020-07-12
 */
public interface PermissionMapper extends BaseMapper<Permission> {

	void deleteRolePermissionByPid(@Param("id")Serializable id);
}
