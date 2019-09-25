package cn.offway.athena.service;


import cn.offway.athena.domain.PhFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 反馈表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-25 15:10:52 Exp $
 */
public interface PhFeedbackService {

    PhFeedback save(PhFeedback phFeedback);

    PhFeedback findOne(Long id);

    void delete(Long id);

    List<PhFeedback> save(List<PhFeedback> entities);

    Page<PhFeedback> findAll(Pageable pageable);
}
