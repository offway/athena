package cn.offway.athena.service;

import java.util.List;

import cn.offway.athena.domain.PhBrandadmin;

/**
 * Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-01 11:26:00 Exp $
 */
public interface PhBrandadminService{

	PhBrandadmin save(PhBrandadmin phBrandadmin);
	
	PhBrandadmin findOne(Long id);

	List<Long> findBrandIdByAdminId(Long adminId);
}
