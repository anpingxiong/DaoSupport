package com.daoSupport.annotationHelper;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

//这个类主要是拿取注解中的信息的
public class AnnotationContentFetchHelper {
	

	/**
	 * 主要是通过一个注解来获取注解中的信息
	 * 注解类toString 之后的格式如：@com.daoSupport.annotation.PrimaryKeyAnnotation(update=false, auto_increment=true, column=id)
	 * 我们希望得到的是　<update,false><auto_increment,true>,<column,id>
	 * @param annotation 这个是穿过来的注解类
	 * @return  返回的是含有注解信息的注解类
	 */
	public Map<String,String>  getAnnotationContent(Annotation annotation){
		Map<String ,String > result   =  null;
		String  annotationToString = annotation.toString();
	    annotationToString =  annotationToString.substring(annotationToString.indexOf('(')+1,annotationToString.indexOf(')')).trim();
		 
		if(annotationToString==null || annotationToString.equals("") ){  //判断是不是null对象，如果是null或者字符串是""的话那么返会的结果也应该是null
		}else{
			result   = new HashMap<String,String>(5);
			String[]  messages   = annotationToString.split(",");
		    for(String mes: messages){
		        //在此将信息保存在此Map 中去
		    	 mes  =   mes.trim();
		    	 String[]  messAtrribute   =mes.split("=");
		    	 result.put(messAtrribute[0], messAtrribute[1]);
		    } 	
		}
	    return result;
	}
	
}
