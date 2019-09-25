package cn.offway.athena.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.athena.service.PhFeedbackDetailService;

import cn.offway.athena.domain.PhFeedbackDetail;
import cn.offway.athena.repository.PhFeedbackDetailRepository;


/**
 * 反馈表明细Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-25 15:10:52 Exp $
 */
@Service
public class PhFeedbackDetailServiceImpl implements PhFeedbackDetailService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhFeedbackDetailRepository phFeedbackDetailRepository;
	
	@Override
	public PhFeedbackDetail save(PhFeedbackDetail phFeedbackDetail){
		return phFeedbackDetailRepository.save(phFeedbackDetail);
	}
	
	@Override
	public PhFeedbackDetail findOne(Long id){
		return phFeedbackDetailRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phFeedbackDetailRepository.delete(id);
	}

	@Override
	public List<PhFeedbackDetail> save(List<PhFeedbackDetail> entities){
		return phFeedbackDetailRepository.save(entities);
	}
}
