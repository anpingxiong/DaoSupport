package com.daoSupport.annotationHelper;

import java.io.File;
import java.io.FileFilter;

public class ClassFileFilterHelper implements FileFilter {

	/**
	 *anping
	 *TODO
	 *@param pathname
	 *@return
	 */
	@Override
	public boolean accept(File pathname) {
		boolean  result  = true;
        if(pathname.isFile()){
        String name   = pathname.getName();
    	name=name.substring(name.lastIndexOf('.'), name.length());
    	if(!name.equals(".class")){
    		result  = false;
    	}
    	
        }
		return result;
	}

}
