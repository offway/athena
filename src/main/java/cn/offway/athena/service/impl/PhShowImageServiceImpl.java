package cn.offway.athena.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.offway.athena.domain.PhShowImage;
import cn.offway.athena.repository.PhShowImageRepository;
import cn.offway.athena.service.PhShowImageService;


/**
 * 晒图Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhShowImageServiceImpl implements PhShowImageService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhShowImageRepository phShowImageRepository;
	
	@Override
	public PhShowImage save(PhShowImage phShowImage){
		return phShowImageRepository.save(phShowImage);
	}
	
	@Override
	public PhShowImage findOne(Long id){
		return phShowImageRepository.findOne(id);
	}
	
	@Override
	public PhShowImage findByOrderNo(String orderNo){
		return phShowImageRepository.findByOrderNo(orderNo);
	}
	
	@Override
	public Page<PhShowImage> findByPage(final String orderNo,final String unionid,final String status,Pageable page){
		return phShowImageRepository.findAll(new Specification<PhShowImage>() {
			
			@Override
			public Predicate toPredicate(Root<PhShowImage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(orderNo)){
					params.add(criteriaBuilder.equal(root.get("orderNo"), orderNo));
				}
				
				if(StringUtils.isNotBlank(unionid)){
					params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
				}
				
				if(StringUtils.isNotBlank(status)){
					params.add(criteriaBuilder.equal(root.get("status"), status));
				}
				
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		}, page);
	}
}
