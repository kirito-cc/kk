package com.sxt.bus.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.sxt.bus.domain.Customer;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Provider;



@Aspect
@Component
@EnableAspectJAutoProxy
public class BusinessCacheAspect {
	/**
	 * 日志出处
	 */
	private Log log = LogFactory.getLog(BusinessCacheAspect.class);
	
	/**
	 * 声明一个容器
	 */
	private static Map<String,Object>CACHE_CONTAINER = CachePool.CACHE_CONTAINER;
	public static Map<String,Object> getCACHE_CONTAINER(){
		return CACHE_CONTAINER;
	}
	//声明切面表达式
	private static final String POINTCUT_CUSTOMER_ADD = "execution(* com.sxt.bus.service.impl.CustomerServiceImpl.save(..))";
	private static final String POINTCUT_CUSTOMER_UPDATE = "execution(* com.sxt.bus.service.impl.CustomerServiceImpl.updateById(..))";
	private static final String POINTCUT_CUSTOMER_GET = "execution(* com.sxt.bus.service.impl.CustomerServiceImpl.getById(..))";
	private static final String POINTCUT_CUSTOMER_DELETE = "execution(* com.sxt.bus.service.impl.CustomerServiceImpl.removeById(..))";
	private static final String POINTCUT_CUSTOMER_BATCHDELETE = "execution(* com.sxt.bus.service.impl.CustomerServiceImpl.removeByIds(..))";

	
	private static final String CACHE_CUSTOMER_PROFIX = "customer:";
	
	
	/**
	 * 客户添加切入
	 * @throws Throwable 
	 */
	@Around(value = POINTCUT_CUSTOMER_ADD)
	public Object cacheCustomerAdd(ProceedingJoinPoint joinPoint) throws Throwable {
		//取出第一个参数
		Customer customer = (Customer)joinPoint.getArgs()[0];
		Boolean res = (Boolean)joinPoint.proceed();
		if(res) {
			CACHE_CONTAINER.put(CACHE_CUSTOMER_PROFIX+customer.getId(), customer);
		}
		return res;
	}
	/**
	 * 查询切入
	 * @throws Throwable 
	 */
	@Around(value = POINTCUT_CUSTOMER_GET)
	public Object cacheCustomerGet(ProceedingJoinPoint joinPoint) throws Throwable {
		//取出第一个参数
		Integer id = (Integer)joinPoint.getArgs()[0];
		Object res  = CACHE_CONTAINER.get(CACHE_CUSTOMER_PROFIX+id);
		if(res!=null) {
			log.info("已从缓存里面找到客户对象" + CACHE_CUSTOMER_PROFIX + id);
			return res;
		}else {
			Customer customer = (Customer)joinPoint.proceed();
			CACHE_CONTAINER.put(CACHE_CUSTOMER_PROFIX+customer.getId(),customer);
			log.info("未从缓存里面找到客户对象，去数据库查询并放到缓存"+CACHE_CUSTOMER_PROFIX+customer.getId());
			return customer;
		}	
	}
	/**
	 * @throws Throwable 
	 * 更新切入
	 * @throws  
	 */
	@Around(value = POINTCUT_CUSTOMER_UPDATE  )
	public Object cacheCustomerUpdate(ProceedingJoinPoint joinPoint) throws Throwable   {
		Customer deptVo = (Customer)joinPoint.getArgs()[0];
		Boolean isSuccess = (Boolean)joinPoint.proceed();
		if(isSuccess) {
			Customer dept  = (Customer)CACHE_CONTAINER.get(CACHE_CUSTOMER_PROFIX+deptVo.getId());
		    if(dept==null) {
		    	dept = new Customer();
		    }
		BeanUtils.copyProperties(deptVo, dept);
		log.info("客户对象缓存已更新" + CACHE_CUSTOMER_PROFIX + deptVo.getId());
		CACHE_CONTAINER.put(CACHE_CUSTOMER_PROFIX+dept.getId(), dept);
		
		}
		return isSuccess;
	}
	@Around(value = POINTCUT_CUSTOMER_DELETE)
	public Object cacheCustomerDelete(ProceedingJoinPoint joinPoint) throws Throwable {
		Integer id = (Integer)joinPoint.getArgs()[0];
		Boolean isSuccess = (Boolean)joinPoint.proceed();
		if(isSuccess) {
			//删除缓存
			CACHE_CONTAINER.remove(CACHE_CUSTOMER_PROFIX+id);
			log.info("客户对象缓存已删除" + CACHE_CUSTOMER_PROFIX + id);
		}
		return isSuccess;
	}
	@Around(value = POINTCUT_CUSTOMER_BATCHDELETE)
	public Object cacheCustomerBatchDelete(ProceedingJoinPoint joinPoint) {
		Collection<?extends Serializable>idList = (Collection)joinPoint.getArgs()[0];
		Boolean isSuccess = (Boolean)joinPoint.getArgs()[0];
		if(isSuccess) {
			for (Serializable id : idList) {
				CACHE_CONTAINER.remove(CACHE_CUSTOMER_PROFIX+id);
				log.info("客户对象缓存已删除" + CACHE_CUSTOMER_PROFIX + id);
			}
		}
		return isSuccess;
	}
	
	
	// 声明切面表达式
		private static final String POINTCUT_PROVIDER_ADD = "execution(* com.sxt.bus.service.impl.ProviderServiceImpl.save(..))";
		private static final String POINTCUT_PROVIDER_UPDATE = "execution(* com.sxt.bus.service.impl.ProviderServiceImpl.updateById(..))";
		private static final String POINTCUT_PROVIDER_GET = "execution(* com.sxt.bus.service.impl.ProviderServiceImpl.getById(..))";
		private static final String POINTCUT_PROVIDER_DELETE = "execution(* com.sxt.bus.service.impl.ProviderServiceImpl.removeById(..))";
		private static final String POINTCUT_PROVIDER_BATCHDELETE = "execution(* com.sxt.bus.service.impl.ProviderServiceImpl.removeByIds(..))";

		private static final String CACHE_PROVIDER_PROFIX = "provider:";
		/**
		 * 供应商添加切入
		 * @throws Throwable 
		 */
		@Around(value = POINTCUT_PROVIDER_ADD)
		public Object cacheProviderAdd(ProceedingJoinPoint joinPoint) throws Throwable {
			Provider provider = (Provider)joinPoint.getArgs()[0];
			Boolean res = (Boolean)joinPoint.proceed();
			if(res) {
				CACHE_CONTAINER.put(CACHE_PROVIDER_PROFIX+provider.getId(), provider);
				
			}
			return res;
		}
	/**
	 * 查询切入
	 * @throws Throwable 
	 */
		@Around(value = POINTCUT_PROVIDER_GET)
		public Object cacheProviderGet(ProceedingJoinPoint joinPoint) throws Throwable {
			Integer id = (Integer)joinPoint.getArgs()[0];
			
			Object object = CACHE_CONTAINER.get(CACHE_PROVIDER_PROFIX+id);
			if(object!=null) {
				log.info("已从缓存里面找到供应商对象" + CACHE_PROVIDER_PROFIX + id);
				return object;
			}else {
				Provider provider = (Provider)joinPoint.proceed();
				CACHE_CONTAINER.put(CACHE_PROVIDER_PROFIX+provider.getId(), provider);
			}
			
			return object;	
		}
		/**
		 * @throws Throwable 
		 * 更新切入
		 * @throws  
		 */
		@Around(value = POINTCUT_PROVIDER_UPDATE)
		public Object cacheProvideUpdate(ProceedingJoinPoint joinPoint) throws Throwable   {
			Provider provider = (Provider)joinPoint.getArgs()[0];
			Boolean isSuccess = (Boolean)joinPoint.proceed();
			if(isSuccess) {
				Provider providers = (Provider)CACHE_CONTAINER.get(CACHE_PROVIDER_PROFIX+provider.getId());
			  if(providers==null) {
				  providers = new Provider();
			  }
				
				BeanUtils.copyProperties(provider, providers);
				log.info("供应商对象缓存已更新" + CACHE_PROVIDER_PROFIX + provider.getId());
				CACHE_CONTAINER.put(CACHE_PROVIDER_PROFIX+providers.getId(), providers);
						}
			return isSuccess;
		}
		/**
		 * 删除切入
		 * @throws Throwable 
		 */
		@Around(value = POINTCUT_PROVIDER_DELETE)
		public Object cacheProviderUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
			Integer id = (Integer)joinPoint.getArgs()[0];
			Boolean isSuccess = (Boolean)joinPoint.proceed();
			if(isSuccess) {
				CACHE_CONTAINER.remove(CACHE_PROVIDER_PROFIX+id);
				log.info("供应商对象缓存已删除" + CACHE_PROVIDER_PROFIX + id);
			}
			return isSuccess;
		}
		/**
		 * 批量删除
		 * @throws Throwable 
		 */
		@Around(value = POINTCUT_PROVIDER_BATCHDELETE)
		public Object cacheProviderBatchDelete(ProceedingJoinPoint joinPoint) throws Throwable {
			Collection<Integer>idList = (Collection<Integer>)joinPoint.getArgs()[0];
			Boolean res = (Boolean) joinPoint.proceed();
			if(res) {
				for (Integer id : idList) {
					CACHE_CONTAINER.remove(CACHE_PROVIDER_PROFIX+id);
					log.info("供应商对象缓存已删除" + CACHE_PROVIDER_PROFIX + id);
				}
			}
			return res;
		}
		

		//商品数据的缓存 声明切面表达式
			private static final String POINTCUT_GOODS_ADD = "execution(* com.sxt.bus.service.impl.GoodsServiceImpl.save(..))";
			private static final String POINTCUT_GOODS_UPDATE = "execution(* com.sxt.bus.service.impl.GoodsServiceImpl.updateById(..))";
			private static final String POINTCUT_GOODS_GET = "execution(* com.sxt.bus.service.impl.GoodsServiceImpl.getById(..))";
			private static final String POINTCUT_GOODS_DELETE = "execution(* com.sxt.bus.service.impl.GoodsServiceImpl.removeById(..))";

			private static final String CACHE_GOODS_PROFIX = "goods:";
			
		/**
		 * @throws Throwable 
		 * 	商品添加切入
		 * @throws  
		 */
			@Around(value = POINTCUT_GOODS_ADD)
			public Object cacheGoodsAdd(ProceedingJoinPoint joinPoint) throws Throwable  {
				//获取第一个参数
				Goods object = (Goods)joinPoint.getArgs()[0];
				Boolean res = (Boolean)joinPoint.proceed();
				if(res) {
					CACHE_CONTAINER.put(CACHE_GOODS_PROFIX+object.getId(), object);
				}
				return res;
			}
			/**
			 * 查询切入
			 * @throws Throwable 
			 */
			@Around(value = POINTCUT_GOODS_GET)
			public Object cacheGoodsGet(ProceedingJoinPoint joinPoint) throws Throwable {
				//获取第一个参数
				Integer id = (Integer)joinPoint.getArgs()[0];
				//从缓存里面取
				Goods obj = (Goods)CACHE_CONTAINER.get(id);
				if(obj!=null) {
					log.info("已从缓存里面找到商品对象" + CACHE_GOODS_PROFIX + id);
					return obj;
				}else {
					Goods object = (Goods)joinPoint.proceed();
					CACHE_CONTAINER.put(CACHE_GOODS_PROFIX+object.getId(), object);
					log.info("未从缓存里面找到商品对象，去数据库查询并放到缓存" + CACHE_GOODS_PROFIX + object.getId());
					return object;
				}
			}
		/**
		 * 更新切入	
		 * @throws Throwable 
		 */
		@Around(value = POINTCUT_GOODS_UPDATE)	
		public Object cacheGoodsUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
			//取出第一个参数
			Goods object = (Goods)joinPoint.getArgs()[0];
			Boolean res1 = (Boolean)joinPoint.proceed();
			if(res1) {
				Goods obj = (Goods)CACHE_CONTAINER.get(CACHE_GOODS_PROFIX+object.getId());
				if(obj==null) {
					obj=new Goods();
				}
				BeanUtils.copyProperties(object, obj);
				log.info("商品对象缓存已更新" + CACHE_GOODS_PROFIX + obj.getId());
				CACHE_CONTAINER.put(CACHE_GOODS_PROFIX+obj.getId(), obj);
			}
			return res1;
		}
			
		/**
		 * 删除切入	
		 * @throws Throwable 
		 */
		@Around(value = POINTCUT_GOODS_DELETE)
		public Object cacheGoodsDelete(ProceedingJoinPoint joinPoint) throws Throwable {
			//获取第一个参数
			Integer id = (Integer)joinPoint.getArgs()[0];
			Boolean res = (Boolean)joinPoint.proceed();
			if(res) {
				CACHE_CONTAINER.remove(CACHE_GOODS_PROFIX+id);
				log.info("商品对象缓存已删除" + CACHE_GOODS_PROFIX + id);
			}
			return res;
		}
			
			
}
