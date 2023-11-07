package com.shcorp.admin.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 관리자 로그인 서비스 인터페이스
 *
 * @author  kyle
 * @date    2020-08-13
 * @since   2.0
 */

public interface AdminLoginService {

	public Map<String, Object> selectMemberInfo (Map<String, Object>map) throws Exception;

	public int selectIdCnt (Map<String, Object>map) throws Exception;

	public int selectIdDupCheck (Map<String, Object>map) throws Exception;

	public int selectJoinIdCnt (Map<String, Object>map) throws Exception;

	public Map<String, Object> selectFindId (Map<String, Object>map) throws Exception;

	public Map<String, Object> selectPassword (Map<String, Object>map) throws Exception;

	public int updatePassword (Map<String, Object>map) throws Exception;
	
	public int insertRegistReq(Map<String, Object> map) throws Exception;
	
	public String selectKakao (HashMap<String, Object> map) throws Exception;
	
	public Object insertKakao (Map<String, Object> map) throws Exception;
	
	public Object updateTokenKakao (Map<String, Object> map) throws Exception;

	public String selectKakaoToken(HashMap<String, Object> map) throws Exception; 
	
	public String selectNameDuplicateCheck(String str) throws Exception;

	public Map<String, Object> selectTermOfUse (Map<String, Object>map) throws Exception;

	public Map<String, Object> selectTermOfPrivacyInfo (Map<String, Object>map) throws Exception;

}
