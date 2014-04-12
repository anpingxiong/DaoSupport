package com.daoSupport.vo;

import java.lang.annotation.Annotation;
/**
 * 实例变量容器主要是将实体类的变量信息和注解信息绑定在一起
 * @author anping
 *
 */
public class EntityVaribleContainer {
	private String  variableName;
	private String  variableType;
	private String  variableRefClass;
	private Annotation annotation;//这是在实体类上面的注解
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getVariableType() {
		return variableType;
	}
	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}
	public String getVariableRefClass() {
		return variableRefClass;
	}
	public void setVariableRefClass(String variableRefClass) {
		this.variableRefClass = variableRefClass;
	}
	public Annotation getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}
	
}
