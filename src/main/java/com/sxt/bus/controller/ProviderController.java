package com.sxt.bus.controller;


import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.Provider;
import com.sxt.bus.service.IProviderService;
import com.sxt.bus.vo.ProviderVo;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2020-07-30
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {

	
	@Autowired
	IProviderService providerSerVice;
	/**
	 * 查询
	 * @param providerVo
	 * @return
	 */
	@RequestMapping("loadAllProvider")
	public DataGridView loadAllProvider(ProviderVo providerVo) {
		IPage<Provider>page = new Page<Provider>(providerVo.getPage(),providerVo.getLimit());
		QueryWrapper<Provider>queryWrapper = new QueryWrapper<Provider>();
		queryWrapper.like(StringUtils.isNotBlank(providerVo.getProvidername()), "providername", providerVo.getProvidername());
		queryWrapper.like(StringUtils.isNotBlank(providerVo.getPhone()), "phone",providerVo.getPhone());
		queryWrapper.like(StringUtils.isNotBlank(providerVo.getConnectionperson()), "connectionperson", providerVo.getConnectionperson());
		this.providerSerVice.page(page, queryWrapper);
		return new DataGridView(page.getTotal(),page.getRecords());
	}
	/**
	 * 添加
	 */
	@RequestMapping("addProvider")
	public ResultObj addProvider(ProviderVo providerVo) {
		try {
			this.providerSerVice.save(providerVo);
			return new ResultObj().ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().ADD_ERROR;
		}
	}
	/**
	 * 修改
	 */
	@RequestMapping("updateProvider")
	public ResultObj updateProvider(ProviderVo providerVO) {
		try {
			this.providerSerVice.updateById(providerVO);
			return new ResultObj().UPDATE_SUCCESS;
		} catch (Exception e) {
              e.printStackTrace();
              return new ResultObj().UPDATE_ERROR;
                    
		}
	}
	/**
	 * 删除
	 */
	@RequestMapping("deleteProvider")
	public ResultObj deleteProvider(Integer id) {
		try {
			this.providerSerVice.removeById(id);
			return new ResultObj().DELETE_ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().DELETE_ERROR;
		}
	}
	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteProvider")
	public ResultObj batchDeleteProvider(ProviderVo providerVo) {
		Collection<Integer>idList = new ArrayList<Integer>();
		for (Integer id : providerVo.getIds()) {
			idList.add(id);
		}
		try {
			this.providerSerVice.removeByIds(idList);
			return new ResultObj().DELETE_SUCCESS;

		} catch (Exception e) {
			return new ResultObj().DELETE_ERROR;
		}
	}
}

