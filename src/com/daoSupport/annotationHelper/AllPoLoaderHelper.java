package com.daoSupport.annotationHelper;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import com.daoSupport.exception.ErrorException;
import com.daoSupport.util.ReadXmlUtil;
/**
 * 思路是做一个专门加载所有的类名和包的
 * 
 * 在做一个转换为element对象的  【我们也就之需要这么一个Element对象】
 * 
 * 
 * 
 */
public class AllPoLoaderHelper {
     
	/** 
      * anping
      * TODO 为了去加载所有的实体类 
      * @return_type:返回的是实体类set类型,里面装的是我们的实体类的包名和类名
      *
      * 当然 选择这一步是说明在xml们已经配置了的
      * 首先  先拿到Dao 们所在的位置
      * 开始扫描保重的所有类  但是现在的关键问题是如何去做扫描 包中的所有类呢？？
      */
	public   Set<String> LoadAllPoByEntityXmlConfig(){
		String poBasePath  = ReadXmlUtil.getPoPathByXmlConfig();
		URL  url  = AllPoLoaderHelper.class.getClassLoader().getResource("EntityTable.xml");
		 String urlPath  = url.toString();
		
	    urlPath =  urlPath.substring(0, urlPath.lastIndexOf('/'))+"/"+this.changePOPath(poBasePath); 
		System.out.println(urlPath);
		File file = null;
		try {
			file = new File(new URI(urlPath));  //通过uri来创建啊SRC  因为urlPath是通过URL解析来的
		} catch (URISyntaxException e) {
			 e.printStackTrace();
			 
		}
	    if(file.exists()){
	    	System.out.println(poBasePath+"目录存在");
	    	//既然存在那么我们就直接去拿出所有的文件名,包括包下面的
	    	ClassScanHelper helper= new ClassScanHelper();
	    	 //拿到过滤出来的实体类
	    	Set<String> entitys = helper.getAllEntity(file,poBasePath);
	 
	    	if(entitys.size()==0){
	    		 try {
	 				throw  new ErrorException("EntityTable.xml中poBaseSrc所指的路径中不存在被EntityAnnoation标示的类，请确认po包名是否是以"+poBasePath+"开头");
	 			} catch (ErrorException e) {
	 			 	e.printStackTrace();
	 			} 
	    	}else{
	    		System.out.println("1111111");
	    		//这里才是实体类的地址 现在需要做的是创建
	    	    for(String entity:entitys){
	    			System.out.println(entity);
	    		}
	    		
	    	}
	    	
	    }else{
	    	System.out.println(poBasePath+"目录不存在");
	        try {
				throw  new ErrorException("EntityTable.xml中poBaseSrc所指的路径不存在，请确认po包名是否是以"+poBasePath+"开头");
			} catch (ErrorException e) {
			 	e.printStackTrace();
			} 
	    }
		return null;
	}
	/**
	 * 
	 * anping
	 * TODO  将xml中的 .转换为 / 将包路径转换为  实体文件的路径
	 * @param path 
	 * @return
	 * @return_type:String
	 *
	 */
	 public String  changePOPath(String path){
		 return path.replace('.','/');
	 }
	 
}
