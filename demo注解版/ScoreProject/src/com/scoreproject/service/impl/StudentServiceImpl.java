package com.scoreproject.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.daoSupport.dao.impl.EntityDaoImpl;
import com.daoSupport.exception.DBException;
import com.daoSupport.exception.ErrorException;
import com.scoreproject.po.*;
import com.scoreproject.po.Class;
import com.scoreproject.service.StudentService;

public class StudentServiceImpl implements StudentService{

	private StudentServiceImpl() {

	}

	private static StudentServiceImpl data = new StudentServiceImpl();

	public static StudentServiceImpl getInstance() {
		if (data == null) {
			synchronized (StudentServiceImpl.class) {
				return new StudentServiceImpl();
			}
		} else {
			return data;
		}

	}
	@Override
	public void addStudent(String studentNo, String studentName, int classId) {
			Student student  = new Student();
			student.setStudentName(studentName);
			student.setStudentNo(studentNo);
			Class classx   = new Class();
			classx.setId(classId);
			student.setClasses(classx);
			try {
				EntityDaoImpl.getInstance().save(student);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	@Override
	public List<Student> showStudent(int classId) {
		List<Student> students = null;
		List<Object> params = new ArrayList<Object>(1);
		params.add(classId);
	    try {
	    	students=EntityDaoImpl.getInstance().findAllEntity(Student.class,0,1000,null,"classes",params,0)==null?null:EntityDaoImpl.getInstance().findAllEntity(Student.class,0,1000,null,"classes",params,0).getResults();
		} catch (ErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return students;
	}

}
