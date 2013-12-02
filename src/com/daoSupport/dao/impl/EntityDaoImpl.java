package com.daoSupport.dao.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.daoSupport.Log.DaoSupportLog;
import com.daoSupport.dao.EntityDao;
import com.daoSupport.daoHelper.EntitySaveHelper;
import com.daoSupport.daoHelper.EntitySearchHelper;
import com.daoSupport.daoHelper.EntityUpdateHelper;
import com.daoSupport.daoHelper.EntityUtilHelper;
import com.daoSupport.exception.DBException;
import com.daoSupport.exception.ErrorException;
import com.daoSupport.util.CheckEntityUtil;
import com.daoSupport.util.DBUtil;
import com.daoSupport.util.ReadXmlUtil;
import com.daoSupport.vo.QueryResult;

public class EntityDaoImpl implements EntityDao {

	private EntityDaoImpl() {
	}

	private static class EntityDaoHelper {
		static final EntityDao INSTANCE = new EntityDaoImpl();
	}

	public static EntityDao getInstance() {
		return EntityDaoHelper.INSTANCE;
	}

	/***
	 * 
	 * save 的实现方式: 1.先通过传过来的类来拿到需要保存的实体类是那一个,拿到类名 2.判断这个类是不是实体类: 未实现 3.去xml中查找出
	 * table 名，和所有的字段名 4.开始准备拼接Sql语句， 字段不为空的就拼到里面去。 5.拼接完了就执行。
	 * 
	 * 判断是否为空和唯一都在service层判断
	 * 
	 * @throws DBException
	 * @throws ErrorException
	 * */
	@Override
	public int save(Object object) throws DBException, ErrorException {
		Element classElement = null;
		// 如果不是实体类的话 那就直接跳出程序
		if (false == CheckEntityUtil.doCheck(object.getClass()))
			throw new ErrorException("数据库实体类不存在");
		// 拿到这个类的element 的对象
		classElement = ReadXmlUtil.getClassElement(object.getClass());

		// 字段的所有数据保存在这里了
		Map<String, Object> props = EntityUtilHelper.getAllFieldsValue(object);

		String tableName = classElement.attributeValue("table");
		String sql = "insert into  " + tableName + "(";

		sql = EntitySaveHelper.createInsertSql(sql, props, classElement);
//		System.out.println(sql);
		DaoSupportLog.getLogger().info(sql);
		// 连接数据库
		Connection connection = DBUtil.getconn();
		PreparedStatement pstmt = null;
		try {
			int pCount = 1;
			pstmt = DBUtil.getstst(connection, sql);
			// 首先看看需不需要处理 primary key
			Element primaryKey = EntityUtilHelper.getColumnByPropertiesnName("id", classElement);
			Boolean idIncrement = Boolean.parseBoolean(primaryKey
					.attributeValue("auto_increment"));

			if (idIncrement == false) {
				EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, pCount,
						primaryKey.attributeValue("type"), props.get("id"));
				pCount++;
			}

			// 开始处理非主键 的东西
			Iterator<String> fieldsIterator = props.keySet().iterator();
			while (fieldsIterator.hasNext()) {
				String name = fieldsIterator.next();
				// 我们插入的数据都是要非空的
				if (props.get(name) != null) {
					Element prop = EntityUtilHelper.getColumnByPropertiesnName(name,
							classElement);
					// 为非主外键
					if (prop != null && prop.attribute("key") == null) {
						EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, pCount,
								prop.attributeValue("type"), props.get(name));
						pCount++;
					}
					// 外键处理
					if (prop != null && prop.attribute("key") != null
							&& prop.attributeValue("key").equals("foreign")) {
						String fkClassName = prop.attributeValue("className");

						Field field = Class.forName(fkClassName)
								.getDeclaredField("id");

						field.setAccessible(true);
						EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, pCount,
								prop.attributeValue("type"), field.get(props
										.get(prop.attributeValue("name"))));
						pCount++;
					}
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		int result;
		try {
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		}

		DBUtil.close(connection, pstmt);
		return result;
	}

	/**
	 * update 的实现思路: 1.拿到对象之后我们来拿到类名（然后判断是不是实体类） 2.去xml中匹配 ， 拿到表名 和属性名
	 * 3.拼接sql语句， 字段中的值为 null 的则不拼到sql中 4.拼接完之后执行
	 * 
	 * 
	 * 是可以更新父类的 更新只能够来更通过id 来更新（在这里我们不判断是否已经存在了这个对象,而是给service层判断） update t_work
	 * set username = **,sid =** where id = **;
	 * 
	 * @throws ErrorException
	 * @throws DBException
	 * */
	@Override
	public int update(Object object) throws ErrorException, DBException {
		Element classElement = null;

		// 如果不是实体类的话 那就直接跳出程序
		if (false == CheckEntityUtil.doCheck(object.getClass()))
			throw new ErrorException("数据库实体类不存在");
		// 拿到这个类的element 的对象
		classElement = ReadXmlUtil.getClassElement(object.getClass());

		// 字段的所有数据保存在这里了
		Map<String, Object> props = EntityUtilHelper.getAllFieldsValue(object);

		String tableName = classElement.attributeValue("table");

		String sql = "update " + tableName + " set ";

		sql = EntityUpdateHelper.createUpdateBySql(sql, props, classElement);

		if (sql == null) {
			throw new ErrorException("更新的数据不能为空");
		}
		DaoSupportLog.getLogger().info(sql) ;
		// ----连接DB
		Connection connection = DBUtil.getconn();
		PreparedStatement pstmt = null;
		int pCount = 1;
		try {
			pstmt = DBUtil.getstst(connection, sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		}
		// ---开始预处理db
		// 开始处理非主键 的东西
		Iterator<String> fieldsIterator = props.keySet().iterator();
		while (fieldsIterator.hasNext()) {
			String fieldName = fieldsIterator.next();
			if (props.get(fieldName) != null) {
				Element element = EntityUtilHelper.getColumnByPropertiesnName(fieldName,
						classElement);
				try {
					// 处理非主外键
					if ((element.attribute("update") == null || element
							.attributeValue("update").equals("true"))
							&& element.attribute("key") == null) {
						EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, pCount,
								element.attributeValue("type"),
								props.get(fieldName));
						pCount++;
					} else if ((element.attribute("update") == null || element
							.attributeValue("update").equals("true"))
							&& element.attribute("key") != null
							&& element.attributeValue("key").equals("foreign")) {
						String fkClassName = element
								.attributeValue("className");

						Field field = Class.forName(fkClassName)
								.getDeclaredField("id");

						field.setAccessible(true);
						EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, pCount,
								element.attributeValue("type"), field.get(props
										.get(element.attributeValue("name"))));
						pCount++;
					}

				} catch (SQLException e) {
					e.printStackTrace();
					throw new DBException("抱歉,系统异常");
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}
		}
		// ---------处理非主键结束
		// 开始处理主键
		Element primaryKey = EntityUtilHelper.getColumnByPropertiesnName("id", classElement);
		try {
			EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, pCount,
					primaryKey.attributeValue("type"), props.get("id"));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		}
		// ----处理主键结束
		int result;
		try {
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		}
		DBUtil.close(connection, pstmt);
		return result;
	}

	/**
	 * delete 实现思路: 1. 通过查过来的Class<T> t 去判断是不是实体类 2. 通过 t 去xml 中匹配,拿到 表名 3.
	 * 拼接删除语句 4. 执行
	 * 
	 * c测试成功：： 删除过程中出现错误会自动回退 实现回退是需要设置自动提交为false 的；
	 * 
	 * @throws ErrorException
	 * @throws DBException
	 * */
	@Override
	public <T> int delete(Class<T> t, Object id) throws ErrorException,
			DBException {
		// -----检测实体类是否存在 开始

		if (CheckEntityUtil.doCheck(t) == false)
			throw new ErrorException("数据库实体类不存在");

		// ----检测完毕

		// ---拿到类的xml配置文件
		Element classElement = null;

		classElement = ReadXmlUtil.getClassElement(t);

		String tableName = classElement.attributeValue("table");
		// 拿到表明字
		Element element = EntityUtilHelper.getColumnByPropertiesnName("id", classElement);

		// 拼接sql
		String sql = "delete from " + tableName + " where "
				+ element.attributeValue("column") + "=?";

		// 连接db
		java.sql.Connection connection = DBUtil.getconn();

		PreparedStatement pstmt = null;
		try {
			pstmt = DBUtil.getstst(connection, sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉，系统异常");
		}
		// ----预处理开始
		try {
			EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, 1,
					element.attributeValue("type"), id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ErrorException("你传入的参数值类型不正确");
		}
		// ----预处理结束
		int result;
		try {

			connection.setAutoCommit(false);
			result = pstmt.executeUpdate();
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();// 回退数据库
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DBException("抱歉，系统异常");
		}

		DBUtil.close(connection, pstmt);
		return result;
	}

	@Override
	/**
	 * 直接调用 检测字段唯一的方法来拿到数据
	 * 
	 * 测试通过
	 * */
	public <T> T findById(Class<T> t, Object id) throws ErrorException,
			DBException {

		List<Object> parames = new ArrayList<Object>();
		parames.add(id);
		return this.findEntity(t, "id", parames);

	}

	/**
	 * 直接调用getAllEntity 测试通过
	 * 
	 * @throws ErrorException
	 * @throws DBException
	 * */
	@Override
	public <T> T findEntity(Class<T> t, String sql_where, List<Object> parames)
			throws ErrorException, DBException {
		QueryResult<T> entitys = this.findAllEntity(t, 0, 1, null, sql_where,
				parames, 0);
		if (entitys == null)
			return null;
		else
			return entitys.getResults().get(0);
	}

	/**
	 * sql_where传入的是Entity的成员变量 为了避免我们自己还需要在查找表的列 parames传入的是sql_where对应的值
	 * sql_where 的格式如下:username 或username,age
	 *    如果数据为空则返回null
	 * @throws ErrorException
	 * @throws DBException
	 * */
	@Override
	public <T> QueryResult<T> findAllEntity(Class<T> t, int firstIndex,
			int maxResult, Map<String, String> OrderBy, String sql_where,
			List<Object> parames, int flag) throws ErrorException, DBException {
		// -----检测实体类是否存在 开始
		if (CheckEntityUtil.doCheck(t) == false)
			throw new ErrorException("数据库实体类不存在");

		// ----检测完毕
		Element element = null;

		element = ReadXmlUtil.getClassElement(t);

		String tableName = element.attributeValue("table");

		Map<Element, Object> condition = null;

		// 对sql的拼接需要判断sql_where是不是空的
		String sql = "select  * from " + tableName;
		String sql2 = "select count(*) from " + tableName;
		if (sql_where != null) {
			sql = sql + " where  ";
			sql2 = sql2 + " where ";
			condition = EntitySearchHelper.interpretSqlWhereWithParame(element, sql_where,
					parames);
			Iterator<Element> condtionElement = condition.keySet().iterator();
			while (condtionElement.hasNext()) {
				Element prop = condtionElement.next();
				if (flag == 0) {
					sql = sql + " " + prop.attributeValue("column") + "=? and ";
					sql2 = sql2 + " " + prop.attributeValue("column")
							+ "=? and ";
				} else {
					sql = sql + " " + prop.attributeValue("column") + "=? or ";
					sql2 = sql2 + " " + prop.attributeValue("column")
							+ "=? or ";
				}
			}
			if (flag == 0) {
				sql = sql.substring(0, sql.lastIndexOf("and"));
				sql2 = sql2.substring(0, sql2.lastIndexOf("and"));
			} else {
				sql = sql.substring(0, sql.lastIndexOf("or"));
				sql2 = sql2.substring(0, sql2.lastIndexOf("or"));
			}
		}

		sql = EntitySearchHelper.createOrderBySql(OrderBy, sql, element);
		sql = sql + " limit " + firstIndex + "," + maxResult;
		// sql语句拼接结束 
		DaoSupportLog.getLogger().info(sql);
		DaoSupportLog.getLogger().info(sql2);
		java.sql.Connection conn = DBUtil.getconn();
		PreparedStatement pstmt = null;
		// ---预处理开始
		try {
			pstmt = DBUtil.getstst(conn, sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉，系统异常");
		}
		if (condition != null) {
			Iterator<Element> condtionElement = condition.keySet().iterator();
			int i = 1;
			while (condtionElement.hasNext()) {
				Element prop = condtionElement.next();
				try {
					EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, i,
							prop.attributeValue("type"), condition.get(prop));
					i++;
				} catch (SQLException e) {
					e.printStackTrace();
					throw new DBException("抱歉,系统异常");
				}
			}
		}
		// --预处理结束
		// 游标开始
		ResultSet set;
		List<T> result = null;
		try {
			set = pstmt.executeQuery();
			result = EntitySearchHelper.getObjectByResultSet(set, element, t);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		}

		try {
			set.close(); // 关闭游标
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 游标结速；
		int count = 0;
		QueryResult<T> queryResult = null;
		// 计算总算开始
		if (result.size() == 0) {
			System.out.println("没查到结果");
		} else {

			// ---预处理开始
			try {
				pstmt = DBUtil.getstst(conn, sql2);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException("抱歉，系统异常");
			}
			if (condition != null) {
				Iterator<Element> condtionElement = condition.keySet()
						.iterator();
				int i = 1;
				while (condtionElement.hasNext()) {
					Element prop = condtionElement.next();
					try {
						EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, i,
								prop.attributeValue("type"),
								condition.get(prop));
						i++;
					} catch (SQLException e) {
						e.printStackTrace();
						throw new DBException("抱歉,系统异常");
					}
				}
			}
			// --预处理结束

			try {
				ResultSet ResultSet = pstmt.executeQuery();
				if (ResultSet.next()) {
					count = ResultSet.getInt(1);
					System.out.println(count);
				}
				queryResult = new QueryResult<T>();
				queryResult.setResults(result);
				queryResult.setTotalCount(count);
				ResultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		DBUtil.close(conn, pstmt);

		// 计算总数结束
		return queryResult;
	}



 

 


	
	@Override
	public <T> List<T> findAllEntityByCompose(Class<T> t, int firstIndex,
			int maxResult, String sql, List<Object> parames) throws DBException {
		// sql语句拼接结束
		System.out.println(sql);
		java.sql.Connection conn = DBUtil.getconn();
		PreparedStatement pstmt = null;
		// ---预处理开始
		try {
			pstmt = DBUtil.getstst(conn, sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉，系统异常");
		}
		int i = 1;
		if (parames != null) {
			Iterator<Object> iterator = parames.iterator();
			while (iterator.hasNext()) {
				Object key = iterator.next();

				try {
					EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, i, key
							.getClass().getSimpleName(), key);
					i++;
				} catch (SQLException e) {
					e.printStackTrace();
					throw new DBException("抱歉，系统异常");
				}
			}
		}

		try {
			pstmt.setInt(i, firstIndex);
			pstmt.setInt(i + 1, maxResult);
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new DBException("抱歉，系统异常");
		}

		// --预处理结束
		// 游标开始
		Element element = ReadXmlUtil.getClassElement(t);
		ResultSet set;
		List<T> result = null;
		try {
			set = pstmt.executeQuery();

			result = EntitySearchHelper.getObjectByResultSet(set, element, t);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		}

		try {
			set.close(); // 关闭游标
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBUtil.close(conn, pstmt);

		// 计算总数结束
		return result;
	}

	@Override
	public int getAllCount(String sql, List<Object> parames) throws DBException {
		Connection connection = DBUtil.getconn();
		PreparedStatement pstmt = null;
		try {
			pstmt = DBUtil.getstst(connection, sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		}

		int i = 1;
		if (parames != null) {

			Iterator<Object> iterator = parames.iterator();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				try {
					System.out.println(key);
					EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, i, key
							.getClass().getSimpleName(), key);
					i++;
				} catch (SQLException e) {
					e.printStackTrace();
					throw new DBException("抱歉，系统异常");
				}
			}
		}

		ResultSet set = null;
		int result = 0;
		try {
			set = pstmt.executeQuery();
			if (set.next()) {
				result = set.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		}

		try {
			set.close(); // 关闭游标
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBUtil.close(connection, pstmt);
		return result;
	}

	@Override
	public <T> void BatchSave(List<T> lists, Class<T> t) throws ErrorException,
			DBException {
		Element classElement = null;
		// 如果不是实体类的话 那就直接跳出程序
		if (false == CheckEntityUtil.doCheck(t))
			throw new ErrorException("数据库实体类不存在");
		// 拿到这个类的element 的对象
		classElement = ReadXmlUtil.getClassElement(t);

		// 字段的所有数据保存在这里了
		// 字段的所有数据保存在这里了
		Map<String, Object> prop_11 = EntityUtilHelper.getAllFieldsValue(lists.get(0));

		String tableName = classElement.attributeValue("table");
		String sql = "insert into  " + tableName + "(";

		sql = EntitySaveHelper.createInsertSql(sql, prop_11, classElement);

		// 连接数据库
		Connection connection = DBUtil.getconn();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
		 	e1.printStackTrace();
		 	throw new ErrorException("服务器异常");
		}
		
		PreparedStatement pstmt;
		try {
			pstmt = DBUtil.getstst(connection, sql);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		 	throw new ErrorException("服务器异常");
		};
		
		try {
			// 开始批量的处理
			for (Iterator<T> iterator = lists.iterator(); iterator.hasNext();) {
		 
				T tObject = iterator.next();
			 
				Map<String, Object> props = EntityUtilHelper.getAllFieldsValue(tObject);
			 
				int pCount = 1;
			
				// 首先看看需不需要处理 primary key
				Element primaryKey = EntityUtilHelper.getColumnByPropertiesnName("id",
						classElement);
				Boolean idIncrement = Boolean.parseBoolean(primaryKey
						.attributeValue("auto_increment"));

				if (idIncrement == false) {
					EntityUtilHelper.setPreparedStatementByPropertieType(pstmt, pCount,
							primaryKey.attributeValue("type"), props.get("id"));
					pCount++;
				}

				// 开始处理非主键 的东西
				Iterator<String> fieldsIterator = props.keySet().iterator();
				while (fieldsIterator.hasNext()) {
					String name = fieldsIterator.next();
					// 我们插入的数据都是要非空的
					if (props.get(name) != null) {
						Element prop = EntityUtilHelper.getColumnByPropertiesnName(name,
								classElement);
						// 为非主外键
						if (prop != null && prop.attribute("key") == null) {
							EntityUtilHelper.setPreparedStatementByPropertieType(pstmt,
									pCount, prop.attributeValue("type"),
									props.get(name));
							pCount++;
						}
						// 外键处理
						if (prop != null && prop.attribute("key") != null
								&& prop.attributeValue("key").equals("foreign")) {
							String fkClassName = prop
									.attributeValue("className");

							Field field = Class.forName(fkClassName)
									.getDeclaredField("id");

							field.setAccessible(true);
							EntityUtilHelper.setPreparedStatementByPropertieType(pstmt,
									pCount, prop.attributeValue("type"), field
											.get(props.get(prop
													.attributeValue("name"))));
							pCount++;
						}
					}
				}
				pstmt.addBatch();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("抱歉,系统异常");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		 
		try {
		  pstmt.executeBatch();
		  connection.commit();
		 } catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new DBException("抱歉,系统异常");
		 	}

		DBUtil.close(connection, pstmt);
	 
	}
}
