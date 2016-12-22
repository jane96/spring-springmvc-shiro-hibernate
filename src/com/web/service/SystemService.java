package com.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.dao.UserDao;
import com.web.entity.User;

@Service("systemService")
@Transactional(readOnly = false)
public class SystemService {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final String HASH_MD5 = "MD5";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	@Autowired
	UserDao userDao;
	public void save(User user){
		userDao.save(user);
	}
	
	public User getUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}
}
