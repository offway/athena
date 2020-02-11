package cn.offway.athena.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.athena.domain.PhAddressBrand;

/**
 * 品牌地址管理Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-02-11 15:28:49 Exp $
 */
public interface PhAddressBrandRepository extends JpaRepository<PhAddressBrand,Long>,JpaSpecificationExecutor<PhAddressBrand> {

	/** 此处写一些自定义的方法 **/
}
