package com.daoSupport.test;

 
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.junit.Test;

import com.daoSupport.dao.EntityDao;
import com.daoSupport.dao.impl.EntityDaoImpl;
import com.daoSupport.exception.DBException;
import com.daoSupport.exception.ErrorException;
import com.daoSupport.po.School;
import com.daoSupport.po.Work;
import com.daoSupport.util.DBUtil;
import com.daoSupport.vo.QueryResult;
import com.mysql.jdbc.Connection;

public class TestDaoImpl {
	@Test
	public void testFindAllEntityByAnd() throws NoSuchFieldException, SecurityException, ErrorException, DBException{
	  EntityDao entity  = EntityDaoImpl.getInstance();
	  List<Object>   object  = new ArrayList<>();
	 
	  object.add(1);
	  object.add("121");
	 
	  Map<String, String> orderBy  = new LinkedHashMap<String, String>();
	  orderBy.put("id","desc");
	  orderBy.put("workName","desc");
	  QueryResult<Work> all=entity.findAllEntity(Work.class, 0, 1000000, orderBy, "id,workName",object,0);
 
	  QueryResult<School> school=entity.findAllEntity(School.class, 0, 1000000, null, null,null,0);
	  
	  QueryResult<Work> allWork=entity.findAllEntity(Work.class, 0, 1000000, null, null,null,0);
	  System.out.println(allWork.getResults().get(7).getDate());
	  
	
	} 
	@Test
	public void testFindEntityById() throws ErrorException, DBException{
		EntityDao entity  = EntityDaoImpl.getInstance();
		 Work work  = entity.findById(Work.class, 1);
		  System.out.print(work.getWorkName());
		  System.out.println(work.getDate());
	}
	
	@Test
	public void testFindEntity() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ErrorException, DBException{
		EntityDao entity  = EntityDaoImpl.getInstance();
		  List<Object>   object2  = new ArrayList<>();
		  object2.add(1);
		 Work work  =  entity.findEntity(Work.class,"id", object2);
		   System.out.println(work.getId());
		   System.out.println(work.getWorkName());
		 
	   	 School  school  = entity.findById(School.class, 2);
	      System.out.println(school.getSchoolName()+"111111111111");
	 }
	
	@Test
	public void testDelete() throws ErrorException, DBException{
		EntityDao entity  = EntityDaoImpl.getInstance();
		  System.out.println(  entity.delete(School.class, 2));
	}
	
	
	/**
	 * 拿到的是最大的结果
	 * @throws DBException 
	 * @throws ErrorException 
	 * */
	@Test
	public void testFindAllEntityByOr() throws ErrorException, DBException{
		EntityDao entity  = EntityDaoImpl.getInstance();
		  List<Object>   object  = new ArrayList<>();
		   object.add(1);
		  object.add("121");
		  System.out.println(object.get(0));
		  Map<String, String> orderBy  = new LinkedHashMap<String, String>();
		  orderBy.put("id","desc");
		  orderBy.put("workName","desc");
		  QueryResult<Work> all=entity.findAllEntity(Work.class, 0, 1000000, orderBy, "id,workName",object,1);
		  
	}
	
	@Test
	public void testSave() throws DBException, ErrorException{
		EntityDao entity  = EntityDaoImpl.getInstance();
 
		 
		 School  school2  = new School();
		 school2.setId(1);
		 entity.save(school2);
		 
		 Work  work  = new Work();
		 work.setId(1);
		 work.setDate(new Date());
		 work.setSchool(school2);
		 work.setWorkName("ac");
		 entity.save(work);
	}
	 
	@Test
	public void testUpdate() throws ErrorException, DBException{
		EntityDao entity  = EntityDaoImpl.getInstance();
		 Work work  =  new Work();
		 work.setId(2);
		 work.setDate(new Date());
	     work.setWorkName("anping");
		 work.setSchool(null);
		 entity.update(work);
		 
		 School school = new School();
		 school.setId(3);
		 school.setSchoolName("anping");
		 entity.update(school);
	}
	
	
//	public static void main(String args[]) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
     // Class<?> 还是要加的
	//		 Class<?> a  = Class.forName("com.lucene.po.Work");
//		  Object c =  a.newInstance();
//		 Field  field=   c.getClass().getDeclaredField("id");
//		  
//		 System.out.println(field.getName());
//	}
	/**
	 * 判断对象的成员变量的值是不是在xml 中是非空的,但是实际传入的值不为空 传入的参数是对象的成员变量的值 props 和xml中的
	 * element对象 ::规则是不检测主键和 Interger类型 成员变量 返回String
	 * 为null时表示正常,返回为非null，返回的是第一时间抓到的错误。
	 * 
	 * 测试通过
	 **/
	private String checkPropertiesNull(Map<String, Object> props,
			Element element) {
		String flag = null;

		Iterator<Element> properties = element.elementIterator();// xml中所有成员变量的配置

		while (properties.hasNext()) {// 迭代成员变量,查看是否有不符合规范的。
			Element propertie = properties.next();
			// 如果不是主键
			if (!(propertie.attribute("key") != null && propertie
					.attributeValue("key").equals("primary"))) {
				// 不为空
				if (propertie.attributeValue("null") != null
						&& propertie.attributeValue("null").equals("false")) {
					// 但是实际数据为空 不考虑int 因为默认为0
					if (props.get(propertie.attributeValue("name")) == null) {
						flag = propertie.attributeValue("name") + "不能为空";
						break;
					}
				}
			}
		}

		return flag;
	}
	
	@Test
	public void  testgetCount() throws DBException{
		EntityDao entity  = EntityDaoImpl.getInstance(); 
		 School school   = new School();
		  List<Object> obj  = new ArrayList<Object>();
		  
		  obj.add(1);
		 int a  =entity.getAllCount("select count(*) from t_school where id=?", obj);
		 System.out.println(a);
	 
	}
	
	@Test
	public void testPreStatement(){ 
	}
}
