package com.daoSupport.po;

import java.util.Date;

public class Work {
	private int id;
	private String workName;
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
