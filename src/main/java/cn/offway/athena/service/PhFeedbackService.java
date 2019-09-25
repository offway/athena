package cn.offway.athena.service;


import java.util.List;

import cn.offway.athena.domain.PhFeedback;

/**
 * 反馈表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-25 15:10:52 Exp $
 */
public interface PhFeedbackService{

    PhFeedback save(PhFeedback phFeedback);
	
    PhFeedback findOne(Long id);

    void delete(Long id);

    List<PhFeedback> save(List<PhFeedback> entities);
}
