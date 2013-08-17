package com.project.util;

import java.io.IOException;
import java.io.InputStream;
 
import java.util.Iterator;
 

 
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
 

 
 

public class ReadXmlUtil {
	private static SAXReader saxreader = null;
	static {
		saxreader = new SAXReader();
	}

	public  static  Boolean checkClassIsExists(String path, Class t)
			 {
 
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
		//判断类是不是存在
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
	
 
	
 
	
	//通过实体类拿到 Element
	public   static Element getClassElement(String path,Class t)  {
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
			//判断类是不是存在
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
	
	
	 
}
