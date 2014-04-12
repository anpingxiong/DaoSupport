package com.daoSupport.test;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.Test;

import com.daoSupport.Log.DaoSupportLog;

 

 
public class Log4J2xTest {
	public static void main(String[] args) throws SecurityException, IOException {
	  
	  
	 LogManager logManager =  LogManager.getLogManager();
	 logManager.readConfiguration(Log4J2xTest.class.getClassLoader().getResourceAsStream("DaoSupport.properties"));
	 Logger  logger  = Logger.getLogger("abc");
	 
	 Level level   = Level.INFO;
	 logManager.getLogger("abc").log(level, "111");
	 logger.log(level, "1212");
	
	FileHandler  fileHandler  = new FileHandler("/media/LEARN/anping.log");
	Logger logger2  = Logger.getLogger("anping");
 
	}
	@Test
	public void testLog(){
		DaoSupportLog  log  = new DaoSupportLog();
 	log.getLogger().info("helloWorld");
 	log.getLogger().warning("完蛋了");
		//System.out.println(log.isUserConfigLog());
		
	}
}
