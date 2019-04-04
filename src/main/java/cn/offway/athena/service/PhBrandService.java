package cn.offway.athena.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	List<PhBrand> findAll();

	Page<PhBrand> findByPage(Long brandId, String brandName, Pageable page);

	List<PhBrand> findByIds(List<Long> ids);

	List<PhBrand> findByShowImgId(Long showImgId);
}
