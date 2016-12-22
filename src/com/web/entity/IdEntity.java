package com.web.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;



/**
 * 数据Entity类
 * @author hz
 * @version 2016-12-27
 */
@MappedSuperclass
public abstract class IdEntity<T> extends DataEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String id;		// 编号
	
	public IdEntity() {
		super();
	}
	
	@PrePersist//在更新之前调用
	public void prePersist(){
		super.prePersist();
		this.id = IdGen.uuid();
	}

	@Id
	//@Length(min=1, max=64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
