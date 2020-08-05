package com.sxt.bus.controller;


import java.util.Date;
import java.util.List;

import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Provider;
import com.sxt.bus.service.IGoodsService;
import com.sxt.bus.service.IProviderService;
import com.sxt.bus.util.AppFileUtils;
import com.sxt.bus.vo.GoodsVo;
import com.sxt.bus.vo.InportVo;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.common.WebUtils;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2020-07-30
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	IGoodsService goodsService;
	
	@Autowired
	IProviderService providerService;
	
	
	
	
	
	
	/**
	 * 根据供应商Id查询商品信息
	 */
	@RequestMapping("loadGoodsByProviderId")
	public DataGridView loadGoodsByProviderId(Integer providerid) {
		QueryWrapper<Goods>queryWrapper = new QueryWrapper<Goods>();
		queryWrapper.eq("available",Constast.AVAILABLE_TRUE);
		queryWrapper.eq(providerid!=null, "providerid", providerid);
		List<Goods>list = this.goodsService.list(queryWrapper);
		for (Goods goods : list) {
			Provider provider = this.providerService.getById(providerid);
			if(null!=provider) {
				goods.setProvidename(provider.getProvidername());
			}
		}
		return new DataGridView(list);
		
	}
	/**
	 * 加载所有可用的供应商
	 * 
	 */
	@RequestMapping("loadAllGoodsForSelect")
	public DataGridView loadAllGoodsForSelect() {
		QueryWrapper<Goods>queryWrapper = new QueryWrapper<Goods>();
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		List<Goods>list = this.goodsService.list(queryWrapper);
		for (Goods goods : list) {
			Provider provider = this.providerService.getById(goods.getProviderid());
			if(null!=provider) {
				goods.setProvidename(provider.getProvidername());
			}
		}
		return new DataGridView(list);
	}
	
	
	
	/**
	 * 查询
	 * @param goodsVo
	 * @return
	 */
	@RequestMapping("loadAllGoods")
	public DataGridView loadAllGoods(GoodsVo goodsVo) {
		IPage<Goods> page = new Page<>(goodsVo.getPage(), goodsVo.getLimit());
		QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(goodsVo.getProviderid()!=null&&goodsVo.getProviderid()!=0,"providerid",goodsVo.getProviderid());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getGoodsname()), "goodsname", goodsVo.getGoodsname());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getProductcode()), "productcode", goodsVo.getProductcode());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getPromitcode()), "promitcode", goodsVo.getPromitcode());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getDescription()), "description", goodsVo.getDescription());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getSize()), "size", goodsVo.getSize());
		this.goodsService.page(page, queryWrapper);
		List<Goods> records = page.getRecords();
		for (Goods goods : records) {
			Provider provider = this.providerService.getById(goods.getProviderid());
			if(null!=provider) {
				goods.setProvidename(provider.getProvidername());
			}
		}
		return new DataGridView(page.getTotal(), records);
	}
	//添加
	@RequestMapping("addGoods")
	public ResultObj addGoods(GoodsVo goodsVo) {
	
		try {
			if(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().endsWith("_temp")) {
				String newName = AppFileUtils.renameFile(goodsVo.getGoodsimg());
				goodsVo.setGoodsimg(newName);
			}
			return new ResultObj().ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().ADD_ERROR;
		}
	}
	/**
	 * 修改
	 */
	@RequestMapping("updateGoods")
	public ResultObj updateGoods(GoodsVo goodVo) {
		try {
			//说明是不默认图片
			if(!(goodVo.getGoodsimg()!=null&&goodVo.getGoodsimg().equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG))) {
				if(goodVo.getGoodsimg().endsWith("_temp")) {
					String newName = AppFileUtils.renameFile(goodVo.getGoodsimg());
					goodVo.setGoodsimg(newName);
					//删除原先的图片
					String oldPath = this.goodsService.getById(goodVo.getId()).getGoodsimg();
					if(!oldPath.equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG)) {
						AppFileUtils.removeFileByPath(oldPath);
					}
				
				}
			}
			this.goodsService.save(goodVo);
			return new ResultObj().UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().UPDATE_ERROR;
		}
	}
	/**
	 * 删除
	 */
	@RequestMapping("deleteGoods")
	public ResultObj deleteGoods(Integer id, String goodsimg) {
		try {
			//删除原文件
			AppFileUtils.removeFileByPath(goodsimg);
			this.goodsService.removeById(id);
			return new ResultObj().DELETE_SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().DELETE_ERROR;
		}
	}

}

