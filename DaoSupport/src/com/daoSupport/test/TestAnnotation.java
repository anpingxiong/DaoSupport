package com.daoSupport.test;

 
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import com.daoSupport.Log.DaoSupportLog;
import com.daoSupport.annotation.EntityAnnotation;
import com.daoSupport.annotation.PrimaryKeyAnnotation;
import com.daoSupport.annotationHelper.AnnotationContentFetchHelper;
import com.daoSupport.po.School;

public class TestAnnotation {
  public static void main(String args[]) throws NoSuchFieldException, SecurityException{
    School school  = new School();
    
    
    System.out.println(school.getClass().getAnnotation(EntityAnnotation.class));
  
    System.out.println(school.getClass().getAnnotation(PrimaryKeyAnnotation.class));
 
      
    Field fields[]  = school.getClass().getDeclaredFields();
    //拿到的这个注解的string 我们就可以拿到相应的信息了
    //@com.daoSupport.annotation.PrimaryKeyAnnotation(update=false, auto_increment=true, column=id)
    for(Field  field : fields){
    	for(Annotation annotation:field.getAnnotations()){
    		 AnnotationContentFetchHelper   helper  =new AnnotationContentFetchHelper();
             Map<String,String> content   =   helper.getAnnotationContent(annotation)   ; 	  
    	     DaoSupportLog.getLogger().info(content.toString());
    	}
    }
    
  }
}
