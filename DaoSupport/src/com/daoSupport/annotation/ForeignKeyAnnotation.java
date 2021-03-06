package com.daoSupport.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 外健类注解，用来表示  类属性是外健对象
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKeyAnnotation {
	public String column()default"";
 
  	public String type()default"";
	
  	public String key()default"foreign";
	
   
}
