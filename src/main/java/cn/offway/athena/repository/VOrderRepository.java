package cn.offway.athena.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.athena.domain.VOrder;
import java.lang.String;
import java.util.List;


/**
 * VIEWRepository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface VOrderRepository extends JpaRepository<VOrder,Long>,JpaSpecificationExecutor<VOrder> {

	VOrder findByOrderNo(String orderNo);
}
