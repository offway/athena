package cn.offway.athena.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.athena.domain.PhOrderExpressInfo;

/**
 * 订单物流Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhOrderExpressInfoRepository extends JpaRepository<PhOrderExpressInfo,Long>,JpaSpecificationExecutor<PhOrderExpressInfo> {

	/** 此处写一些自定义的方法 **/
}
