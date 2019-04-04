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
import cn.offway.athena.service.PhBrandService;

import cn.offway.athena.domain.PhBrand;
import cn.offway.athena.domain.PhGoodsStock;
import cn.offway.athena.repository.PhBrandRepository;


/**
 * 品牌库Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhBrandServiceImpl implements PhBrandService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhBrandRepository phBrandRepository;
	
	@Override
	public PhBrand save(PhBrand phBrand){
		return phBrandRepository.save(phBrand);
	}
	
	@Override
	public PhBrand findOne(Long id){
		return phBrandRepository.findOne(id);
	}
	
	@Override
	public List<PhBrand> findAll(){
		return phBrandRepository.findAll();
	}
	
	@Override
	public List<PhBrand> findByIds(List<Long> ids){
		return phBrandRepository.findByIds(ids);
	}
	
	@Override
	public Page<PhBrand> findByPage(final Long brandId,final String brandName,Pageable page){
		return phBrandRepository.findAll(new Specification<PhBrand>() {
			
			@Override
			public Predicate toPredicate(Root<PhBrand> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(brandName)){
					params.add(criteriaBuilder.like(root.get("brandName"), "%"+brandName+"%"));
				}
				
				if(null != brandId){
					params.add(criteriaBuilder.equal(root.get("brandId"), brandId));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		}, page);
	}
	
	@Override
	public List<PhBrand> findByShowImgId(Long showImgId){
		return phBrandRepository.findByShowImgId(showImgId);
	}
	
}
