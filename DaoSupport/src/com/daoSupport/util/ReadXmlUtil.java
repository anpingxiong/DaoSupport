package com.daoSupport.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.daoSupport.annotationHelper.AllPoLoaderHelper;
import com.daoSupport.exception.ErrorException;
import com.daoSupport.po.School;
import com.daoSupport.po.Work;
import com.daoSupport.poAutoToxml.util.Po2XmlUtil;

public class ReadXmlUtil {
	private static SAXReader saxreader = null;
	private static Element rootElement = null;
	private static String path = "EntityTable.xml";
	 static {
		saxreader = new SAXReader();
		// 首先我就拿到xml中的所有的Element的对象
		try {
			rootElement = getRootElementBypath(path);
		} catch (ClassNotFoundException | ErrorException e) {
			 e.printStackTrace();
		}
	}

	
	/**
	  public static Boolean checkClassIsExists(String path, Class<?> t) {
	 

		Boolean result = false;
		// 拿到classpath下的文件
		InputStream inputStream = ReadXmlUtil.class.getClassLoader()
				.getResourceAsStream(path);
		Document document = null;
		try {
			document = saxreader.read(inputStream);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 拿到root
		Element element = document.getRootElement();
		// 现在是拿到了root，但是我们要拿到的孩子,所有我们是先拿到孩子的迭代器
		Iterator<Element> childrensIterator = element.elementIterator();
		// 判断类是不是存在
		while (childrensIterator.hasNext()) {
			Element children = childrensIterator.next();
			if (t.getName().equals(children.attributeValue("className"))) {
				result = true;
				break;
			}
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 通过实体类拿到 Element 当然这个path现在来说是不需要了的
	public static Element getClassElement(String path, Class t) {
		Element classElement = null;
		// 拿到classpath下的文件
		InputStream inputStream = ReadXmlUtil.class.getClassLoader()
				.getResourceAsStream(path);
		Document document = null;
		try {
			document = saxreader.read(inputStream);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 拿到root
		Element element = document.getRootElement();
		// 现在是拿到了root，但是我们要拿到的孩子,所有我们是先拿到孩子的迭代器
		Iterator<Element> childrensIterator = element.elementIterator();
		// 判断类是不是存在
		while (childrensIterator.hasNext()) {
			Element children = childrensIterator.next();
			if (t.getName().equals(children.attributeValue("className"))) {
				classElement = children;
				break;
			}
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classElement;
	}
	
***/
	/**
	 * 
	 * @param path 路径默认是EntityTable.xml
	 * @return 返回的是根Elment
	 * @throws ErrorException 
	 * @throws ClassNotFoundException 
	 */
	private static Element getRootElementBypath(String path) throws ClassNotFoundException, ErrorException {
		Element rootElement = null;
		// 拿到classpath下的文件
		InputStream inputStream = ReadXmlUtil.class.getClassLoader()
				.getResourceAsStream(path);
		Document document = null;
		try {
			document = saxreader.read(inputStream);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 拿到对象之后我关闭流
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 拿到root
		rootElement = document.getRootElement();
	 
		
		//在此判断是不是配置需要使用注解版本，
		if(getPoPathByXmlConfig(rootElement)!=null){
			if(rootElement.elements("Entity").size()!=0){
				throw new ErrorException("你配置了使用注解版就不要在配置Entity,请您取消注解或者是删除所有的Entity");
			}
			
			Po2XmlUtil util  = new Po2XmlUtil();
			AllPoLoaderHelper  help   = new AllPoLoaderHelper();
			
		//	rootElement=util.createXmlByPos(help.LoadAllPoByEntityXmlConfig(rootElement));
	    	 Element entitysRoot  = util.createXmlByPos(help.LoadAllPoByEntityXmlConfig(rootElement));
		     List<Element> entitys  =   entitysRoot.elements("Entity");
		    for(Element entity :entitys){
			     rootElement.add((Element)entity.clone());
		     }
		  }
		 
		return rootElement;
	}
    /**
     * 可以选择的  不一定是rooElement
     * */
	public static Element getRootElement() {
		return rootElement;
	}

	
	//这样子拿到的classElement就不需要再次的去读取xml了
	public static Element getClassElement(Class<?> t) {
		Element classElement = null;
		// 现在是拿到了root，但是我们要拿到的孩子,所有我们是先拿到孩子的迭代器
		Iterator<Element> childrensIterator = getRootElement().elementIterator();
		// 判断类是不是存在
		while (childrensIterator.hasNext()) {
			Element children = childrensIterator.next();
			if (t.getName().equals(children.attributeValue("className"))) {
				classElement = children;
				break;
			}
		}
		return classElement;
	}
	
	public static Boolean checkClassIsExists(Class<?> t) {
		Boolean result = false;
	 
		// 现在是拿到了root，但是我们要拿到的孩子,所有我们是先拿到孩子的迭代器
		Iterator<Element> childrensIterator = getRootElement().elementIterator();
		// 判断类是不是存在
		while (childrensIterator.hasNext()) {
			Element children = childrensIterator.next();
			if (t.getName().equals(children.attributeValue("className"))) {
				result = true;
				break;
			}
		}
		 
		return result;
 
	}

	/**
	 * 
	 * anping  
	 * TODO 为了去xml查找是 po的基本路径, 对于是否有无该配置在调用之前要已经判断完毕了的
	 * @return
	 * @throws ErrorException 
	 * @return_type:String
	 *
	 */
	public static String getPoPathByXmlConfig(Element rootElement) {
		String result=null;
		Element element  =rootElement.element("useAnnotation");
 
		//判断useAnnotation存不存在，在判断poBaseSrc存不存在
		if(element==null){}
		else{
			Attribute attribute =element.attribute("poBaseSrc");
			if(attribute==null){
		 	}else{
		 		String   text  = attribute.getText();
		 		if(text==null || text.equals("")){
		 		}else{
		 			//拿到数据
		 			result=text;
		 		}
			}
		}
		
		return result;
	}
	

	 
}
