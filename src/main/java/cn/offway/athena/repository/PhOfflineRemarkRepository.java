package cn.offway.athena.repository;

import cn.offway.athena.domain.PhOfflineRemark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 线下订单留言Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-09 13:33:39 Exp $
 */
public interface PhOfflineRemarkRepository extends JpaRepository<PhOfflineRemark, Long>, JpaSpecificationExecutor<PhOfflineRemark> {
    @Query(nativeQuery = true, value = "SELECT content FROM ph_offline_remark where orders_id = ?1 order by create_time desc limit 1")
    Object findLatest(Long id);
}
