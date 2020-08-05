package com.sxt.bus.service.impl;

import com.sxt.bus.domain.Provider;
import com.sxt.bus.mapper.ProviderMapper;
import com.sxt.bus.service.IProviderService;
import com.sxt.bus.vo.ProviderVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 老雷
 * @since 2020-07-30
 */
@Service
@Transactional
public class ProviderServiceImpl extends ServiceImpl<ProviderMapper, Provider> implements IProviderService {

	
	@Override
	public boolean save(Provider providerVo) {
		return super.save(providerVo);
	}
	@Override
	public boolean updateById(Provider providerVO) {
		return super.updateById(providerVO);
	}
	@Override
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}
	@Override
	public boolean removeByIds(Collection<?extends Serializable> idList) {
		return super.removeByIds(idList);
	}
	@Override
	public Provider getById(Serializable id) {
		return super.getById(id);
	}
}
