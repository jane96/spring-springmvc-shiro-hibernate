<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<!-- 登录错误 -->
<%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
<body >
	请你登录！
	<label><%=error==null?"":"com.web.security.CaptchaException".equals(error)?"验证码错误，请重试！":"密码或者用户名错误，请重新输入！" %></label>
	<form action="login" method="post" id="loginForm">
		<input type="text" name="username" required/>
		<input type="password" name="password" required/>
		<input type="submit" value="提交"/>
		<input type="text"  name="validateCode"  id="validateCode" class="form-control" placeholder="验证码"  required/>
		<img id="validateImg" src="http://localhost:8080//tdata/ValidateCodeServlet" title="换一张" onClick="this.src='http://localhost:8080//tdata/ValidateCodeServlet?'+ new Date().getTime();"/>
	</form>
</body>
</html>