package com.daoSupport.po;

import com.daoSupport.annotation.EntityAnnotation;
import com.daoSupport.annotation.PrimaryKeyAnnotation;

@EntityAnnotation(table="t_student")
public class Student {
	@PrimaryKeyAnnotation(auto_increment=true,column="studentNo",update=false)
	private int id;
	private String name ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
