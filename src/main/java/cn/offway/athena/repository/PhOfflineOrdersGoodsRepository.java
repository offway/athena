package cn.offway.athena.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.athena.domain.PhOfflineOrdersGoods;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-27 13:41:55 Exp $
 */
public interface PhOfflineOrdersGoodsRepository extends JpaRepository<PhOfflineOrdersGoods,Long>,JpaSpecificationExecutor<PhOfflineOrdersGoods> {

	/** 此处写一些自定义的方法 **/
	List<PhOfflineOrdersGoods> findByOrdersNo(String ordersNo);

	@Transactional
	void deleteByOrdersNo(String ordersNo);

	List<PhOfflineOrdersGoods> findByBrandName(String brandName);
}
