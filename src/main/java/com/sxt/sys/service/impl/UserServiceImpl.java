package com.sxt.sys.service.impl;

import com.sxt.sys.domain.User;
import com.sxt.sys.mapper.RoleMapper;
import com.sxt.sys.mapper.UserMapper;
import com.sxt.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 老雷
 * @since 2020-07-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	@Autowired
	RoleMapper roleMapper;
	
	@Override
	public boolean save(User entity) {
		return super.save(entity);
	}
	
	
	@Override
	public boolean updateById(User entity) {
		return super.updateById(entity);
	}
	@Override
	public User getById(Serializable id) {
		return super.getById(id);
	}
	@Override
	public boolean removeById(Serializable id) {
		/**
		 * 根据用户ID删除用户中间表的数据
		 */
		roleMapper.deleteRoleUserById(id);
		//删除用户头像【如果是默认头像不删除，否则删除】
		return super.removeById(id);
	}


	@Override
	public void saveUserRole(Integer uid, Integer[] ids) {
		//根据用户Id删除sys_role_user里面的数据
		this.roleMapper.deleteRoleUserById(uid);
		if(null!=ids&&ids.length>0) {
			for(Integer rid : ids) {
				this.roleMapper.insertUserRole(uid,rid);
			}
		}
		
	}
	
	
	
	
	
	
	
	
}
