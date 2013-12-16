package com.daoSupport.poAutoToxml.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.daoSupport.Log.DaoSupportLog;
import com.daoSupport.annotation.PrimaryKeyAnnotation;
import com.daoSupport.annotationHelper.AnnotationContentFetchHelper;
import com.daoSupport.annotationHelper.AnnotationHelper;
import com.daoSupport.exception.ErrorException;
import com.daoSupport.vo.EntityVaribleContainer;
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
	 * @throws ErrorException 
	 * @throws ClassNotFoundException 
	 * @return_type:Element
	 *
	 */
	public Element   createXmlByPos(Set<String> entitys) throws ClassNotFoundException, ErrorException{
		DocumentFactory factory =new DocumentFactory(); 
		Element rootElement=  factory.createElement("Entitys");
		for(String entityName :entitys){
		   Element  entity  =	this.createElementByPo(factory, entityName);
		 rootElement.add(entity);
		 
		} 
		 return  rootElement;
	}
	/**
	 * 
	 * anping
	 * TODO   将po类解析为一个Element对象
	 * @return 返回的是单个实体类生成的Element对象  
	 * @throws ClassNotFoundException 
	 * @throws ErrorException 
	 * @return_type:Element
	 *生成的策略如下：
	 *    首先判断是不是拥有主键，如果没有主键的话,那则抛出异常。并且停止继续生成。
	 *    如果检测有主键，那么现生产出之间这个elments   格式如下：		<property name="id" column="id" key="primary" type="Integer"
	*		auto_increment="true" update="false" />
	*	
	*	 然后去产生外键，把外键产生好，但是发现外键指向的类是未知的或者是错误的话，立刻停止，并且
	*	 报错。
	*	 最好产生那些平常的属性。
	*	 完毕
	*	 
	*  遇到自己不曾想过的问题是：我把属性的type 规定死了。  如果有math 等或者其它的type 来代替基本数据类型 怎么办？？	 
	*	 
	*	 	 
	 */
	

	public Element  createElementByPo(DocumentFactory documentFactory,String entityName) throws ClassNotFoundException, ErrorException{
	  	Class<?> entity = null; 
	 		entity=Class.forName(entityName);
 			Field[] fields = entity.getDeclaredFields();
			boolean  flag = false;
 			for(Field field:fields){
 				Annotation primaryAnnotation = field.getAnnotation(PrimaryKeyAnnotation.class);
 	 			
 				if(primaryAnnotation!=null){
 					flag=true;
				}
 			}
 			if(flag ==false){
 				//如果没有主健的话那就抛出异常,因为默认都是有主健的
			  	throw new ErrorException(entityName+"没有主健注解标示,请检查是否有@PrimaryKeyAnnotation标示主健");
		  
 			}
 			 	//有主键就开始生成xml
		 return  	this.createElementIfPrimaryKeyExists(documentFactory, entityName);
			 
 			
	}
	
	/**
	 * 
	 * anping
	 * TODO 创建一个Elemnet 对象,如果primary key  主键存在 
	 * @return
	 * @throws ClassNotFoundException 
	 * @return_type:Element  如果没有配置的话，则返回的是null;
	 *  先抛弃上面的问题不去搭理,那我们现在的工做是创建一个叫 entity 的 element 
	 *  如下：<Entity className="com.daoSupport.po.Work" table="t_work"></Entity>
	 *  主要需要添加的两个属性是className  和 table 
	 */
	public Element createElementIfPrimaryKeyExists(DocumentFactory documentFactory,String entityName) throws ClassNotFoundException{
		 Element  entity  = documentFactory.createElement("Entity");
		 AnnotationHelper annotationHelper   = new  AnnotationHelper();
		 Annotation annotation = annotationHelper.getEntityAnnotation(entityName);
		 AnnotationContentFetchHelper  fetchHelper  = new AnnotationContentFetchHelper();
		 
		 //这里是添加Entity　这个element
		 Map<String,String> entityConfigValue  =  fetchHelper.getAnnotationContent(annotation);
		 if(entityConfigValue.get("table")==null){
		     DaoSupportLog.getLogger().info(entityName+"类没有在注解上配置table,请在类上的@EntityAnnotation加上上对table的配置");
			 return null;
		 }
		 
		 
		 entity.add(documentFactory.createAttribute(entity, "className",entityName));
		 
		  
		 entity.add(documentFactory.createAttribute(entity, "table", entityConfigValue.get("table")));
		  //添加Entity结束
		 
		 
		 
		  
		 //主键开始
		 
		 this.createElementsForPrimary(documentFactory, fetchHelper, annotationHelper, entity, entityName);
		 //主键添加结束
		 
		 //添加外键
		 this.createElementsForForeign(documentFactory, fetchHelper, annotationHelper, entity, entityName);
		 //添加外键结束
		 return entity;
	}
	
	/**
	 * 
	 * anping
	 * TODO 创建一个属性 ,用来表示 primary key 在xml 中的配置
	 * @return
	 * @throws ClassNotFoundException 
	 * @return_type:Element
	 *  需要通过发射机制去拿到属性的类型和name
	 */
	public void createElementsForPrimary(DocumentFactory documentFactory,AnnotationContentFetchHelper  fetchHelper,
			AnnotationHelper annotationHelper,Element entity,String entityName) throws ClassNotFoundException{
	        EntityVaribleContainer container  =  annotationHelper.getPrimayKeyAnnotation(entityName);
	        Element idElement =  documentFactory.createElement("property");
	        this.addArrtibuteForElement(documentFactory, idElement, container, fetchHelper);
	        entity.add(idElement);
	}
	
	/**
	 * 
	 * anping
	 * TODO  创建一个属性 用来表示一些通用简单的配置 
	 * @return
	 * @return_type:Element
	 *
	 */
    public Element createElemenetForVariable(){
    	return null;
    }
    
    /**
     * 
     * anping
     * TODO 创建一个属性  用来表示外健的配置 
     * @return
     * @return_type:Element
     *
     */
	public void createElementsForForeign(DocumentFactory documentFactory,AnnotationContentFetchHelper  fetchHelper,
			AnnotationHelper annotationHelper,Element entity,String entityName) throws ClassNotFoundException{
	        List<EntityVaribleContainer> containers  =  annotationHelper.getForeignKeyAnnotation(entityName);
	       for(EntityVaribleContainer container:containers){
	        Element idElement =  documentFactory.createElement("property");
	        this.addArrtibuteForElement(documentFactory, idElement, container, fetchHelper);
	        entity.add(idElement);
	       }
	}	
    /**
     * 为xml中的Element 添加Arrtibute 属性
     * @param documentFactory
     * @param element
     * @param container
     * @param fetchHelper
     */
    public void addArrtibuteForElement(DocumentFactory documentFactory,Element element,EntityVaribleContainer container,AnnotationContentFetchHelper  fetchHelper){
    	element.add(documentFactory.createAttribute(element, "name", container.getVariableName()));
       
    	if(container.getVariableType()!=null){
    		element.add(documentFactory.createAttribute(element, "type", container.getVariableType()));
    		
       	 }
    	
    	if(container.getVariableRefClass()!=null){
    		element.add(documentFactory.createAttribute(element, "className", container.getVariableRefClass()));
          	 	
    	}
    	
    	if(container.getAnnotation()==null){
    	element.add(documentFactory.createAttribute(element, "column", container.getVariableName()));	
    
    	}else{
    		//将注解中的信息配置进去
    	   Map<String,String>  configValues = fetchHelper.getAnnotationContent(container.getAnnotation());
    	   Set<String>	keys  = configValues.keySet();
    	   Iterator<String> iterator  = keys.iterator();
    	   while(iterator.hasNext()){
    		   String configName = iterator.next();
    		   String configValue = configValues.get(configName);
    		   element.add(documentFactory.createAttribute(element,configName, configValue));	
    	    }
    	   
    	   if(configValues.get("column").equals("")){
    			element.add(documentFactory.createAttribute(element, "column", container.getVariableName()));	
    		    }
    	}
    	
     }
}
