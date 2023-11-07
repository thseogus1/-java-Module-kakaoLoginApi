<%--
  Class Name : adminLogin.jsp
  Description : 로그인
  				카카오 / apple 로그인이 있으며,
  				카카오 : 카카오 API Key로 initial 한 뒤 return url(redirectUrl)을 던지면, 해당 return url을 카카오에서 호출해 준다.
  Modification Information

      수정일        수정자        수정내용
    ----------    --------    ---------------------------
    2023.10.15    CSH         최초작성

    author   : SHC
    since    : 2023.10.15
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="content-language" content="ko">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 shrink-to-fit=no">

<title>인증나라</title>


<!-- ===============================================-->
<!--    Stylesheets-->
<!-- ===============================================-->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin="">
<link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@300;400;600;700;800;900&amp;display=swap" rel="stylesheet">
<link href="/vendors/simplebar/simplebar.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://unicons.iconscout.com/release/v4.0.8/css/line.css">
<link href="/assets/css/theme-rtl.min.css" type="text/css" rel="stylesheet" id="style-rtl">
<link href="/assets/css/theme.min.css" type="text/css" rel="stylesheet" id="style-default">
<link href="/assets/css/user-rtl.min.css" type="text/css" rel="stylesheet" id="user-style-rtl">
<link href="/assets/css/user.min.css" type="text/css" rel="stylesheet" id="user-style-default">

</head>

<script type="text/javascript">

// kakao login
function loginWithKakao(retUrl) {
    Kakao.Auth.authorize({
		redirectUri: retUrl
    });
}

//   function getCookie(name) {
//     var parts = document.cookie.split(name + '=');
//     if (parts.length === 2) { return parts[1].split(';')[0]; }
//   }
 
// 이메일 형식 확인 정규식
function email_check( email ) {    
    var regex=/([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    return (email != '' && email != 'undefined' && regex.test(email)); 
}

</script>

</head>

<body class="login">

<div class="wrapper wrapper-login">	
	<main class="main" id="top">
		<div class="container">
			<div class="row flex-center min-vh-100 py-5">
				<div class="col-sm-10 col-md-8 col-lg-5 col-xl-5 col-xxl-3">
					<a class="d-flex flex-center text-decoration-none mb-4" href="/main.do">
						<div class="d-flex align-items-center fw-bolder fs-5 d-inline-block">
							<img src="/images/certinara_logo.png" alt="phoenix" width="58" />
						</div>
					</a>
					<div class="text-center mb-7">
						<h3 class="text-1000">Sign In</h3>
						<p class="text-700">Get access to your account</p>
					</div>
					<button class="btn btn-phoenix-secondary w-100 mb-3" onclick="javascript:loginWithKakao('${kakaoRedirectUrl}');">
						<img src="/images/kakao_login_medium_narrow.png" alt="카카오 로그인 버튼" />
					</button>
					<button class="btn btn-phoenix-secondary w-100">
						<img src="/images/apple_login.png" alt="애플 로그인 버튼" />
					</button>
				</div>
				
			</div>
		</div>
	</main>

	<script src="https://t1.kakaocdn.net/kakao_js_sdk/2.4.0/kakao.min.js" integrity="sha384-mXVrIX2T/Kszp6Z0aEWaA8Nm7J6/ZeWXbL8UpGRjKwWe56Srd/iyNmWMBhcItAjH" crossorigin="anonymous"></script>
	<script>Kakao.init('${kakaoApiKey}'); </script> <!-- 사용하려는 앱의 REST 키 입력 --> 	 <!-- 내 계정 apikey 144817b09a3681399b509d8462b37a53 -->
	
	<script type="text/javascript" src="https://appleid.cdn-apple.com/appleauth/static/jsapi/appleid/1/en_US/appleid.auth.js"></script>
</body>
</html>
