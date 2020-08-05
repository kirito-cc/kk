package com.sxt.bus.service.impl;

import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Inport;
import com.sxt.bus.domain.Outport;
import com.sxt.bus.mapper.GoodsMapper;
import com.sxt.bus.mapper.InportMapper;
import com.sxt.bus.mapper.OutportMapper;
import com.sxt.bus.service.IGoodsService;
import com.sxt.bus.service.IInportService;
import com.sxt.bus.service.IOutportService;
import com.sxt.sys.common.WebUtils;
import com.sxt.sys.domain.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 老雷
 * @since 2020-08-03
 */
@Service
@Transactional
public class OutportServiceImpl extends ServiceImpl<OutportMapper, Outport> implements IOutportService {

	
	
	@Autowired
	InportMapper inportMapper;
	@Autowired
	GoodsMapper goodsMapper;
	@Override
	public void addOutport(Integer id,Integer number,String remark) {
		//根据进货ID查询进货信息
		Inport inport = this.inportMapper.selectById(id);
		//根据商品Id查询商品信息
		Goods goods = this.goodsMapper.selectById(inport.getGoodsid());
		goods.setNumber(goods.getNumber()-number);
		this.goodsMapper.updateById(goods);
		//添加退货单信息
		Outport entity = new Outport();
		entity.setGoodsid(inport.getGoodsid());
		entity.setNumber(number);
		User user = (User)WebUtils.getSession().getAttribute("user");
		entity.setOperateperson(user.getName());
		entity.setOutportprice(inport.getInportprice());
		entity.setOutputtime(new Date());
		entity.setPaytype(inport.getPaytype());
		entity.setProviderid(inport.getProviderid());
		entity.setRemark(remark);
		this.getBaseMapper().insert(entity);
		
	}

	
}
