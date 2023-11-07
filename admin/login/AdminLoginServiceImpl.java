package com.shcorp.admin.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 관리자 로그인 서비스 구현체
 *
 * @author  kyle
 * @date    2020-08-13
 * @since   2.0
 */

@Service("adminLoginService")
public class AdminLoginServiceImpl implements AdminLoginService {

	@Autowired
	AdminLoginDao adminLoginDao;


	@Override
	public int selectJoinIdCnt(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectJoinIdCnt(map);
	}

	@Override
	public Map<String, Object> selectMemberInfo(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectMemberInfo(map);
	}

	@Override
	public int selectIdCnt(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectIdCnt(map);
	}

	@Override
	public int selectIdDupCheck(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectIdDupCheck(map);
	}

	@Override
	public Map<String, Object> selectFindId(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectFindId(map);
	}

	@Override
	public Map<String, Object> selectPassword (Map<String, Object>map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectPassword(map);
	}

	@Override
	public int updatePassword (Map<String, Object>map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.updatePassword(map);
	}
	
	@Override
	public int insertRegistReq(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.insertRegistReq(map);
	}
	
	@Override
	public String selectKakao(HashMap<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectKakao(map);
	}
	
	@Override
	public Object insertKakao(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.insertKakao(map);
	}
	
	@Override
	public Object updateTokenKakao(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.updateTokenKakao(map);
	}

	@Override
	public String selectKakaoToken(HashMap<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectKakaoToken(map);
	}
	
	@Override
	public String selectNameDuplicateCheck(String str) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectNameDuplicateCheck(str);
	}
	
	public Map<String, Object> selectTermOfUse (Map<String, Object>map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectTermOfUse(map);
	}

	public Map<String, Object> selectTermOfPrivacyInfo (Map<String, Object>map) throws Exception {
		// TODO Auto-generated method stub
		return adminLoginDao.selectTermOfPrivacyInfo(map);
	}

	
}
