package com.daoSupport.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class DaoSupportLog {
	private static  LogManager logManager;
    private static  Logger logger;	
	static{
		
		logManager =LogManager.getLogManager();
        logger= Logger.getGlobal();
	}
	/**
	 * 这是通过用户的配置文件拿取日志的配置，但是名字必须为DaoSupport.properties
	 **/
	private  static Logger  getLoggerByProperties(){
		logManager.addLogger(logger);
		try {
			logManager.readConfiguration(DaoSupportLog.class.getClassLoader().getResourceAsStream("DaoSupport.properties"));
		    return logger; 
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.out.println("你的DaoSupport.properties配置有误！");
			e.printStackTrace();
			return null;
		}
		 
	}
	/**
	 * 判断用户有没有做配置文件
	 * @return
	 */
	private static boolean  isUserConfigLog(){
	
		InputStream  stream =null;
		try {
	       stream=DaoSupportLog.class.getClassLoader().getResourceAsStream("DaoSupport.properties");
		} catch (Exception e) {
			 e.printStackTrace();
		     return false ;
		}
		
	    if(stream==null){
	    	return false;
	    }
		return true;
	}
	/**
	 * 使用非配置文件的
	 * @return
	 */
	private static Logger getLoggerByDefault(){
		
		return logger;
	}
	
	/**
	 * 判断拿取
	 * @return
	 */
	public  static Logger getLogger(){
		Logger logger =null;
		if(isUserConfigLog()){
			logger=getLoggerByProperties();
		}else{
			logger=getLoggerByDefault();
		}
		return logger;
	}
}
