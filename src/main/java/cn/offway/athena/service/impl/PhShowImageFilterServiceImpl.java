package cn.offway.athena.service.impl;

import cn.offway.athena.domain.PhShowImageFilter;
import cn.offway.athena.repository.PhShowImageFilterRepository;
import cn.offway.athena.service.PhShowImageFilterService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 返图筛选Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhShowImageFilterServiceImpl implements PhShowImageFilterService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhShowImageFilterRepository phShowImageFilterRepository;

    @Override
    public PhShowImageFilter save(PhShowImageFilter phShowImageFilter) {
        return phShowImageFilterRepository.save(phShowImageFilter);
    }

    @Override
    public List<PhShowImageFilter> save(List<PhShowImageFilter> phShowImageFilters) {
        return phShowImageFilterRepository.save(phShowImageFilters);
    }

    @Override
    public PhShowImageFilter findOne(Long id) {
        return phShowImageFilterRepository.findOne(id);
    }

    @Override
    public int countByShowImage(String showImage) {
        return phShowImageFilterRepository.countByShowImage(showImage);
    }

    @Override
    public Page<PhShowImageFilter> findByPage(final Long brandId, final List<Long> brandIds, Date sTime, Date eTime, Pageable page) {
        return phShowImageFilterRepository.findAll(new Specification<PhShowImageFilter>() {
            @Override
            public Predicate toPredicate(Root<PhShowImageFilter> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();
                if (null != brandId) {
                    params.add(criteriaBuilder.equal(root.get("brandId"), brandId));
                }
                if (CollectionUtils.isNotEmpty(brandIds)) {
                    In<Object> in = criteriaBuilder.in(root.get("brandId"));
                    for (Object brandId : brandIds) {
                        in.value(brandId);
                    }
                    params.add(in);
                }
                if (sTime != null && eTime != null) {
                    params.add(criteriaBuilder.between(root.get("createTime"), sTime, eTime));
                } else if (sTime != null) {
                    params.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), sTime));
                } else if (eTime != null) {
                    params.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), eTime));
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, page);
    }
}
