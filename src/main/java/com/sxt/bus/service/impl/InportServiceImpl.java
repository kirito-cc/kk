package com.sxt.bus.service.impl;

import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Inport;
import com.sxt.bus.mapper.GoodsMapper;
import com.sxt.bus.mapper.InportMapper;
import com.sxt.bus.service.IInportService;
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
 * @since 2020-08-02
 */
@Service
public class InportServiceImpl extends ServiceImpl<InportMapper, Inport> implements IInportService {
   
	@Autowired 
	private GoodsMapper goodsMapper;
	
	@Override
	public boolean removeById(Serializable id) {
		//根据进货Id查询进货信息
		Inport inport = this.baseMapper.selectById(id);
		//根据商品ID查询商品信息
		Goods goods = this.goodsMapper.selectById(inport.getGoodsid());
		//库存的算法   当前库存-进货单数量
		goodsMapper.updateById(goods);
		
		return super.removeById(id);
	}
	
	
	
	@Override
	public boolean save(Inport entity) {
    	//根据商品id查询商品
    	Goods goods = goodsMapper.selectById(entity.getGoodsid());
    	goods.setNumber(goods.getNumber()+entity.getNumber());
    	goodsMapper.updateById(goods);
    	
    	//保存进货信息
    	return super.save(entity);
    }
	
	@Override
	public boolean updateById(Inport entity) {
		//根据进货id查询进货
		Inport inport = this.baseMapper.selectById(entity.getId());
		//根据商品Id查询商品信息
		Goods goods = this.goodsMapper.selectById(entity.getGoodsid());
		//库存的算法     当前库存-修改之前的库存数量+修改之后的数量；
		goods.setNumber(goods.getNumber()-inport.getNumber()+entity.getNumber());
		this.goodsMapper.updateById(goods);
		//更新进货单
		return super.updateById(entity);
	}
	
}
