package cn.offway.athena.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.athena.domain.PhOrderInfo;
import cn.offway.athena.utils.JsonResult;

/**
 * 订单Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhOrderInfoService{

	PhOrderInfo save(PhOrderInfo phOrderInfo);
	
	PhOrderInfo findOne(Long id);

	String generateOrderNo(String prefix);

	JsonResult saveOrder(String orderNo);

	PhOrderInfo findByOrderNo(String orderNo);

	Page<PhOrderInfo> findByPage(String orderNo, String unionid, String status, List<Long> brandIds, Pageable page);
}
