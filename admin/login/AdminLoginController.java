package com.shcorp.admin.login;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shcorp.apple.appleAPI;
import com.shcorp.common.service.CommonService;
import com.shcorp.common.util.Sha256Util;
import com.shcorp.kakao.kakaoAPI;

/**
 * 로그인 서비스 인터페이스
 *
 * @author  CSH
 * @date    2022.09.15
 * @since   2.0
 */
@Controller

public class AdminLoginController {
	Logger LOG = Logger.getLogger(this.getClass());

	@Autowired
	private AdminLoginService adminLoginService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
    private kakaoAPI kakao;
	
	@Autowired
    private appleAPI apple;
	
	@Value(value = "#{props['kakao.apiKey']}")
	private String kakaoApiKey;
	
	@Value(value = "#{props['kakao.redirect.url']}")
	private String kakaoRedirectUrl;
	
	@Value(value = "#{props['apple.team.id']}")
	private String appleTeamId;
	
	@Value(value = "#{props['apple.login.key']}")
	private String appleLoginKey;
	
	@Value(value = "#{props['apple.client.id']}")
	private String appleClientId;
	
	@Value(value = "#{props['apple.redirect.url']}")
	private String appleRedirectUrl;
	
	@Value(value = "#{props['apple.key.path']}")
	private String appleKeyPath;
	

    /**
	 * 로그인 화면 호출
	 * @return ModelAndView
	 * @exception Exception
	 */
	@GetMapping(value="/admin/adminLogin.do")
	public ModelAndView adminLogin() throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController adminLogin :: START");
		}
		
		ModelAndView mv = new ModelAndView("/CertiNaraManager/login/adminLogin");
		// 기관코드
		List<Map<String, Object>> agencyGubun = commonService.selectCode01("01"); /*01:기관구분코드, 02:로케이션구분코드*/
		
		mv.addObject("agencyGubunCd", agencyGubun);
		
		mv.addObject("kakaoApiKey", kakaoApiKey);
		mv.addObject("kakaoRedirectUrl", kakaoRedirectUrl);
		
		mv.addObject("appleLoginKey", appleLoginKey);
		mv.addObject("appleClientId", appleClientId);
		mv.addObject("appleRedirectUrl", appleRedirectUrl);
		mv.addObject("appleKeyPath", appleKeyPath);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController adminLogin :: END");
		}
		
		return mv;
	}

    /**
	 * 로그인 및 사용자 정보 확인
	 * @param Map
	 * @param HttpServletRequest
	 * @param HttpSession 
	 * @return ModelAndView
	 * @exception Exception
	 */
	// 
	@RequestMapping(value="/admin/loginAction.do")
	public String loginAction(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpSession session) throws Exception{
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController loginAction :: START");
		}

		String mv = "abcdefg";
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("### loginAction :: userid=" + map.get("userId"));
		}

		int count = adminLoginService.selectIdCnt(map);
    	if(count == 1) {
    		// 사용자 정보 select 및 session 저장
    		Map<String,Object> adminLoginMap = adminLoginService.selectMemberInfo(map);
    		
    		//동의항목 체크가 안되어있으면
    		if(!adminLoginMap.get("ADMISSION").equals("P")) { // 사용 승인이 나지 않았을때
//    			result = "warning";
    		}
    		else {
    			adminLoginService.updateTokenKakao(map);
    			
	    		session.setAttribute("adminLoginMap", adminLoginMap);
				session.setMaxInactiveInterval(60*60*24*365);
				
				mv = "redirect:/main.do";
				
				if (LOG.isDebugEnabled()) {
					Map<String, Object> retMap = (Map)session.getAttribute("adminLoginMap");
					LOG.debug("### sessiion adminLoginMap :: USER_ID=" + retMap.get("USER_ID") + ", agency_name=" + retMap.get("AGENCY_NAME"));
				}
//				result = "success";
    		}
    	}else {
//    		result = "failed";
    		adminLoginService.insertKakao(map);
    		mv = "redirect:/login/agree.do";
    	}
//    	mv.addObject("result", result);
    	
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController loginAction :: END");
		}
		
    	return mv;
    }

    /**
	 * 로그아웃한다.
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping(value="/logout.do")
	public String actionLogout(HttpServletRequest request, ModelMap model) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController actionLogout :: START");
		}
		
    	request.getSession().setAttribute("adminLoginMap", null);
    	 
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController actionLogout :: END");
		}
		
    	return "redirect:/admin/login.do";
    }

    /**
	 * 아이디 찾기
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value="/ajax/searchId.do")
	public ModelAndView selectFindId(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpSession session) throws Exception{
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController selectFindId :: START");
		}
		
    	ModelAndView mv = new ModelAndView("jsonView");
    	
    	String result = "";
		Map<String,Object> searchIdMap = adminLoginService.selectFindId(map);
		if (searchIdMap != null) {
	    	mv.addObject("resultMap", searchIdMap);
			result = "success";
		}
		else {
			result = "failed";
		}
		
    	mv.addObject("result", result);
    	
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController selectFindId :: END");
		}
		
    	return mv;
    }

   /**
	 * 아이디 중복확인
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value="/ajax/idDupCheck.do")
	public ModelAndView selectIdDupCheck(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpSession session) throws Exception{
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController selectIdDupCheck :: START");
		}
		
    	ModelAndView mv = new ModelAndView("jsonView");
    	
    	String result = "";
		int cnt = adminLoginService.selectIdDupCheck(map);

		if (cnt < 1) {
			result = "success";
		}
		else {
			result = "failed";
		}
    	mv.addObject("result", result);
    	
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController selectIdDupCheck :: END");
		}
		
    	return mv;
    }

    /**
	 * 비밀번호 찾기 (사용자명, 핸드폰번호)
	 * @param Map
	 * @param HttpServletRequest
	 * @param HttpSession 
	 * @return ModelAndView
	 * @exception Exception
	 */
	// 
	@RequestMapping(value="/ajax/findPass.do")
	public ModelAndView selectPassword(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpSession session) throws Exception{

		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController selectPassword :: START");
		}

		ModelAndView mv = new ModelAndView("jsonView");
		String result = "";
		// 사용자명과 핸드폰번호로 비밀번호찾기
		Map<String,Object> searchPassMap = adminLoginService.selectPassword(map);
		if (searchPassMap != null) {
			// SHA256으로 암호화를 했을 경우 복호화 할 수 없어서, 비번을 사용자ID로 초기화 시켜준다.
			searchPassMap.replace("USER_PASS", searchPassMap.get("USER_ID"));
			
			// 사용자ID로 초기화 시킨 비번을 update 한다.
			String newpw = Sha256Util.getEncSha256(searchPassMap.get("USER_ID").toString());
			Map<String,Object> updatePassMap = searchPassMap;
			updatePassMap.replace("USER_PASS", newpw);

			long updateResult = adminLoginService.updatePassword(updatePassMap);
			
	    	mv.addObject("resultMap", searchPassMap);
			result = "success";

			if (LOG.isDebugEnabled()) {
				LOG.debug("### AdminLoginController selectPassword result :: USER_ID=" + searchPassMap.get("USER_ID") + ", USER_PASS=" + searchPassMap.get("USER_PASS"));
			}
		}
		else {
			result = "failed";

			if (LOG.isDebugEnabled()) {
				LOG.debug("### AdminLoginController selectPassword result :: NOT Founded password user_name=" + map.get("user_name") + ", user_mobile=" + map.get("user_mobile"));
			}
		}

    	mv.addObject("result", result);
    	
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController selectPassword :: END");
		}
		
    	return mv;
    }

    /**
	 * 사용자 등록요청
	 * @param Map
	 * @param HttpServletRequest
	 * @param HttpSession 
	 * @return ModelAndView
	 * @exception Exception
	 */
	@RequestMapping(value="/reg/registReq.do")
//	public ModelAndView insertRegistReq(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpSession session) throws Exception{
	public String insertRegistReq(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpSession session) throws Exception{

		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController insertRegistReq :: START : map userId=" + map.get("userId") + ", accessToken=" + map.get("accessToken"));
		}

//		ModelAndView mv = new ModelAndView("jsonView");
		ModelAndView mv = new ModelAndView("/CertiNaraManager/main/mainView");
//		ModelAndView mv = new ModelAndView("/CertiNaraManager/quote/quoteContact");
		String result = "success";

//    	String pw = (String) map.get("user_pass");    	
//		pw = Sha256Util.getEncSha256(pw);
//		map.replace("user_pass", pw);
//		map.put("agency_cd", "0");	// 기관코드 (사용자가 등록이 않된 상태라서 기관코드를 기본값인 0을 설정한다.
//		map.put("admission", "R"); // 승인여부(R:신청)
//		map.put("auth_cd", "ROLE_ANONYMOUS"); // 사용자권한코드(ROLE_ADMIN,ROLE_ANONYMOUS,ROLE_MANAGER,ROLE_USER_MEMBER)
		

		// 사용자정보 등록요청을 insert
		int insertResult = adminLoginService.insertRegistReq(map);

		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController insertRegistReq result :: " + insertResult);
		}

		// 등록된 사용자 정보 얻어서 session에 담기
		Map<String,Object> adminLoginMap = adminLoginService.selectMemberInfo(map);
		
		if (adminLoginMap != null) {
    		session.setAttribute("adminLoginMap", adminLoginMap);
			session.setMaxInactiveInterval(60*60*24);
   		
    		if (LOG.isDebugEnabled()) {
    			Map<String, Object> retMap = (Map)session.getAttribute("adminLoginMap");
    			LOG.debug("### session adminLoginMap :: userId=" + retMap.get("userId") + ", userName=" + retMap.get("userName"));
    		}
		}

    	mv.addObject("result", result);
    	
		if (LOG.isDebugEnabled()) {
			LOG.debug("### AdminLoginController insertRegistReq :: END");
		}
		
//    	return mv;
		return "redirect:/main.do";
    }

	/**
	 * 카카오 로그인
	 * View에서 apiKey와 redirect url로 현재 Mapping url을 던진뒤 카카오에서 호출해 주는 return callback method 
	 * @return String
	 * @exception Exception
	 */
	@GetMapping(value="/oauth/kakao.do")
//	public String kakaoLogin(@RequestParam("code") String code, HttpSession session, @RequestParam Map<String,Object> map) throws Exception{
	public String kakaoLogin(@RequestParam("code") String code, HttpSession session, ModelMap model) throws Exception{
//	public ModelAndView kakaoLogin(@RequestParam("code") String code, HttpServletRequest request, HttpSession session) throws Exception{

		if (LOG.isDebugEnabled()) {
			LOG.debug("============================== AdminLoginController.kakaoLogin Start ==============================");
			LOG.debug("[AdminLoginController.kakaoLogin] code ={"+code+"}");
		}

		ModelAndView mv = null; // new ModelAndView("jsonView");
		Map<String,Object> map = new HashMap<String, Object>();
		
		// 카카오의 Aouth 엑세스 토큰을 얻어온다.
		String access_Token = kakao.getAccessToken(code);		
		// access token에 의한 user 정보 얻기
		HashMap<String, Object> userInfo = kakao.getUserInfo(access_Token);
		
		String resultPage = "";
				
		map.put("userId", userInfo.get("userId")); 	// 카카오 user id
		map.put("accessToken", access_Token);		// 카카오 access token
		
		map.put("agencyCd", "0");					// 기관코드 (사용자가 등록이 않된 상태라서 기관코드를 기본값인 0을 설정한다.
		map.put("admission", "P"); 					// 승인여부(P:승인, R:신청)
		map.put("authCd", "ROLE_MEMBER"); 			// 사용자권한코드(ROLE_SYSADMIN:시스템관리자, ROLE_ADMIN:업체관리자, ROLE_MANAGER:공급자, ROLE_MEMBER:수요자, ROLE_ANONYMOUS:익명사용자)
		
		int count = adminLoginService.selectIdCnt(map);
    	if (count == 1) {
    		// 사용자 정보 select 및 session 저장
    		mv = new ModelAndView("/CertiNaraManager/main/mainView");

    		Map<String,Object> adminLoginMap = adminLoginService.selectMemberInfo(map);
    		
    		if (adminLoginMap != null) {
	    		session.setAttribute("adminLoginMap", adminLoginMap);
	    		
	    		if (LOG.isDebugEnabled()) {
	    			Map<String, Object> retMap = (Map)session.getAttribute("adminLoginMap");
	    			LOG.debug("### kakaoLogin :: session adminLoginMap : userId=" + retMap.get("userId") + ", userName=" + retMap.get("userName"));
	    		}
    		}
    		
    		return "redirect:/main.do";
    	}
    	else {
//    		mv = new ModelAndView("/CertiNaraManager/agree/agreement"); // "redirect:/login/agree.do";
//
//    		if (LOG.isDebugEnabled()) {
//				LOG.debug("### kakaoLogin ::  session adminLoginMap : userId=" + map.get("userId") + ", accessTokey=" + map.get("accessToken"));
//			}
//    		
//    		mv.addObject("userMap", map);
//			
    		Map<String, Object> termOfUseMap = adminLoginService.selectTermOfUse(map);
//    		mv.addObject("termOfUse", termOfUseMap);
    		
    		Map<String, Object> termOfPrivacyInfo = adminLoginService.selectTermOfPrivacyInfo(map);
//    		mv.addObject("termOfPrivacyInfo", termOfPrivacyInfo);
  
			model.addAttribute("userMap", map);
			model.addAttribute("termOfUse", termOfUseMap);
			model.addAttribute("termOfPrivacyInfo", termOfPrivacyInfo);

    		return "/CertiNaraManager/agree/agreement";
    	}
		// session.setAttribute("userId", userInfo.get("userId"));
		
//		String returnPage = loginAction(map, null, session);
//		
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("[AdminLoginController.kakaoLogin] adminLoginMap =" + session.getAttribute("adminLoginMap"));
//			LOG.debug("============================== AdminLoginController.kakaoLogin End ==============================");
//		}
//		
//		return returnPage;
//    	return mv;
	}
	
	/**
	 * 카카오 로그아웃
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/ajax/kakao/logout.do")
	public String logout(HttpSession session) throws Exception {
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("============================== AdminLoginController.kakaoLogout Start ==============================");
		}
		
		Map<String, Object> sessionMap = (HashMap<String, Object>) session.getAttribute("adminLoginMap"); //
		HashMap<String,Object> map = new HashMap<String,Object>();
		String access_Token = "";
		
		map.put("userId", sessionMap.get("userId")); //.getAttribute("userId"));
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("[AdminLoginController.kakaoLogout] userId={" + map.get("userId")); //.getAttribute("userId") + "}");
		}

		access_Token = adminLoginService.selectKakaoToken(map);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("[AdminLoginController.kakaoLogout] access_Token =" + access_Token); //session.getAttribute("adminLoginMap"));
		}
		
//		kakao.kakaoLogout(session.getAttribute("userId").toString(), access_Token);
		kakao.kakaoLogout(sessionMap.get("userId").toString(), access_Token);
	    session.invalidate();
	    
		if (LOG.isDebugEnabled()) {
			LOG.debug("============================== AdminLoginController.kakaoLogout End ==============================");
		}
		
	    return "redirect:/admin/adminLogin.do";
	}
	
	@RequestMapping(value="/kakao/unlink.do")
	public String unlink(HttpSession session) throws Exception {
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		String access_Token = "";
		
		map.put("userId", session.getAttribute("userId"));
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("kakaoLogout Controller");
			LOG.debug("session="+session.getAttribute("userId"));
		}
		
		access_Token = adminLoginService.selectKakaoToken(map);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("access_Token="+access_Token);
		}
		
	    kakao.kakaoUnlink(access_Token);
	    session.invalidate();
	    
	    return "redirect:/admin/adminLogin.do";
	}

	@RequestMapping(value="/login/agree.do")
	public String agreement(HttpSession session) throws Exception {
		
		return "CertiNaraManager/agree/agreement";
	}
	
	@RequestMapping(value="/login/duplicateCheck.do")
	public ModelAndView duplicateCheck(HttpSession session, HttpServletRequest request ) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("========== duplicateCheck Controller Start ==========");
			LOG.debug("userName="+request.getParameter("userName"));
		}
		
		String count = adminLoginService.selectNameDuplicateCheck(request.getParameter("nickName"));
		String result = "";
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("count="+count);
		}
		
		if(count == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("success");
			}
			result="success";
		}
		else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("failed");
			}
			result="failed";
		}
		
		mv.addObject("result", result);
		
		return mv;
	}
	
	@RequestMapping(value="/login/agreeCheck.do")
	public String agreementCheck(HttpSession session) throws Exception {
		
		return "redirect:/main.do";
	}
	
//	@GetMapping(value = "/ouath/apple.do")
//	public String appleLoginPage(ModelMap model) {
//
//	  Map<String, String> metaInfo = appleService.getLoginMetaInfo();
//
//	  model.addAttribute("client_id", metaInfo.get("CLIENT_ID"));
//	  model.addAttribute("redirect_uri", metaInfo.get("REDIRECT_URI"));
//	  model.addAttribute("nonce", metaInfo.get("NONCE"));
//
//	  return "index";
//	}
	
}