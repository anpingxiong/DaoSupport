package com.scoreproject.test;

import org.junit.Test;

import com.daoSupport.dao.EntityDao;
import com.daoSupport.dao.impl.EntityDaoImpl;
import com.daoSupport.exception.DBException;
import com.daoSupport.exception.ErrorException;
import com.scoreproject.po.Student;

public class testA {
	@Test
	public void test() throws DBException, ErrorException{
		EntityDao  dao = EntityDaoImpl.getInstance();
		
		com.scoreproject.po.Class aclass = new com.scoreproject.po.Class ();
		aclass.setId(1);
		aclass.setClassNo("1102");
	
		Student  student  = new Student();
		student.setStudentName("aaaa");
		student.setStudentNo("20111429");
		student.setClasses(aclass);
 		dao.save(student);
		
	}
}
