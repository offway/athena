package cn.offway.athena.service;

import cn.offway.athena.domain.PhUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 用户信息Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhUserInfoService {

    PhUserInfo save(PhUserInfo phUserInfo);

    PhUserInfo findOne(Long id);

    PhUserInfo findByUnionid(String unionid);

    Page<PhUserInfo> findByPage(String nickname, String unionid, String phone, String id, String real_name, String isAuth, Pageable page);
}
