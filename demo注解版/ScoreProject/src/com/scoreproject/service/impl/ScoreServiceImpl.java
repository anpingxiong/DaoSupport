package com.scoreproject.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daoSupport.dao.impl.EntityDaoImpl;
import com.daoSupport.exception.DBException;
import com.daoSupport.exception.ErrorException;
import com.scoreproject.po.Course;
import com.scoreproject.po.Score;
import com.scoreproject.po.Student;
import com.scoreproject.service.ScoreService;

public class ScoreServiceImpl implements ScoreService {

	private ScoreServiceImpl() {

	}

	private static ScoreServiceImpl data = new ScoreServiceImpl();

	public static ScoreServiceImpl getInstance() {
		if (data == null) {
			synchronized (ScoreServiceImpl.class) {
				return new ScoreServiceImpl();
			}
		} else {
			return data;
		}

	}
	@Override
	public void addScore(int studentId, int courseId, float score) {
		Score score1 = new Score();
		Course course = new Course();
		course.setId(courseId);
		Student student = new Student();
		student.setId(studentId);
		score1.setCourse(course);
		score1.setStudent(student);
		score1.setScore(score);

		try {
			EntityDaoImpl.getInstance().save(score1);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Score> showScore(int studentId, int courseId) {
		List<Score> scores = null;
		List<Object> params = null;
		String a = "student";
		params = new ArrayList<Object>();
		params.add(studentId);
		try {

			if (courseId != -1) {
				a = "course,student";
				params.add(courseId);
			}

			scores = EntityDaoImpl
					.getInstance()
					.findAllEntity(Score.class, 0, 1000, null, a, params, 0)
					==null?null: EntityDaoImpl
							.getInstance()
							.findAllEntity(Score.class, 0, 1000, null, a, params, 0)
							.getResults();
           if(scores!=null){
			for(Score data:scores){
				List<Object>  params2= new ArrayList<Object>();
				params2.add(data.getCourse().getId());
				data.setCourse(EntityDaoImpl.getInstance().findEntity(Course.class, "id", params2));
				params2.clear();
				params2.add(data.getCourse().getId());
				data.setStudent(EntityDaoImpl.getInstance().findEntity(Student.class, "id", params2));
			}
			}
		} catch (ErrorException e) {
			e.printStackTrace();
		} catch (DBException e) {
			e.printStackTrace();
		}
		return scores;

	}
}
