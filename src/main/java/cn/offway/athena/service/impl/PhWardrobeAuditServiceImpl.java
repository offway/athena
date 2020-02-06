package cn.offway.athena.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.athena.service.PhWardrobeAuditService;

import cn.offway.athena.domain.PhWardrobeAudit;
import cn.offway.athena.repository.PhWardrobeAuditRepository;


/**
 * 衣柜审核Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-02-06 19:20:56 Exp $
 */
@Service
public class PhWardrobeAuditServiceImpl implements PhWardrobeAuditService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhWardrobeAuditRepository phWardrobeAuditRepository;
	
	@Override
	public PhWardrobeAudit save(PhWardrobeAudit phWardrobeAudit){
		return phWardrobeAuditRepository.save(phWardrobeAudit);
	}
	
	@Override
	public PhWardrobeAudit findOne(Long id){
		return phWardrobeAuditRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phWardrobeAuditRepository.delete(id);
	}

	@Override
	public List<PhWardrobeAudit> save(List<PhWardrobeAudit> entities){
		return phWardrobeAuditRepository.save(entities);
	}
}
