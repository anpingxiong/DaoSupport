package com.daoSupport.po;

import java.util.Date;

import com.daoSupport.annotation.EntityAnnotation;
import com.daoSupport.annotation.ForeignKeyAnnotation;
import com.daoSupport.annotation.PrimaryKeyAnnotation;
import com.sun.tracing.dtrace.Attributes;
@EntityAnnotation(table="t_work")
public class Work {
	@PrimaryKeyAnnotation(column="id")
	private int id;
	private String workName;
	@ForeignKeyAnnotation(column="sid",type="Integer")
	private School  school;
	 
	private Date date ;
	
 
	public int getId() {
		return id;
	}
	 
	public void setId(int id) {
		this.id = id;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
