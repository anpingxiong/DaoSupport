package com.scoreproject.service.impl;

import java.util.List;

import com.daoSupport.dao.impl.EntityDaoImpl;
import com.daoSupport.exception.DBException;
import com.daoSupport.exception.ErrorException;
import com.scoreproject.po.Course;
import com.scoreproject.service.CourseService;

public class CourseServiceImpl implements CourseService{
	private CourseServiceImpl() {

	}

	private static CourseServiceImpl data = new CourseServiceImpl();

	public static CourseServiceImpl getInstance() {
		if (data == null) {
			synchronized (CourseServiceImpl.class) {
				return new CourseServiceImpl();
			}
		} else {
			return data;
		}

	}

	@Override
	public List<Course> showCourse()  {
		List<Course> result  = null;
		try {
			result= (List<Course>) EntityDaoImpl.getInstance().findAllEntity(Course.class, 0,1000,null,null, null, 0).getResults();
		} catch (ErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
