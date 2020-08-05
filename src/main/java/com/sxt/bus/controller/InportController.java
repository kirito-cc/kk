package com.sxt.bus.controller;


import java.util.Date;
import java.util.List;

import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Inport;
import com.sxt.bus.domain.Provider;
import com.sxt.bus.service.IGoodsService;
import com.sxt.bus.service.IInportService;
import com.sxt.bus.service.IProviderService;
import com.sxt.bus.vo.InportVo;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.common.WebUtils;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2020-08-02
 */
@RestController
@RequestMapping("/inport")
public class InportController {
	@Autowired
	IInportService inportService;
	@Autowired
	IProviderService providerService;
	@Autowired
	IGoodsService goodsService;
	
	/**
	 * 删除
	 */
	@RequestMapping("deleteInport")
	public ResultObj deleteInport(Integer id) {
		try {
			this.inportService.removeById(id);
			return new ResultObj().DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().DELETE_ERROR;
		}
	}
	
	
	/**
	 * 修改
	 */
	@RequestMapping("updateInport")
	public ResultObj updateInport(InportVo inportVo) {
		try {
			this.inportService.updateById(inportVo);
			return new ResultObj().UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().UPDATE_ERROR;
		}
	}
	

	/**
	 * 添加
	 */
	@RequestMapping("addInport")
	public ResultObj addInport(InportVo inportVo) {
		try {
			inportVo.setInporttime(new Date());
			User user = (User)WebUtils.getSession().getAttribute("user");
			inportVo.setOperateperson(user.getUsername());
			this.inportService.save(inportVo);
			return new ResultObj().ADD_SUCCESS;
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().ADD_ERROR;
		}
	}
	
	
	
	/**
	 * 查询
	 */
	@RequestMapping("loadAllInport")
	public DataGridView loadAllInport(InportVo inportVo) {
		IPage<Inport>page = new Page<Inport>(inportVo.getPage(),inportVo.getLimit());
		QueryWrapper<Inport>queryWrapper = new QueryWrapper<Inport>();
		queryWrapper.eq(inportVo.getProviderid()!=null&&inportVo.getProviderid()!=0,"providerid" , inportVo.getProviderid());
		queryWrapper.eq(inportVo.getGoodsid()!=null&&inportVo.getGoodsid()!=0, "goodsid", inportVo.getGoodsid());
		queryWrapper.ge(inportVo.getStartTime()!=null, "inporttime",inportVo.getStartTime());
		queryWrapper.le(inportVo.getEndTime()!=null, "inporttime", inportVo.getEndTime());
		queryWrapper.like(StringUtils.isNotBlank(inportVo.getOperateperson()), "operatepersion", inportVo.getOperateperson());
		queryWrapper.like(StringUtils.isNoneBlank(inportVo.getRemark()), "remark", inportVo.getRemark());
		queryWrapper.orderByDesc("inporttime");
		this.inportService.pageMaps(page, queryWrapper);
		List<Inport>list = page.getRecords();
		for (Inport inport : list) {
			Provider provider = this.providerService.getById(inport.getProviderid());
			if(null!=provider) {
				inport.setProvidename(provider.getProvidername());
			}
			Goods goods = this.goodsService.getById(inport.getGoodsid());
			if(null!=goods) {
				inport.setGoodsname(goods.getGoodsname());
				inport.setSize(goods.getSize());
			}
		}
			return new DataGridView(page.getTotal(),list);	
		
	}
	

}

