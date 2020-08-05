package com.sxt.sys.service;

import com.sxt.sys.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 老雷
 * @since 2020-07-09
 */
public interface IUserService extends IService<User> {
	/**
	 * 保存用户和角色之间的关系
	 */
	public void saveUserRole( Integer uid,Integer[]ids);

}
