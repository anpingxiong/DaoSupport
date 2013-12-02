package com.daoSupport.test;

 
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.daoSupport.annotation.EntityAnnotation;
import com.daoSupport.annotation.PrimaryKeyAnnotation;
import com.daoSupport.po.School;

public class TestAnnotation {
  public static void main(String args[]) throws NoSuchFieldException, SecurityException{
    School school  = new School();
    
    
    System.out.println(school.getClass().getAnnotation(EntityAnnotation.class));
  
    System.out.println(school.getClass().getAnnotation(PrimaryKeyAnnotation.class));
 
      
    Field fields[]  = school.getClass().getDeclaredFields();
    
    for(Field  field : fields){
    	for(Annotation annotation:field.getAnnotations()){
    		System.out.println(annotation);
    	}
    }
    
  }
}
