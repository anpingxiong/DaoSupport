package com.daoSupport.annotationHelper;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import com.daoSupport.annotation.EntityAnnotation;
 
public class ClassScanHelper {
	/**
	 * 
	 * anping
	 * TODO 通过扫描指定的包 来获得所有的class 
	 * @param file   EntityTable.xml中定义的包所得到的源文件夹
	 * @return
	 * @return_type:Set<String>  是存放了所有的.class文件的东西
	 *
	 */
	public  void  scanFolderToGetClass(File file,String packagename){
		 
		File[]  files   =  file.listFiles(new ClassFileFilterHelper());
		 
		for(File  fileItem :files){
			if(fileItem.isDirectory()){
				 scanFolderToGetClass(fileItem,packagename+"."+fileItem.getName());
			}else if(fileItem.isFile()){
				 classSet.add(packagename+"."+fileItem.getName().substring(0, fileItem.getName().lastIndexOf('.')));
			}
		}
		 
	}
	/**
	 * 
	 * anping
	 * TODO     拿到所有的实体类 
	 * @return  
	 * @return_type:Set<String>  里面存储的是  实体类的名字 
	 *
	 */
	public  Set<String> getAllEntity(File file,String packagename){
		Set<String>  resultSet   = new HashSet<String>();
		
		this.scanFolderToGetClass(file, packagename);
		
		 
		for(String  className  : this.getClassSet()){
			  if(this.isEntity(className)){
				  resultSet.add(className);
			  }
		 }
		 return resultSet;
	}
	
	 
	
	/**
	 * 
	 * anping
	 * TODO 判断是不是实体类
	 * @param className
	 * @return
	 * @return_type:boolean  true为实体类 false 不是实体类
	 *
	 */
	public boolean  isEntity(String className){
		boolean  result = false;
		try {
			Class<?>  entity  = Class.forName(className);
			Annotation  annotation =  entity.getAnnotation(EntityAnnotation.class);
			if(annotation!=null){
				result=true;
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return result;
	}
		
	public Set<String> getClassSet() {
		return classSet;
	}
  
	
	private  Set<String> classSet   =new HashSet<String>();
	
	
	
}
