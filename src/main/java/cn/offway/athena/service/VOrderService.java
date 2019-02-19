package cn.offway.athena.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.athena.domain.VOrder;

/**
 * VIEWService接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface VOrderService{

	VOrder save(VOrder vOrder);
	
	VOrder findOne(Long id);

	Page<VOrder> findByPage(String orderNo, String unionid, Pageable page);
}
