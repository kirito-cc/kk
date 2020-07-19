package com.sxt.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sys")
public class SystemController {
	/*
	 * 跳转到登陆页面
	 */
	@RequestMapping("toLogin")
  public String  toLogin() {
	   return "system/index/login";
  }
	
	
	/**
	 * 跳转首页
	 */
	@RequestMapping("index")
	public String index() {
		return "system/index/index";
	}
	
	/*
	 * 跳转到工作台
	 */
	@RequestMapping("toDeskManager")
	public String toDeskManager() {
		return "system/index/deskManager";
	}
	
	/*
	 * 
	 * 跳转到日志管理
	 */
	
	@RequestMapping("toLoginfoManager")
	public String toLoginfoManager() {
		return "system/loginfo/loginfoManager";
	}
	
	/**
	 * 跳转到公告管理
	 */
	
	@RequestMapping("toNoticeManager")
	public String toNoticeManager() {
		return "system/notice/noticeManager";
	}
	/**
	 * 跳转到部门管理
	 */
	
	@RequestMapping("toDeptManager")
	public String toDeptManager() {
		return "system/dept/deptManager";
	}
	
	/**
	 * 跳转到部门管理-left
	 */
	
	@RequestMapping("toDeptLeft")
	public String ToDeptLeft() {
		return "system/dept/DeptLeft";
	}
	
	/**
	 * 跳转到部门管理-right
	 */
	
	@RequestMapping("toDeptRight")
	public String ToDeptRight() {
		return "system/dept/DeptRight";
	}
	/**
	 * 跳转到菜单管理
	 * 
	 */
	@RequestMapping("toMenuManager")
	public String toMenuManager() {
		return "system/menu/menuManager";
	}
	
	/**
	 * 跳转到菜单管理-left
	 * 
	 */
	@RequestMapping("toMenuLeft")
	public String toMenuLeft() {
		return "system/menu/menuLeft";
	}
	
	
	/**
	 * 跳转到菜单管理--right
	 * 
	 */
	@RequestMapping("toMenuRight")
	public String toMenuRight() {
		return "system/menu/menuRight";
	}
}
