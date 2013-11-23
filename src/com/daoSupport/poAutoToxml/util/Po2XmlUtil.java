package com.daoSupport.poAutoToxml.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

 







import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.bean.BeanDocumentFactory;

import com.daoSupport.annotation.PrimaryKeyAnnotation;
import com.daoSupport.exception.ErrorException;
/**
 * 该类是用来转化的
 */
public class Po2XmlUtil {
	/**
	 * 
	 * anping
	 * TODO 自动的生成Element对象
	 * 由对个po 生成的Element 对象组成。
	 * @param entitys  是实体类包+名
	 * @return  返回的是一个已经成型了的xml element对象
	 * @return_type:Element
	 *
	 */
	public Element   createXmlByPos(Set<String> entitys){
		BeanDocumentFactory factory =new BeanDocumentFactory(); 
		Element rootElement=  factory.createElement("Entitys");
		
		
		return  rootElement;
	}
	/**
	 * 
	 * anping
	 * TODO   将po类解析为一个Element对象
	 * @return 返回的是单个实体类生成的Element对象  
	 * @return_type:Element
	 *   
	 */
	public Element  createElementByPo(String entityName){
	  	Class<?> entity = null; 
		try {
			entity=Class.forName(entityName);
 			Annotation primaryAnnotation = entity.getAnnotation(PrimaryKeyAnnotation.class);
			if(primaryAnnotation==null){
				//如果没有主健的话那就抛出异常,因为默认都是有主健的
				try {
					throw new ErrorException(entityName+"没有主健注解标示,请检查是否有@PrimaryKeyAnnotation标示主健");
				} catch (ErrorException e) {
					 e.printStackTrace();
				}
				
			}else{
				//有主键就开始生成xml
				
			}
			
		} catch (ClassNotFoundException e) {
		 	e.printStackTrace();
		}
		
		 
		return null;
	}
	
	/**
	 * 
	 * anping
	 * TODO 创建一个Elemnet 对象,如果primary key  主键存在 
	 * @return
	 * @return_type:Element
	 *
	 */
	public Element createElementIfPrimaryKeyExists(String entityName){
		
		return null;
	}
	
	/**
	 * 
	 * anping
	 * TODO 创建一个属性 ,用来表示 primary key 在xml 中的配置
	 * @return
	 * @return_type:Attribute
	 *
	 */
	public Attribute createAttributeByPrimary(){
		return  null;
	}
	
	/**
	 * 
	 * anping
	 * TODO  创建一个属性 用来表示一些通用简单的配置 
	 * @return
	 * @return_type:Attribute
	 *
	 */
    public Attribute createAttribute(){
    	return null;
    }
    
    /**
     * 
     * anping
     * TODO 创建一个属性  用来表示外健的配置 
     * @return
     * @return_type:Attribute
     *
     */
    public Attribute  createAttributeByForiegn(){
    	return null;
    }
	
}
