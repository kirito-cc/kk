package com.sxt.bus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author 24168
 *
 */
@Controller
@RequestMapping("/bus")
public class BusinessController {
	/**
	 * 跳转到客户管理
	 */
	@RequestMapping("toCustomerManager")
	public String toCustomerManager() {
		return "business/customer/customerManager";
		
	}
	/**
	 * 跳转到供应商管理
	 */
	@RequestMapping("toProviderManager")
	public String toProviderManager() {
		return "business/provider/providerManager";
	}
	/**
	 * 跳转到货品管理页面
	 */
	@RequestMapping("toGoodsManager")
	public String toGoodsManager() {
		return "business/goods/goodsManager";
	}
	
	/**
	 * 跳转到进货管理页面
	 */
	@RequestMapping("toInportManager")
	public String toInportManager() {
		return "business/inport/inportManager";
	}
	/**
	 * 跳转到退货货管理页面
	 */
	@RequestMapping("toOutportManager")
	public String toOutportManager() {
		return "business/Outport/OutportManager";
	}
}
