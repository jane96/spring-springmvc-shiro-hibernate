package com.web.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.common.collect.Maps;
import com.web.dao.UserDao;
import com.web.entity.User;
import com.web.security.CaptchaException;
import com.web.security.SystemAuthorizingRealm.Principal;



public class UserUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	

	public static final String CACHE_USER = "user";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	
	public static User getUser(){
		User user = (User)getCache(CACHE_USER);
		if (user == null){
			try{
				Subject subject = SecurityUtils.getSubject();
				System.out.println("subject==null:" +(subject==null));
				Principal principal = (Principal)subject.getPrincipal();
				System.out.println("principal==null:" + (principal==null));
				if (principal!=null){
					System.out.println("Principal不为空");
					user = userDao.get(principal.getId());
	//					Hibernate.initialize(user.getRoleList());
					putCache(CACHE_USER, user);
				}
			}catch (UnavailableSecurityManagerException e) {
				throw new  CaptchaException("UnavailableSecurityManagerException");
			}catch (InvalidSessionException e){
				throw new  CaptchaException("InvalidSessionException");
				
			}
		}
		if (user == null){
			user = new User();
			try{
				SecurityUtils.getSubject().logout();
			}catch (UnavailableSecurityManagerException e) {
				
			}catch (InvalidSessionException e){
				
			}
		}
		return user;
	}
	
	
	public static User getUser(boolean isRefresh){
		if (isRefresh){
			removeCache(CACHE_USER);
		}
		return getUser();
	}

	/*public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> list = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (list == null || list.size() <= 0){
			User user = getUser();
			DetachedCriteria dc = roleDao.createDetachedCriteria();
			dc.createAlias("office", "office");
//			dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
//			dc.add(dataScopeFilter(user, "office", "userList"));
//			dc.createAlias("createBy","createBy");
//			dc.add(Restrictions.eq("createBy.id",user.getId()));
			dc.add(dataScopeFilter(user, "office", "createBy"));
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
//			dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
			list = roleDao.find(dc);
			putCache(CACHE_ROLE_LIST, list);
		}
		return list;
	}*/
	
	/**
	 * 超级管理员根据机构获取角色
	 * @return
	 */
	/*public static List<Role> getRoleListByOrgName(String orgId){
		@SuppressWarnings("unchecked")
		List<Role> list = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (list == null || list.size() <= 0){
			User user = getUser();
			DetachedCriteria dc = roleDao.createDetachedCriteria();
			dc.createAlias("office", "office");
//			dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
//			dc.add(dataScopeFilter(user, "office", "userList"));
//			dc.createAlias("createBy","createBy");
//			dc.add(Restrictions.eq("createBy.id",user.getId()));
			dc.add(dataScopeFilter(user, "office", "createBy"));
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
//			dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
			list = roleDao.find(dc);
			putCache(CACHE_ROLE_LIST, list);
		}
		return list;
	}
	*/
	/*public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao.findAllList();
			}else{
				menuList = menuDao.findByUserId(user.getId());
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}*/
	
	
	
	
	
	public static User getUserById(String id){
		if(StringUtils.isNotBlank(id)) {
			return userDao.get(id);
		} else {
			return null;
		}
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}
	
	public static Map<String, Object> getCacheMap(){
		Map<String, Object> map = Maps.newHashMap();
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			return principal!=null?principal.getCacheMap():map;
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return map;
	}
	
	
	/**
	 * 根据用户编号获取电话
	 */
	public static String getPhoneByUserId(String userIds){
		String mobile = "";
		String[] userArray = userIds.split(",");
		for (String str : userArray) {
			User user = userDao.get(str);
			String mobileStr = user.getPhone();
			mobile = mobile+mobileStr+",";
		}
		String mobiles = mobile.substring(0,mobile.length()-1);
		return mobiles;
	}
	
}
