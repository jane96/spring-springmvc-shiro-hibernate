package com.web.dao;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.common.Reflections;
import com.web.entity.User;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class baseDao<T>{
	@Autowired
	private SessionFactory sessionFactory;
	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<?> entityClass;
	
	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public baseDao() {
		entityClass = Reflections.getClassGenricType(getClass());
	}

	public Session getSession(){
		try{
			if(sessionFactory == null)
				System.out.println("未注入1");
			else
				System.out.println("sessionFactory不为空");
		}catch(Exception e){
			e.printStackTrace();
		}
		return sessionFactory.getCurrentSession();
	}
	
	public void save(T entity){
		try {
			// 获取实体编号
			Object id = null;
			for (Method method : entity.getClass().getMethods()){
				Id idAnn = method.getAnnotation(Id.class);
				if (idAnn != null){
					id = method.invoke(entity);
					break;
				}
			}
			// 插入前执行方法
			if (null == (String)id){
				for (Method method : entity.getClass().getMethods()){
					PrePersist pp = method.getAnnotation(PrePersist.class);
					if (pp != null){
						method.invoke(entity);
						break;
					}
				}
			}
			// 更新前执行方法
			else{
				for (Method method : entity.getClass().getMethods()){
					PreUpdate pu = method.getAnnotation(PreUpdate.class);
					if (pu != null){
						method.invoke(entity);
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		getSession().saveOrUpdate(entity);
		
	}
	@SuppressWarnings("unchecked")
	public T getByHql(String qlString){
		Query query = createQuery(qlString);
		return (T)query.uniqueResult();
	}
	public Query createQuery(String qlString){
		System.out.println("进入createQuery方法1！");
		Query query = getSession().createQuery(qlString);
		return query;
	}
	@SuppressWarnings("unchecked")
	public T get(Serializable id){
		return (T)getSession().get(entityClass, id);
	}
	
}
