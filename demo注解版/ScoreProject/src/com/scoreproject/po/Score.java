package com.scoreproject.po;

import com.daoSupport.annotation.EntityAnnotation;
import com.daoSupport.annotation.ForeignKeyAnnotation;
import com.daoSupport.annotation.PrimaryKeyAnnotation;

@EntityAnnotation(table="t_score")
public class Score {
	  @PrimaryKeyAnnotation(column="id")
	private int id;
	
	@ForeignKeyAnnotation(column="cid",type="Integer")
	private Course course;
	@ForeignKeyAnnotation(column="sid",type="Integer")
	private Student student;
	private  float score;
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
