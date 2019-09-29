package cn.offway.athena.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.athena.service.PhOfflineOrdersService;

import cn.offway.athena.domain.PhOfflineOrders;
import cn.offway.athena.repository.PhOfflineOrdersRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-27 13:41:55 Exp $
 */
@Service
public class PhOfflineOrdersServiceImpl implements PhOfflineOrdersService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhOfflineOrdersRepository phOfflineOrdersRepository;
	
	@Override
	public PhOfflineOrders save(PhOfflineOrders phOfflineOrders){
		return phOfflineOrdersRepository.save(phOfflineOrders);
	}
	
	@Override
	public PhOfflineOrders findOne(Long id){
		return phOfflineOrdersRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phOfflineOrdersRepository.delete(id);
	}

	@Override
	public List<PhOfflineOrders> save(List<PhOfflineOrders> entities){
		return phOfflineOrdersRepository.save(entities);
	}

	@Override
	public Page<PhOfflineOrders> findByPage(String realName, String users, String state, String ordersNo,Pageable page){
		return phOfflineOrdersRepository.findAll(new Specification<PhOfflineOrders>() {
			@Override
			public Predicate toPredicate(Root<PhOfflineOrders> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				if (StringUtils.isNotBlank(realName)){
					params.add(criteriaBuilder.equal(root.get("realName"),realName));
				}
				if (StringUtils.isNotBlank(users)){
					params.add(criteriaBuilder.equal(root.get("users"),users));
				}
				if (StringUtils.isNotBlank(state)){
					params.add(criteriaBuilder.equal(root.get("state"),state));
				}
				if (StringUtils.isNotBlank(ordersNo)){
					params.add(criteriaBuilder.equal(root.get("ordersNo"),ordersNo));
				}
				Predicate[] predicates = new Predicate[params.size()];
				criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		},page);
	}
}
