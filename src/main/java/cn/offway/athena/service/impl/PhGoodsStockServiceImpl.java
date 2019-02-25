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
import cn.offway.athena.service.PhGoodsStockService;
import cn.offway.athena.domain.PhGoods;
import cn.offway.athena.domain.PhGoodsStock;
import cn.offway.athena.repository.PhGoodsStockRepository;


/**
 * 商品库存Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhGoodsStockServiceImpl implements PhGoodsStockService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsStockRepository phGoodsStockRepository;
	
	@Override
	public PhGoodsStock save(PhGoodsStock phGoodsStock){
		return phGoodsStockRepository.save(phGoodsStock);
	}
	
	@Override
	public PhGoodsStock findOne(Long id){
		return phGoodsStockRepository.findOne(id);
	}
	
	@Override
	public int countByGoodsIdAndColorAndSize(Long goodsId,String color,String size){
		return phGoodsStockRepository.countByGoodsIdAndColorAndSize(goodsId, color, size);
	}
	
	@Override
	public int updateStock(String orderNo){
		return phGoodsStockRepository.updateStock(orderNo);
	}
	
	@Override
	public Page<PhGoodsStock> findByPage(final Long brandId,final String brandName,final Long goodsId,final String goodsName,
			final String isOffway,final String color,final String size,Pageable page){
		return phGoodsStockRepository.findAll(new Specification<PhGoodsStock>() {
			
			@Override
			public Predicate toPredicate(Root<PhGoodsStock> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(brandName)){
					params.add(criteriaBuilder.like(root.get("brandName"), "%"+brandName+"%"));
				}
				
				if(null != brandId){
					params.add(criteriaBuilder.equal(root.get("brandId"), brandId));
				}
				
				if(StringUtils.isNotBlank(color)){
					params.add(criteriaBuilder.equal(root.get("color"), color));
				}
				
				if(StringUtils.isNotBlank(size)){
					params.add(criteriaBuilder.equal(root.get("size"), size));
				}
				
				if(null !=goodsId){
					params.add(criteriaBuilder.equal(root.get("goodsId"), goodsId));
				}
				
				if(StringUtils.isNotBlank(isOffway)){
					params.add(criteriaBuilder.equal(root.get("isOffway"), isOffway));
				}
				
				if(StringUtils.isNotBlank(goodsName)){
					params.add(criteriaBuilder.like(root.get("goodsName"), "%"+goodsName+"%"));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		}, page);
	}
}
