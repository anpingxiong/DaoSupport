package com.daoSupport.po;

import com.daoSupport.annotation.EntityAnnotation;
import com.daoSupport.annotation.PrimaryKeyAnnotation;

@EntityAnnotation(table="t_school")
public class School {
	@PrimaryKeyAnnotation(auto_increment=true,column="id",update=false)
	private int id;
	private String schoolName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
}
