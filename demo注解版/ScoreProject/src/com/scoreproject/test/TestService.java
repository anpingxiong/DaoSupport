package com.scoreproject.test;

import java.util.List;

import org.junit.Test;

import com.scoreproject.po.Score;
import com.scoreproject.po.Student;
import com.scoreproject.service.impl.ClassServiceImpl;
import com.scoreproject.service.impl.ScoreServiceImpl;
import com.scoreproject.service.impl.StudentServiceImpl;

public class TestService {
    @Test
	public void testClassService(){
		ClassServiceImpl classService = ClassServiceImpl.getInstance();
		 List<com.scoreproject.po.Class> classs = classService.showClass();
		 for(com.scoreproject.po.Class data:classs){
			 System.out.println(data.getId()+"--"+data.getClassNo());
		 }
	}
    
    
    @Test
    public void testStudentService(){
    	StudentServiceImpl studentService = StudentServiceImpl.getInstance();
    	List<Student> students  =  studentService.showStudent(1);
    	for(Student student:students){
    		System.out.println(student.getStudentName());
    	}
    }
    
    @Test
    public void testScoreService(){
    	ScoreServiceImpl  scoreImpl  = ScoreServiceImpl.getInstance();
    	List<Score>  datas =scoreImpl.showScore(1, -1);
    	for(Score data:datas){
    		System.out.println(data.getId()+"--"+data.getScore()+"--"+data.getCourse().getName());
    	}
    	
    }
    
    
    
    
}
