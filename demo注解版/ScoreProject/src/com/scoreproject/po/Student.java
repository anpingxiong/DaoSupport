package com.scoreproject.po;

import com.daoSupport.annotation.EntityAnnotation;
import com.daoSupport.annotation.ForeignKeyAnnotation;
import com.daoSupport.annotation.PrimaryKeyAnnotation;

@EntityAnnotation(table="t_student")
public class Student {
	  @PrimaryKeyAnnotation(column="id")
	private int id ;
	private String studentNo;
	private String studentName;
	@ForeignKeyAnnotation(column="cid",type="Integer")
	private Class classes;
	
	
	
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public Class getClasses() {
		return classes;
	}
	public void setClasses(Class classes) {
		this.classes = classes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
