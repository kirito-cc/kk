package com.sxt.bus.controller;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Outport;
import com.sxt.bus.domain.Provider;
import com.sxt.bus.service.IGoodsService;
import com.sxt.bus.service.IOutportService;
import com.sxt.bus.service.IProviderService;
import com.sxt.bus.vo.OutportVo;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2020-08-03
 */
@RestController
@RequestMapping("/outport")
public class OutportController {

	private static final String Provider = null;
	@Autowired
	IOutportService outportService;
	
	@Autowired
	IProviderService providerService;
	
	@Autowired
	IGoodsService goodsService;
	
	/**
	 * 查询退货信息
	 */
	
	@RequestMapping("loadAllOutport")
	public DataGridView loadAllOutport(OutportVo outportVo) {
		IPage<Outport>page = new Page<Outport>(outportVo.getPage(),outportVo.getLimit());
		QueryWrapper<Outport>queryWrapper = new QueryWrapper<Outport>();
		queryWrapper.eq(outportVo.getProviderid()!=null&&outportVo.getProviderid()!=0, "provider", outportVo.getProviderid());
		queryWrapper.eq(outportVo.getGoodsid()!=null&&outportVo.getGoodsid()!=0, "goodsid", outportVo.getGoodsid());
		queryWrapper.ge(outportVo.getStartTime()!=null, "outporttime", outportVo.getStartTime());
		queryWrapper.le(outportVo.getEndTime()!=null, "outporttime",outportVo.getEndTime());
		queryWrapper.like(StringUtils.isNotBlank(outportVo.getOperateperson()), "operateperson", outportVo.getOperateperson());
		queryWrapper.like(StringUtils.isNotBlank(outportVo.getRemark()), "remark",outportVo.getRemark());
		queryWrapper.orderByDesc("outporttime"); 
		this.outportService.page(page, queryWrapper);
		List<Outport>list = page.getRecords();
		for (Outport outport : list) {
			Provider provider =this.providerService.getById(outport.getProviderid());
			if(null!=provider) {
				outport.setProvidename(provider.getProvidername());
			}
			Goods goods = this.goodsService.getById(outport.getGoodsid());
			if(null!=goods) {
				outport.setGoodsname(goods.getGoodsname());
				outport.setSize(goods.getSize());
			}
		}
		return new DataGridView(page.getTotal(),list);
	}
	
	
	
	
	
	
	
	
	/**
	 * 添加退货信息
	 */
	@RequestMapping("addOutport")
	public ResultObj addOutport(Integer id,Integer number,String remark) {
		try {
			this.outportService.addOutport(id,number,remark);
			return new ResultObj().ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().ADD_ERROR;
		}
	}
}

