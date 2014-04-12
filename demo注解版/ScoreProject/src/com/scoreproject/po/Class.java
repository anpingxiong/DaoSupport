package com.scoreproject.po;

import com.daoSupport.annotation.EntityAnnotation;
import com.daoSupport.annotation.PrimaryKeyAnnotation;
import com.daoSupport.annotation.VariableAnnotation;

@EntityAnnotation(table="t_class")
public class Class {
     @PrimaryKeyAnnotation(column="id")
	private int id;
    
 
	private String classNo;
	
 
	public String getClassNo() {
		return classNo;
	}
	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}
 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
