package cn.offway.athena.service;


import java.util.List;

import cn.offway.athena.domain.PhWardrobeAudit;

/**
 * 衣柜审核Service接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-02-06 19:20:56 Exp $
 */
public interface PhWardrobeAuditService{

    PhWardrobeAudit save(PhWardrobeAudit phWardrobeAudit);
	
    PhWardrobeAudit findOne(Long id);

    void delete(Long id);

    List<PhWardrobeAudit> save(List<PhWardrobeAudit> entities);
}
