package com.sxt.bus.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sxt.bus.domain.Customer;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Provider;
import com.sxt.bus.mapper.CustomerMapper;
import com.sxt.bus.mapper.GoodsMapper;
import com.sxt.bus.mapper.ProviderMapper;
import com.sxt.bus.util.SpringUtils;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.User;
import com.sxt.sys.mapper.DeptMapper;
import com.sxt.sys.mapper.UserMapper;

public class CachePool {
	/**
	 * 所有的缓存数据放到这个CACHE_CONTAINER类似于redis
	 */
	public static volatile Map<String, Object> CACHE_CONTAINER = new HashMap<>();
	/**
	 * 根据KEY删除缓存
	 * @param key
	 */
	public static void removeCacheByKey(String key) {
		if(CACHE_CONTAINER.containsKey(key)) {
			CACHE_CONTAINER.remove(key);
		}
	}
	/**
	 * 清空所用缓存
	 */
	public static void removeAll() {
		CACHE_CONTAINER.clear();
	}
	/**
	 * 同步缓存
	 */
	public static  void syncData() {
		//同步部门数据
		DeptMapper deptMapper = SpringUtils.getBean(DeptMapper.class);
		List<Dept>deptList = deptMapper.selectList(null);
		for (Dept dept : deptList) {
			CACHE_CONTAINER.put("dept:"+dept.getId(), dept);
		}
		//同步用户数据
		UserMapper userMapper = SpringUtils.getBean(UserMapper.class);
		List<User>userList = userMapper.selectList(null);
		for (User user : userList) {
			CACHE_CONTAINER.put("user:"+user.getId(), user);
		}
		//同步客户数据
		CustomerMapper customerMapper = SpringUtils.getBean(CustomerMapper.class);
		List<Customer>customerList = customerMapper.selectList(null);
		for (Customer customer : customerList) {
			CACHE_CONTAINER.put("customer:"+customer.getId(), customer.getId());
		}
		//同步供应商数据
		ProviderMapper providerMapper = SpringUtils.getBean(ProviderMapper.class);
		List<Provider>providerList = providerMapper.selectList(null);
		for (Provider provider : providerList) {
			CACHE_CONTAINER.put("provider:"+provider.getId(), provider.getId());
		}
		//同步商品数据
		GoodsMapper goodsMapper = SpringUtils.getBean(GoodsMapper.class);
		List<Goods>goodsList = goodsMapper.selectList(null);
		for (Goods goods : goodsList) {
			CACHE_CONTAINER.put("goods:"+goods.getId(), goods.getId());
		}
	}
}