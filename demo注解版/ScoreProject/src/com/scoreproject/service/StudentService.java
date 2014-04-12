package com.scoreproject.service;

import java.util.List;

import com.scoreproject.po.Student;

public interface StudentService {
	public void addStudent(String studentNo,String studentName,int classId);
	public List<Student> showStudent(int classId); 
}
