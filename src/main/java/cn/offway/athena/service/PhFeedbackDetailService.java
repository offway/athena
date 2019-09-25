package cn.offway.athena.service;


import java.util.List;

import cn.offway.athena.domain.PhFeedbackDetail;

/**
 * 反馈表明细Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-25 15:10:52 Exp $
 */
public interface PhFeedbackDetailService{

    PhFeedbackDetail save(PhFeedbackDetail phFeedbackDetail);
	
    PhFeedbackDetail findOne(Long id);

    void delete(Long id);

    List<PhFeedbackDetail> save(List<PhFeedbackDetail> entities);
}
