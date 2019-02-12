package cn.offway.athena.service;

import cn.offway.athena.domain.PhBrand;

/**
 * 品牌库Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhBrandService{

	PhBrand save(PhBrand phBrand);
	
	PhBrand findOne(Long id);
}
