package com.daoSupport.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 主建注解  用来表示某一个属性属于主健
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKeyAnnotation {
	public String column()default"";
    
	public boolean auto_increment()default true;
	
	public boolean update()default false;
 
	public String key()default"primary";
	
}
