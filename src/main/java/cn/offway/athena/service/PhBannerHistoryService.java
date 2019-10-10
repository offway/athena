package cn.offway.athena.service;


import java.util.List;

import cn.offway.athena.domain.PhBannerHistory;

/**
 * Banner 历史Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-10 14:00:40 Exp $
 */
public interface PhBannerHistoryService{

    PhBannerHistory save(PhBannerHistory phBannerHistory);
	
    PhBannerHistory findOne(Long id);

    void delete(Long id);

    List<PhBannerHistory> save(List<PhBannerHistory> entities);
}
