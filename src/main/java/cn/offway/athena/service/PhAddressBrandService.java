package cn.offway.athena.service;


import java.util.List;

import cn.offway.athena.domain.PhAddressBrand;

/**
 * 品牌地址管理Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-02-11 15:28:49 Exp $
 */
public interface PhAddressBrandService{

    PhAddressBrand save(PhAddressBrand phAddressBrand);
	
    PhAddressBrand findOne(Long id);

    void delete(Long id);

    List<PhAddressBrand> save(List<PhAddressBrand> entities);
}
