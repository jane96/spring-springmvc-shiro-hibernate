package com.web.controller;

import java.io.IOException;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.common.collect.Maps;

import com.web.common.BaseController;
import com.web.common.CacheUtils;
import com.web.common.Md5Util;
import com.web.common.UserUtils;
import com.web.common.ValidateCodeServlet;
import com.web.entity.User;
import com.web.mapper.JsonMapper;
import com.web.service.SystemService;
import com.web.test.Test; 
@Controller

public class userController extends BaseController {
	@Autowired
	SystemService systemService;

	/**
	 * 管理登录
	 */
	@RequestMapping("save")
	public String save(){
		User user = new User("王丽", "test", "1", "1783476454", 23);
		//将密码转为md5
		user.setPassword(Md5Util.md5Encoder(user.getPassword()));
		systemService.save(user);
		System.out.println("执行保存操作");
		return "index.html";
	}
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String loginGet(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入get页面");
		User user = UserUtils.getUser();
		/*String url = WebUtils.getSavedRequest(request).getRequestUrl();*/
		//System.out.println(url);
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			return "redirect:"+"/" ;
		}
		return "/html/sysLogin.jsp";
	}
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginPost(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入postLogin页面");
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			return "redirect:"+"/" ;
		}
		return "/html/sysLogin.jsp";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "loginSuccess", method = RequestMethod.GET)
	public String login(@RequestParam(value=FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,required=false) String username, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		System.out.println("进入post页面");
		System.out.println(username);
		User user = UserUtils.getUser();
		System.out.println(user.getId());
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			try{
				String url = WebUtils.getSavedRequest(request).getRequestUrl();
				System.out.println(url);
				return "redirect:"+"/" + url;
				}catch(Exception e){
					return "/html/index.html";
				}

			
		}
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		return "/html/sysLogin.jsp";
	}
	@RequestMapping(value="/")
	public String login(){
		System.out.println("login");
		return "/html/sysLogin.jsp";
	}
	@RequiresPermissions("sys:eidt")
	@RequestMapping(value="list")
	public String test(HttpServletResponse response) throws IOException{
		System.out.println("执行方法");
		Map m1 = new HashMap();
		Map m2 = new HashMap();
		Map m3 = new HashMap();
		Map m4 = new HashMap();
		m3.put("name","公司领导");
		m4.put("id", 2);
		List list = new ArrayList();
		list.add(m3);
		list.add(m4);
		m1.put("department", list);
		this.sendObjectToJson(m1, response);
		return null;
	}
	@RequestMapping("error")
	public String error(){
		System.out.println("没有相应的权限");
		return "/html/error.html";
	}
	@RequestMapping("ValidateCodeServlet")
	public void validateLogin(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		System.out.println("进入ValidateCodeServlet Controller");
		new ValidateCodeServlet().doGet(request, response);
	}
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();//类似于Map
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		
		return loginFailNum < 100;//启用验证码
	}

}
