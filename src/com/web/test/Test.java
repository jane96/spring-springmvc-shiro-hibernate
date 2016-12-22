package com.web.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.dao.baseDao;
import com.web.dao.UserDao;
import com.web.entity.User;


public class Test {
	@Autowired
	private UserDao userDao ;
	public void login(){
		User u = new User();
		u.setName("黄振");
		u.setAge(25);
		userDao.save(u);
	}
	
}
