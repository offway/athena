package cn.offway.athena.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.athena.domain.PhBannerHistory;

/**
 * Banner 历史Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-10 14:00:40 Exp $
 */
public interface PhBannerHistoryRepository extends JpaRepository<PhBannerHistory,Long>,JpaSpecificationExecutor<PhBannerHistory> {

	/** 此处写一些自定义的方法 **/
}
