package com.sxt.bus.util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sxt.sys.common.Constast;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;

/**
 * 文件上传下载工具
 * @author 24168
 *
 */
@Component
public class AppFileUtils {
	//文件上传的保存路径
	public static String UPDATE_PATH = "C:/upload/";
	static {
		//读取配置文件的储存位置
		InputStream stream = AppFileUtils.class.getClassLoader().getResourceAsStream("file.properties");
		Properties properties = new Properties();
		try {
		
			properties.load(stream);;
		} catch (Exception e) {
			e.printStackTrace();
		}
		String property = properties.getProperty("filepath");
		if(property!=null) {
			UPDATE_PATH = property;
		}
	}
	/**
	 * 根据文件老名字得到新名字
	 */
	public static String createNewFileName(String oldName) {
		String stuff = oldName.substring(oldName.lastIndexOf("."),oldName.length());
		return IdUtil.simpleUUID().toUpperCase()+stuff;
		
	}
	/**
	 * 文件下载
	 */
	public static ResponseEntity<Object> createResponseEntity(String path){
		//构造文件对象
		File  file = new File(UPDATE_PATH,path);
		if(file.exists()) {
			//将下载的文件封装byte[];
			byte[]bytes = null;
			try {
				bytes = FileUtil.readBytes(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//创建封装响应头信息的对象
			HttpHeaders header = new HttpHeaders();
			//封装响应类型姓名(APPLICATION_OCTET_STREAM)响应类型姓名不限定
			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			//设置下载的文件的名称
			//header.setContentDispositionFormData("attachment", "123.jpg");
			//创建responseEntity对象
			ResponseEntity<Object>entity = new ResponseEntity<Object>(bytes,header,HttpStatus.CREATED);
			return entity;

		}
		return null;
	}
	/**
	 * 根据路径名字去掉_temp
	 */
	public static String renameFile(String goodsimg) {
		File file = new File(UPDATE_PATH,goodsimg);
		String replace = goodsimg.replace("_temp", "");
		if(file.exists()) {
			file.renameTo(new File(UPDATE_PATH,replace));
		}
		return replace;
	}
/**
 *根据老路径删除图片
 */
	public static void removeFileByPath(String oldPath) {
		if (!oldPath.equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG)) {
			File file = new File(UPDATE_PATH, oldPath);
			if(file.exists()) {
				file.delete();
			}
		}
		
	}
}
