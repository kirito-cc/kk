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
import com.sxt.bus.domain.Customer;
import com.sxt.bus.service.ICustomerService;
import com.sxt.bus.vo.CustomerVo;
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
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	ICustomerService customerService;
	/**
	 * 查询
	 * @param customerVo
	 * @return
	 */
	
	@RequestMapping("loadAllCustomer")
	public DataGridView loadAllCustomer(CustomerVo customerVo) {
		IPage<Customer>page = new Page<Customer>(customerVo.getPage(),customerVo.getLimit());
        QueryWrapper<Customer>queryWrapper = new QueryWrapper<Customer>();
        queryWrapper.like(StringUtils.isNotBlank(customerVo.getCustomername()), "customername", customerVo.getCustomername());
        queryWrapper.like(StringUtils.isNotBlank(customerVo.getPhone()), "phone", customerVo.getPhone());
        queryWrapper.like(StringUtils.isNotBlank(customerVo.getConnectionperson()), "connectionperson", customerVo.getConnectionperson());
        this.customerService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());

	}
/**
 * 添加
 */
	@RequestMapping("addCustomer")
	public ResultObj addCustomer(CustomerVo customerVo) {
		try {
			this.customerService.save(customerVo);
			return new ResultObj().ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().ADD_ERROR;
			
		}
		
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("updateCustomer")
	public ResultObj updateCustomer(CustomerVo customerVo) {
		try {
			this.customerService.updateById(customerVo);
			return new ResultObj().UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().UPDATE_ERROR;
		}
	}
	/**
	 * 删除
	 */
	@RequestMapping("deleteCustomer")
	public ResultObj deleteCustomer(Integer id ) {
		try {
			this.customerService.removeById(id);
			return new ResultObj().DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().DELETE_ERROR;
		}
	}
	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteCustomer")
	public ResultObj batchDeleteCustoer(CustomerVo customerVo) {
		Collection<Integer>idlist = new ArrayList<Integer>();
		for (Integer id : customerVo.getIds()) {
			idlist.add(id);
			
		}
		try {

			this.customerService.removeByIds(idlist);
			return new ResultObj().DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObj().DELETE_ERROR;
		}
	}
}

