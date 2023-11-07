package com.shcorp.admin.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.shcorp.common.dao.AbstractDAO;

/**
 * 코트 정보  DAO
 *
 * @author  kyle
 * @date    2020-08-13
 * @since   2.0
 */

@Repository("adminLoginDao")
public class AdminLoginDao extends AbstractDAO{

	@SuppressWarnings("unchecked")
	public Map<String, Object> selectCourtInfo(Map<String, Object> map) throws Exception{
		return (Map<String, Object>)selectOne("adminLogin.view", map);
	}


	public void insertCourtInfo(Map<String, Object>map) throws Exception{
		insert("adminLogin.insert", map);
	}

	public void updateCourtInfo(Map<String, Object>map) throws Exception{
		insert("adminLogin.update", map);
	}

	public int selectIdCnt(Map<String, Object> map) throws Exception{
		return (int) selectOne("adminLogin.selectIdCnt", map);
	}

	public int selectIdDupCheck(Map<String, Object> map) throws Exception{
		return (int) selectOne("adminLogin.selectIdDupCheck", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> selectMemberInfo(Map<String, Object> map) {
		return (Map<String, Object>)selectOne("adminLogin.selectMemberInfo", map);
	}

	public int selectJoinIdCnt(Map<String, Object> map) {
		return (int) selectOne("adminLogin.selectJoinIdCnt", map);
	}


	public Map<String, Object> selectFindId(Map<String, Object> map) {
		return (Map<String, Object>) selectOne("adminLogin.selectFindId", map);
	}

	public Map<String, Object> selectPassword(Map<String, Object> map) throws Exception{
		return (Map<String, Object> ) selectOne("adminLogin.selectPassword", map);
	}

	public int updatePassword(Map<String, Object> map) throws Exception{
		return (int)update("adminLogin.updatePassword", map);
	}

	public int insertRegistReq(Map<String, Object> map) throws Exception{
		return (int) insert("adminLogin.insertRegistReq", map);
	}
	
	public String selectKakao(HashMap<String, Object> map) throws Exception{
		return (String) selectOne("adminLogin.selectKakao", map);
	}
	
	public Object insertKakao(Map<String, Object> map) throws Exception{
		return insert("adminLogin.insertKakao", map);
	}
	
	public Object updateTokenKakao(Map<String, Object> map) throws Exception{
		return update("adminLogin.updateTokenKakao", map);
	}
	
	public String selectKakaoToken(HashMap<String, Object> map) throws Exception{
		return (String) selectOne("adminLogin.selectKakaoToken", map);
	}
	
	public String selectNameDuplicateCheck(String str) throws Exception{
		return (String) selectOne("adminLogin.selectNameDuplicateCheck", str);
	}

	public Map<String, Object> selectTermOfUse (Map<String, Object>map) throws Exception {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("adminLogin.selectTermOfUse", map);
	}

	public Map<String, Object> selectTermOfPrivacyInfo (Map<String, Object>map) throws Exception {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("adminLogin.selectTermOfPrivacyInfo", map);
	}
}
