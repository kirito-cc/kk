package com.sxt.bus.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.sxt.bus.util.AppFileUtils;

import cn.hutool.core.date.DateUtil;


@RestController
@RequestMapping("file")
public class FileController {
	
	/**
	 * 上传文件
	 */
	@RequestMapping("uploadFile")
	public Map<String,Object>uploadFile(MultipartFile mf){
		//1,得到文件名
		String oldName = mf.getOriginalFilename();
		//2,根据文件名生成新的文件名
		String newName = AppFileUtils.createNewFileName(oldName);
		//得到当前日期的字符串
		String dirName = DateUtil.format(new Date(), "yy-MM-dd");
		//构建文件夹
		File dirFile = new File(AppFileUtils.UPDATE_PATH,dirName);
		//判断文件夹是否存在
		if(!dirFile.exists()) {
			dirFile.mkdirs();//创建文件夹
		}
		//构建文件对象
	    File file = new File(dirFile,newName+"_temp");
	    //把mf里面的图片信息写进file里
	    try {
			mf.transferTo(file);
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	    Map<String,Object>map = new HashMap<String, Object>();
	    map.put("path", dirName+"/"+newName+"_temp");
	    return map;
		
	}
	/**
	 * 图片下载
	 */
	@RequestMapping("showImageByPath")
	public ResponseEntity<Object>showImagePath(String path){
		return AppFileUtils.createResponseEntity(path);
	}
	

}
