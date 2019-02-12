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

import cn.offway.athena.domain.PhAuth;
import cn.offway.athena.repository.PhAuthRepository;
import cn.offway.athena.service.PhAuthService;


/**
 * 用户认证Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhAuthServiceImpl implements PhAuthService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhAuthRepository phAuthRepository;
	
	@Override
	public PhAuth save(PhAuth phAuth){
		return phAuthRepository.save(phAuth);
	}
	
	@Override
	public PhAuth findOne(Long id){
		return phAuthRepository.findOne(id);
	}
	
	@Override
	public Page<PhAuth> findByPage(final String status,final String nickName,final String unionid,Pageable page){
		return phAuthRepository.findAll(new Specification<PhAuth>() {
			
			@Override
			public Predicate toPredicate(Root<PhAuth> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(status)){
					params.add(criteriaBuilder.equal(root.get("status"), status));
				}
				
				if(StringUtils.isNotBlank(nickName)){
					params.add(criteriaBuilder.like(root.get("nickname"), "%"+nickName+"%"));
				}
				
				if(StringUtils.isNotBlank(unionid)){
					params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		}, page);
	}
}
