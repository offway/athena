package cn.offway.athena.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.athena.service.PhBannerHistoryService;

import cn.offway.athena.domain.PhBannerHistory;
import cn.offway.athena.repository.PhBannerHistoryRepository;


/**
 * Banner 历史Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-10 14:00:40 Exp $
 */
@Service
public class PhBannerHistoryServiceImpl implements PhBannerHistoryService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhBannerHistoryRepository phBannerHistoryRepository;
	
	@Override
	public PhBannerHistory save(PhBannerHistory phBannerHistory){
		return phBannerHistoryRepository.save(phBannerHistory);
	}
	
	@Override
	public PhBannerHistory findOne(Long id){
		return phBannerHistoryRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phBannerHistoryRepository.delete(id);
	}

	@Override
	public List<PhBannerHistory> save(List<PhBannerHistory> entities){
		return phBannerHistoryRepository.save(entities);
	}
}
