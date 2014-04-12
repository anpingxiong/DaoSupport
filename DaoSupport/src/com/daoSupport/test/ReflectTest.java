package com.daoSupport.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.daoSupport.po.School;

public class ReflectTest {
	@Test
	public void testReflect() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		
		School  school  = School.class.newInstance();
		Method method = school.getClass().getMethod("setId", int.class);
        //method.setAccessible(true);
        System.out.println(method.isAccessible());
		method.invoke(school, 11);
		
	//	Field field =school.getClass().getDeclaredField("id");
//		field.setAccessible(true);
	//	field.set(school, 1);
        System.out.println(school.getId());
	
	}
	
}
