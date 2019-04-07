package cn.offway.athena.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.athena.domain.PhShowImageFilter;

/**
 * 返图筛选Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhShowImageFilterService{

	PhShowImageFilter save(PhShowImageFilter phShowImageFilter);
	
	PhShowImageFilter findOne(Long id);

	Page<PhShowImageFilter> findByPage(Long brandId, List<Long> brandIds, Pageable page);

	List<PhShowImageFilter> save(List<PhShowImageFilter> phShowImageFilters);

	int countByShowImage(String showImage);
}
