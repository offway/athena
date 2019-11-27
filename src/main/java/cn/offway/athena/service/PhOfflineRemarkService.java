package cn.offway.athena.service;


import cn.offway.athena.domain.PhOfflineRemark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 线下订单留言Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-09 13:33:39 Exp $
 */
public interface PhOfflineRemarkService {

    PhOfflineRemark save(PhOfflineRemark phOfflineRemark);

    PhOfflineRemark findOne(Long id);

    void delete(Long id);

    List<PhOfflineRemark> save(List<PhOfflineRemark> entities);

    Page<PhOfflineRemark> findAllByPage(String id, Pageable page);

    String findLatest(Long id);
}
