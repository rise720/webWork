package com.cac.webWork.model;

import java.io.Serializable;

/**
 * 
* @ClassName: MyUser
* @Description: 用户模板类
*
* @author: JinWH
* @date: 2019年4月10日 下午7:21:18 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年4月10日     JinWH           v1.0.0               修改原因
 */
public class MyUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	
	
	public MyUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyUser(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MyUser [id=" + id + ", name=" + name + ", getId()=" + getId() + ", getName()=" + getName() + "]";
	}
	
}
