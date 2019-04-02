package cn.offway.athena.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.athena.domain.PhShowImage;

/**
 * 晒图Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhShowImageService{

	PhShowImage save(PhShowImage phShowImage);
	
	PhShowImage findOne(Long id);

	PhShowImage findByOrderNo(String orderNo);

	Page<PhShowImage> findByPage(String orderNo, String unionid, String status, List<Long> brandIds, Pageable page);
}
