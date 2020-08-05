package com.sxt.bus.service.impl;

import com.sxt.bus.domain.Customer;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.mapper.GoodsMapper;
import com.sxt.bus.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 老雷
 * @since 2020-07-30
 */
@Service
@Transactional
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

	@Override
	public boolean save(Goods entity) {
		return super.save(entity);
	}
	
	@Override
	public boolean updateById(Goods entity) {
		return super.updateById(entity);
	}
	
	@Override
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}
	
	@Override
	public Goods getById(Serializable id) {
		return super.getById(id);
	}
	
	
	
	
	
	
	
	
}
