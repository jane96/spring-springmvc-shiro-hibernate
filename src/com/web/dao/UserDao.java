package com.web.dao;

import org.springframework.stereotype.Repository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.User;


@Repository("userDao")
public class UserDao extends baseDao<User> {
	public User findByLoginName(String loginName){
		System.out.println("执行查询loginName");
		return getByHql("from User where loginName = '" + loginName + "'");
	}
}
