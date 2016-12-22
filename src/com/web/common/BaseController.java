package com.web.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.web.mapper.JsonMapper;



public class BaseController {
	private static String SAVED_REQUEST_KEY = "shiroSavedRequest";
	protected void sendObjectToJson(Object object, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		String message = JsonMapper.getInstance().toJson(object);
		pw.write(message);
		pw.flush();
		pw.close();
	}
	//异常处理
	@ExceptionHandler(RuntimeException.class)
	public void handleException(Exception ex,HttpServletRequest request,HttpServletResponse response) throws IOException{
		System.out.println("抛异常了！"+ex.getLocalizedMessage());
	   response.getWriter().printf(ex.getMessage());
	    response.flushBuffer();
	}
	
}
