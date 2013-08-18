package com.project.dao;

import java.util.List;
import java.util.Map;

import com.project.vo.QueryResult;

 

public interface EntityDao {
	
	/**使用如 
	 *  Work  work  = new Work();
	 *  work.setId(1);
	 *  work.set***("**");
	 *  
	 * save(work);
	 *  
	 * **/
	public int save(Object object);

	 /**
	  * 使用如： Work work  =  new Work();
	  * work.setId(***);
	  * work.set***(**);
	  * update(work);
	  * */
	public int update(Object object);

	 /**
	  * Work work  = new Work();
	  * work.setId(1);
	  * delete(work);
	  * 
	  * */
	
	public <T> int delete(Class<T> t, Object id);
	
	
    /**
     * findById(Work.class,1);
     * findById(Work.class,"nihao")
     * *
     */
     
	public <T> T findById(Class<T> t, Object id);

	 /**
	  * String sql_where ="id,workName";//是实体类的成员变量名
	  * List<Object>  parames = null;//对条件的赋值处理 // 和sql_where必须一一对应
	  * parames=new ArrayList<Object>();
	  * parames.set(1);
	  * parames.set("212");  
	  * findEntity（Work.class,sql_where,parames）；
	  * 
	  * */
	public <T> T findEntity(Class<T> t, String sql_where, List<Object> parames);

	 /**
	  * 同上
	  * 但是查找全部的话
	  * findAllEntity（Work.class,0,100000,null,null,null,0）；
	  * flag  为0 和1  :0代表都是条件and 1 代表or 
	  * */
	public <T> QueryResult<T> findAllEntity(Class<T> t, int firstIndex,
			int maxResult, Map<String, String> OrderBy, String sql_where,
			List<Object> parames, int flag);

	
	/***
	 *  下面两个是多表的
	 *  vo 也要早xml中配置
	 *  如：
	 *  sql = "select * from t_work ,t_school where t_work.id=t_school.id and id=? order by id desc limit(?,?)";
	 *
	 *List<Object> parames = new ArrayList<Object>();
	 *parames.add(1);
	 *parames.add(0);
	 *parames.add(5);
	 *findAllEntityByCompose(Vo.class,0,1000,sql,parames);
	 * */
	public <T> List<T> findAllEntityByCompose(Class<T> t, int firstIndex,
			int maxResult, String sql, List<Object> parames);
	
	
	/**
	 * 用来拿到总数量 用法同上
	 * */
	public int getAllCount(String sql,List<Object> parames);
}
