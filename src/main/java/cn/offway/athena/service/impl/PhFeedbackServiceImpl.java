package cn.offway.athena.service.impl;

import cn.offway.athena.domain.PhFeedback;
import cn.offway.athena.repository.PhFeedbackRepository;
import cn.offway.athena.service.PhFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * 反馈表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-25 15:10:52 Exp $
 */
@Service
public class PhFeedbackServiceImpl implements PhFeedbackService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhFeedbackRepository phFeedbackRepository;

    @Override
    public PhFeedback save(PhFeedback phFeedback) {
        return phFeedbackRepository.save(phFeedback);
    }

    @Override
    public PhFeedback findOne(Long id) {
        return phFeedbackRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phFeedbackRepository.delete(id);
    }

    @Override
    public Page<PhFeedback> findAll(Pageable pageable) {
        return phFeedbackRepository.findAll(pageable);
    }

    @Override
    public PhFeedback findByBrandId(Long id) {
        return phFeedbackRepository.findOne(new Specification<PhFeedback>() {
            @Override
            public Predicate toPredicate(Root<PhFeedback> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(cb.equal(root.get("brandId"), id));
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhFeedback> save(List<PhFeedback> entities) {
        return phFeedbackRepository.save(entities);
    }
}
