package com.daoSupport.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.daoSupport.annotationHelper.AllPoLoaderHelper;
import com.daoSupport.util.ReadXmlUtil;

public class TestXmlReadUtil {
	@Test
	public void checkIsAnnotation(){
		System.out.println(ReadXmlUtil.getPoPathByXmlConfig() );
	}
	@Test
	public void checkPoSrc (){
	new	AllPoLoaderHelper().LoadAllPoByEntityXmlConfig();
	}
	
	 
}
