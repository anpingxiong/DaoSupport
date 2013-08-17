/*
 * 设计为异常的话是会影响系统的效率的
 * 会有多余的开销
 * */
package com.project.util;

import java.io.IOException;

import org.dom4j.DocumentException;
 

/**
 * 由于用到的次数相当的的多,所以我选择将这个类变成是final
 * */
public class CheckEntityUtil {
	/**
	 * 以什么方式去检测是不是entity阿？ 第一,就是 检测包名是不是最后以po结束 第二,去xml中匹配是不是存在呢
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 * */
	public static Boolean doCheck(Class c,String path) {
		Boolean flag = false;// 标记是不是实体类
		String packageName = c.getName();
	 
		String[] string = packageName.split("\\.");
		System.out.println(string.length);
		// 检验是不是po
		for (int i = 0; i < string.length; i++) {
		 
			if (string[i].equals("po")) {
			
				flag = true;
				break;
			}
		}

		 
		if (flag == false) {
			 return false;
		}

		return ReadXmlUtil.checkClassIsExists(path, c);

	}
	 

}
