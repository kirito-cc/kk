package com.sxt.sys.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sxt.bus.cache.CacheBean;
import com.sxt.bus.cache.CachePool;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;

/**
 * 缓存管理控制器
 * @author 24168
 *
 */
@RestController
@RequestMapping("cache")
public class CacheCtroller {
	
	private static volatile Map<String,Object> CACHE_CONTAINER = CachePool.CACHE_CONTAINER;
	

	/**
	 * 同步缓存
	 */
	@RequestMapping("syncCache")
	public ResultObj syncCache() {
		CachePool.syncData();
		return new ResultObj().DISPATCH_SUCCESS;
	}
	
	
	
	
	/**
	 * 清空缓存
	 * 
	 */
	
	@RequestMapping("removeAllCache")
	public ResultObj removeAllCache() {
		CachePool.removeAll();
		return new ResultObj().DELETE_SUCCESS;
	}
	/**
	 * 删除缓存
	 */
	@RequestMapping("deleteCache")
	public ResultObj deleteCache(String key) {
		CachePool.removeCacheByKey(key);
		return new ResultObj().DELETE_SUCCESS;
	}
	

	/**
	 * 查询所有缓存
	 */
	@RequestMapping("loadAllCache")
	public DataGridView loadAllCache() {
		List<CacheBean>list = new ArrayList<CacheBean>();
		Set<Entry<String,Object>>entry = CACHE_CONTAINER.entrySet();
		for (Entry<String, Object> entry2 : entry) {
			list.add(new CacheBean(entry2.getKey(),entry2.getValue()));
		}
		return new DataGridView(list);
	}
}
