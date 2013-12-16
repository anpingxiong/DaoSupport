package com.daoSupport.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.dom4j.Element;

import com.daoSupport.annotationHelper.AllPoLoaderHelper;
import com.daoSupport.exception.ErrorException;
import com.daoSupport.poAutoToxml.util.Po2XmlUtil;

public class TestPo2XmlUtil {
	 
	public static void main(String[] args) throws ClassNotFoundException, ErrorException, IOException {
		Po2XmlUtil util  = new Po2XmlUtil();
		AllPoLoaderHelper  help   = new AllPoLoaderHelper();
		
		Element  root  = util.createXmlByPos(help.LoadAllPoByEntityXmlConfig());
		File file  = new File("/home/anping/testXml.xml");
		 
		FileWriter writer  = new FileWriter(file);
		root.write(writer);
		writer.flush();
		writer.close();
	}
}
