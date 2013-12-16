package com.daoSupport.annotationHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.daoSupport.Log.DaoSupportLog;
import com.daoSupport.annotation.EntityAnnotation;
import com.daoSupport.annotation.ForeignKeyAnnotation;
import com.daoSupport.annotation.PrimaryKeyAnnotation;
import com.daoSupport.annotation.VariableAnnotation;
import com.daoSupport.vo.EntityVaribleContainer;

/**
 * 
 * @author anping
 *  这是拿到注解信息的方法
 *  我们１。可以拿到主键上的注解
 *  ２。可以拿到属性上的注解
 *  ３。可以拿到外键上的注解
 *  ５。可以拿到实体类上的注解
 */
public class AnnotationHelper {
     /**
      *     
      * @param className 需要去扫描的类
      * @return　返回的是实体类主键变量的信息和注解
      * @throws ClassNotFoundException
      */
	public EntityVaribleContainer  getPrimayKeyAnnotation(String className) throws ClassNotFoundException{
	    EntityVaribleContainer  container  =  null;
	    Field[] fields = Class.forName(className).getDeclaredFields();
	    boolean  flag   = false;
	    for(Field field:fields){
	    	Annotation annotationOnFiels[]  =  field.getAnnotations();
	    	for(Annotation annotationOnField:annotationOnFiels){
	    		if(annotationOnField instanceof PrimaryKeyAnnotation){
	    			container= new EntityVaribleContainer();
	    			container.setAnnotation(annotationOnField);
	    			container.setVariableName(field.getName());
	    			String type   = field.getType().getSimpleName().toLowerCase();
	                 if(!type.equals("int")){
	    			    String  a  =  type.substring(1);
	    			    String b   =  type.substring(0, 1).toUpperCase();
	    			    type=b+a;
	    			    }
	                 else{
	    			    type="Integer";	
	    			    }
	    			container.setVariableType(type);
	    			flag = true;
	    			break;
	    		}
	    	}
	    	if(flag ==true){
	    		break;
	    	}
	    }
		
		return container;	
	}
	/**
	 * 
	 * @param className
	 * @return　返回的是实体类主键变量的信息和注解 [] size 0;
	 * @throws ClassNotFoundException 
	 */
	public List<EntityVaribleContainer>  getForeignKeyAnnotation(String className) throws ClassNotFoundException{
        List<EntityVaribleContainer> container  =new ArrayList<EntityVaribleContainer>(4);  		
	   
		Field[] fields= Class.forName(className).getDeclaredFields();
		for(Field field:fields){
			Annotation[]  fieldsAnnotations = field.getAnnotations();
			for(Annotation annotation:fieldsAnnotations){
				if(annotation instanceof ForeignKeyAnnotation){
					EntityVaribleContainer   entityVariable= new EntityVaribleContainer();
					entityVariable.setAnnotation(annotation);
					entityVariable.setVariableName(field.getName());
	    			entityVariable.setVariableRefClass(field.getType().getName()); 
                    container.add(entityVariable);
				}
			}
		}
		
		return container;
	}
	/**
	 * 
	 * @param className
	 * @return 返回的是实体类主键变量的信息和注解  size 0
	 * @throws ClassNotFoundException 
	 * 我没有考虑既是主键有事外键的问题啊～～～
	 */
	public List<EntityVaribleContainer> getOnVaribleAnnotation(String className) throws ClassNotFoundException{

	    List<EntityVaribleContainer> entityVaribleContainer  = new ArrayList<EntityVaribleContainer>(4);
		 
		Field[] fields= Class.forName(className).getDeclaredFields();
		for(Field field:fields){
            boolean  isPromaryOrForeign = false;
			Annotation  variableAnnotation   = null;
			Annotation[]  fieldsAnnotations = field.getAnnotations();
			for(Annotation annotation:fieldsAnnotations){
				if((annotation instanceof PrimaryKeyAnnotation) || (annotation instanceof ForeignKeyAnnotation)){
					isPromaryOrForeign=true;
					break;
				}else if(annotation instanceof VariableAnnotation){
					variableAnnotation= annotation;
					break;
				}
			}
			
			
			if(!isPromaryOrForeign){
				EntityVaribleContainer    entityVarible  = new EntityVaribleContainer();
				entityVarible.setAnnotation(variableAnnotation);
				entityVarible.setVariableName(field.getName());
				String type   = field.getType().getSimpleName().toLowerCase();
                if(!type.equals("int")){
   			    String  a  =  type.substring(1);
   			    String b   =  type.substring(0, 1).toUpperCase();
   			    type=b+a;
   			    }
                else{
   			    type="Integer";	
   			    }
   			  entityVarible.setVariableType(type);
   			entityVaribleContainer.add(entityVarible);
            }
		}
		 
		return entityVaribleContainer;
	}
	/**
	 * 
	 * @param className
	 * @return 返回的是类上面的注解
	 * @throws ClassNotFoundException
	 */
	public Annotation  getEntityAnnotation(String className) throws ClassNotFoundException{
		return Class.forName(className).getAnnotation(EntityAnnotation.class);
	}
}
