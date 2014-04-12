/*
 * 设计为异常的话是会影响系统的效率的
 * 会有多余的开销
 * */
package com.daoSupport.util;

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
	public static Boolean doCheck(Class<?> c) {
		 return ReadXmlUtil.checkClassIsExists(c);
    }
	 

}
