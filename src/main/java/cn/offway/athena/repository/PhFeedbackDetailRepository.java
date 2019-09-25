package cn.offway.athena.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.athena.domain.PhFeedbackDetail;

/**
 * 反馈表明细Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-25 15:10:52 Exp $
 */
public interface PhFeedbackDetailRepository extends JpaRepository<PhFeedbackDetail,Long>,JpaSpecificationExecutor<PhFeedbackDetail> {

	/** 此处写一些自定义的方法 **/
}
