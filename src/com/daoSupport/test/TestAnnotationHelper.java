package com.daoSupport.test;

import java.util.List;

import org.junit.Test;

import com.daoSupport.annotationHelper.AnnotationHelper;
import com.daoSupport.vo.EntityVaribleContainer;

public class TestAnnotationHelper {
	@Test
	public void testGetPrimaryAnnotation() throws ClassNotFoundException{
		AnnotationHelper helper  = new AnnotationHelper();
		System.out.println(helper.getEntityAnnotation("com.daoSupport.po.School"));
	}
	@Test
	public void testGetEntityAnnotation() throws ClassNotFoundException{
		AnnotationHelper helper  = new AnnotationHelper();
		EntityVaribleContainer container = helper.getPrimayKeyAnnotation("com.daoSupport.po.Teacher"); 
	    System.out.println(container.getVariableName()+"---"+container.getVariableType()+"---"+container.getAnnotation());
	}
	@Test
	public void testGetVariableAnnotation() throws ClassNotFoundException{
		AnnotationHelper helper  = new AnnotationHelper();
		List<EntityVaribleContainer> container =   helper.getOnVaribleAnnotation("com.daoSupport.po.Student");
	    for(EntityVaribleContainer variable :container){
	    	System.out.println(variable.getVariableName()+"---"+variable.getVariableType()+"---"+variable.getVariableRefClass()+"---"+variable.getAnnotation());
	    }
	
	}
	@Test
	public void testGetForignAnnotation() throws ClassNotFoundException{
		AnnotationHelper helper  = new AnnotationHelper();	
		List<EntityVaribleContainer> container =   helper.getForeignKeyAnnotation("com.daoSupport.po.Work");
	    for(EntityVaribleContainer variable :container){
	    	System.out.println(variable.getVariableName()+"---"+variable.getVariableType()+"---"+variable.getVariableRefClass()+"---"+variable.getAnnotation());
	    }
	}
}
