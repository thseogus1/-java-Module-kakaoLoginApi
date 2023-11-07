package com.shcorp.kakao;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
 
@Service
public class kakaoAPI {
	
	Logger LOG = Logger.getLogger(this.getClass());
	
	@Value(value = "#{props['kakao.apiKey']}")
	private String kakaoApiKey;
	
	@Value(value = "#{props['kakao.redirect.url']}")
	private String kakaoRedirectUrl;
	
	@Value(value = "#{props['kakao.adminKey']}")
	private String kakaoAdminKey;
    
	/**
	 * Access ToKen 갖고오기
	 * 
	 * @param authorize_code
	 * @return
	 */
	
    public String getAccessToken (String authorize_code) {
    	LOG.debug("============================== [KakaoAPI.getAccessToken] Start ==============================");
        String access_Token = "";
        String refresh_Token = "";
        String expires_in = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            
            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + kakaoApiKey);
            sb.append("&redirect_uri=" + kakaoRedirectUrl);
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();
            
            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            LOG.debug("[KakaoAPI.getAccessToken] responseCode ={" + responseCode + "}");
 
            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            
            while ((line = br.readLine()) != null) {
                result += line;
            }
            LOG.debug("[KakaoAPI.getAccessToken] responseCode ={" + result + "}");
            
            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
//            expires_in = element.getAsJsonObject().get("expires_in").getAsString();
            
            LOG.debug("[KakaoAPI.getAccessToken] access_token ={" + access_Token +"}");
            LOG.debug("[KakaoAPI.getAccessToken] refresh_token ={" + refresh_Token +"}");
            
//            LOG.debug("expires_in ="+expires_in);
            
            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        LOG.debug("============================== [KakaoAPI.getAccessToken] End ==============================");
        return access_Token;
    }
    
    
    /**
     * 유저 정보 갖고오기
     * 
     * @param access_Token
     * @return
     */
    
    public HashMap<String, Object> getUserInfo (String access_Token) {
    	LOG.debug("============================== [KakaoAPI.getUserInfo] Start ==============================");
        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            
            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            
            int responseCode = conn.getResponseCode();
            LOG.debug("[KakaoAPI.getUserInfo] responseCode ={" + responseCode + "}");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String line = "";
            String result = "";
            
            while ((line = br.readLine()) != null) {
                result += line;
                
            }
            LOG.debug("[KakaoAPI.getUserInfo] response body ={" + result +"}");
            
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            
            JsonElement element1 = (JsonObject)parser.parse(result.toString());
            
            
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> returnMap = mapper.readValue(result, Map.class);
            
            LOG.debug("returnMap:"+ returnMap.get("id"));
            
//            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
//            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            
//            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
//            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            
            userInfo.put("userId", returnMap.get("id"));
//            userInfo.put("nickname", nickname);
//            userInfo.put("email", email);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LOG.debug("============================== [KakaoAPI.getUserInfo] End ==============================");
        return userInfo;
    }
    
    
    /**
     * 카카오 로그아웃
     * @param userId
     */
    
    public void kakaoLogout(String userId,String access_Token) {
    	LOG.debug("============================== [KakaoAPI.kakaoLogout] Start ==============================");
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
        	URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            
            int responseCode = conn.getResponseCode();
            LOG.debug("[KakaoAPI.kakaoLogout] responseCode : {"+ responseCode +"}");

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            LOG.debug("[KakaoAPI.kakaoLogout] responseBody ={"+ result + "}");
            LOG.debug("[KakaoAPI.kakaoLogout] accessToken={" + access_Token + "}");
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LOG.debug("============================== [KakaoAPI.kakaoLogout] End ==============================");
    }
    
    
    public void kakaoUnlink(String access_Token) {
        String reqURL = "https://kapi.kakao.com/v1/user/unlink";
        try {
        	URL url = new URL(reqURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            LOG.debug("conn="+conn);
            LOG.debug("accessToken="+access_Token);
            
            int responseCode = conn.getResponseCode();
            LOG.debug("[KakaoAPI.kakaoUnlink] responseCode : {"+ responseCode +"}");
            
            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
           
            String result = "";
            String line = "";
            
            while ((line = br.readLine()) != null) {
                result += line;
            }
            LOG.debug(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
}