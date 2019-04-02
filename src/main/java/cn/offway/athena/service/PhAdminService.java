package cn.offway.athena.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.athena.domain.PhAdmin;

/**
 * Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhAdminService{

	PhAdmin save(PhAdmin phAdmin);
	
	PhAdmin findOne(Long id);

	void deleteAdmin(String ids) throws Exception;

	Page<PhAdmin> findByPage(String username, String nickname, Pageable page);

	void resetPwd(Long id) throws Exception;

	boolean editPwd(Long id, String password, String newpassword) throws Exception;

	void save(PhAdmin phAdmin, Long[] roleIds, String[] brandIds);
}
