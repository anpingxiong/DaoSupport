package com.daoSupport.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Element;
import org.junit.Test;

import com.daoSupport.util.ReadXmlUtil;

public class TestXmlReadUtil {
	@Test
	public void checkIsAnnotation() throws IOException{
		 
		Element  root   = ReadXmlUtil.getRootElement();
		File file  = new File("/home/anping/testXml.xml");
		 
		FileWriter writer  = new FileWriter(file);
 		root.write(writer);
		writer.flush();
		writer.close();
	}
	@Test
	public void checkPoSrc (){
//	new	AllPoLoaderHelper().LoadAllPoByEntityXmlConfig();
	}
	
	 
}
