package com.ucac.dao.impl;

import java.io.IOException;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.ucac.dao.EntityDao;
import com.ucac.exception.DBException;
import com.ucac.exception.ErrorException;
import com.ucac.util.CheckEntityUtil;
import com.ucac.util.DBUtil;
import com.ucac.util.ReadXmlUtil;
import com.ucac.vo.QueryResult;

public class EntityDaoImpl implements EntityDao {
	/***
	 * 
	 * save 的实现方式: 1.先通过传过来的类来拿到需要保存的实体类是那一个,拿到类名 2.判断这个类是不是实体类: 未实现 3.去xml中查找出
	 * table 名，和所有的字段名 4.开始准备拼接Sql语句， 字段不为空的就拼到里面去。 5.拼接完了就执行。
	 * 
	 * 判断是否为空和唯一都在service层判断
	 * */
	@Override
	public int save(Object object) {
		Element classElement = null;
		// 如果不是实体类的话 那就直接跳出程序
		if (false == CheckEntityUtil.doCheck(object.getClass(),
				"EntityTable.xml"))
			throw new ErrorException("数据库实体类不存在");
		// 拿到这个类的element 的对象
		classElement = ReadXmlUtil.getClassElement("EntityTable.xml",
				object.getClass());

		// 字段的所有数据保存在这里了
		Map<String, Object> props = this.getAllFieldsValue(object);

		String tableName = classElement.attributeValue("table");
		String sql = "insert into  " + tableName + "(";

		sql = this.createInsertSql(sql, props, classElement);
		System.out.println(sql);
		// 连接数据库
		Connection connection = DBUtil.getconn();
		PreparedStatement pstmt = null;
		try {
			int pCount = 1;
			pstmt = DBUtil.getstst(connection, sql);
			// 首先看看需不需要处理 primary key
			Element primaryKey = getColumnByPropertiesnName("id", classElement);
			Boolean idIncrement = Boolean.parseBoolean(primaryKey
					.attributeValue("auto_increment"));

			if (idIncrement == false) {
				this.setPreparedStatementByPropertieType(pstmt, pCount,
						primaryKey.attributeValue("type"), props.get("id"));
				pCount++;
			}

			// 开始处理非主键 的东西
			Iterator<String> fieldsIterator = props.keySet().iterator();
			while (fieldsIterator.hasNext()) {
				String name = fieldsIterator.next();
				// 我们插入的数据都是要非空的
				if (props.get(name) != null) {
					Element prop = getColumnByPropertiesnName(name,
							classElement);
					// 为非主外键
					if (prop != null && prop.attribute("key") == null) {
						this.setPreparedStatementByPropertieType(pstmt, pCount,
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
						this.setPreparedStatementByPropertieType(pstmt, pCount,
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
	 * */
	@Override
	public int update(Object object) {
		Element classElement = null;

		// 如果不是实体类的话 那就直接跳出程序
		if (false == CheckEntityUtil.doCheck(object.getClass(),
				"EntityTable.xml"))
			throw new ErrorException("数据库实体类不存在");
		// 拿到这个类的element 的对象
		classElement = ReadXmlUtil.getClassElement("EntityTable.xml",
				object.getClass());

		// 字段的所有数据保存在这里了
		Map<String, Object> props = this.getAllFieldsValue(object);

		String tableName = classElement.attributeValue("table");

		String sql = "update " + tableName + " set ";

		sql = this.createUpdateBySql(sql, props, classElement);

		if (sql == null) {
			throw new ErrorException("更新的数据不能为空");
		}
		System.out.println(sql);
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
				Element element = getColumnByPropertiesnName(fieldName,
						classElement);
				try {
					// 处理非主外键
					if ((element.attribute("update") == null || element
							.attributeValue("update").equals("true"))
							&& element.attribute("key") == null) {
						this.setPreparedStatementByPropertieType(pstmt, pCount,
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
						this.setPreparedStatementByPropertieType(pstmt, pCount,
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
		Element primaryKey = getColumnByPropertiesnName("id", classElement);
		try {
			this.setPreparedStatementByPropertieType(pstmt, pCount,
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
	 * */
	@Override
	public <T> int delete(Class<T> t, Object id) {
		// -----检测实体类是否存在 开始

		if (CheckEntityUtil.doCheck(t, "EntityTable.xml") == false)
			throw new ErrorException("数据库实体类不存在");

		// ----检测完毕

		// ---拿到类的xml配置文件
		Element classElement = null;

		classElement = ReadXmlUtil.getClassElement("EntityTable.xml", t);

		String tableName = classElement.attributeValue("table");
		// 拿到表明字
		Element element = getColumnByPropertiesnName("id", classElement);

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
			this.setPreparedStatementByPropertieType(pstmt, 1,
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
	public <T> T findById(Class<T> t, Object id) {
		List<Object> parames = new ArrayList<Object>();
		parames.add(id);
		return this.findEntity(t, "id", parames);
	}

	/**
	 * 直接调用getAllEntity 测试通过
	 * */
	@Override
	public <T> T findEntity(Class<T> t, String sql_where, List<Object> parames) {
		QueryResult<T> entitys = this.findAllEntity(t, 0, 10000, null,
				sql_where, parames, 0);
		if (entitys == null)
			return null;
		else
			return entitys.getResults().get(0);
	}

	/**
	 * sql_where传入的是Entity的成员变量 为了避免我们自己还需要在查找表的列 parames传入的是sql_where对应的值
	 * sql_where 的格式如下:username 或username,age
	 * */
	@Override
	public <T> QueryResult<T> findAllEntity(Class<T> t, int firstIndex,
			int maxResult, Map<String, String> OrderBy, String sql_where,
			List<Object> parames, int flag) {
		// -----检测实体类是否存在 开始
		if (CheckEntityUtil.doCheck(t, "EntityTable.xml") == false)
			throw new ErrorException("数据库实体类不存在");

		// ----检测完毕
		Element element = null;

		element = ReadXmlUtil.getClassElement("EntityTable.xml", t);

		String tableName = element.attributeValue("table");

		Map<Element, Object> condition = null;

		// 对sql的拼接需要判断sql_where是不是空的
		String sql = "select  * from " + tableName;
		if (sql_where != null) {
			sql = sql + " where  ";

			condition = this.interpretSqlWhereWithParame(element, sql_where,
					parames);

			Iterator<Element> condtionElement = condition.keySet().iterator();
			while (condtionElement.hasNext()) {
				Element prop = condtionElement.next();
				if (flag == 0)
					sql = sql + " " + prop.attributeValue("column") + "=? and ";
				else
					sql = sql + " " + prop.attributeValue("column") + "=? or ";

			}
			if (flag == 0)
				sql = sql.substring(0, sql.lastIndexOf("and"));
			else
				sql = sql.substring(0, sql.lastIndexOf("or"));

		}

		sql = createOrderBySql(OrderBy, sql, element);
		sql = sql + " limit " + firstIndex + "," + maxResult;
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
		if (condition != null) {
			Iterator<Element> condtionElement = condition.keySet().iterator();
			int i = 1;
			while (condtionElement.hasNext()) {
				Element prop = condtionElement.next();
				try {
					setPreparedStatementByPropertieType(pstmt, i,
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
			result = this.getObjectByResultSet(set, element, t);
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
			try {
				ResultSet ResultSet = pstmt
						.executeQuery("select count(*) from " + tableName);
				if (ResultSet.next()) {
					count = ResultSet.getInt(1);
				}
				queryResult = new QueryResult<>();
				queryResult.setResults(result);
				queryResult.setTotalCount(count);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		DBUtil.close(conn, pstmt);

		// 计算总数结束
		return queryResult;
	}

	/**
	 * 通过ResultSet 又称游标 拿到你需要的实体类 参数: resultSet 游标 classElement
	 * 是你需要传过来的类在xml中对于的classElement 可以为null
	 * 
	 * @throws SQLException
	 *             测试通过
	 * */
	private <T> List<T> getObjectByResultSet(ResultSet resultSet,
			Element classElement, Class<T> t) throws SQLException {
		if (classElement == null)
			classElement = ReadXmlUtil.getClassElement("EntityTable.xml", t);

		List<T> objects = new ArrayList<T>();

		// ---生成实体类开始
		while (resultSet.next()) {

			Map<String, Object> fieldsValue = new HashMap<String, Object>();

			for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {

				fieldsValue.put(resultSet.getMetaData().getColumnName(i + 1),
						resultSet.getObject(i + 1));
			}
			T object = null;
			object = setAllFieldsValue(classElement, t, fieldsValue);
			objects.add(object);
		}
		// ----生成实体类结束
		return objects;
	}

	/**
	 * 通过预处理匹配参数
	 * 
	 * @throws SQLException
	 * 
	 *             测试通过
	 * */

	private void setPreparedStatementByPropertieType(PreparedStatement pstmt,
			int index, String type, Object parameter) throws SQLException {
		switch (type) {
		case "Integer":
			pstmt.setInt(index, (int) parameter);
			break;
		case "Long":
			pstmt.setLong(index, (int) (parameter));
			break;
		case "String":
			pstmt.setString(index, String.valueOf(parameter));
			break;
		case "Short":
			pstmt.setShort(index, (short) parameter);
			break;
		case "Date": {
			java.util.Date time = (Date) parameter;

			pstmt.setDate(index, new java.sql.Date(time.getTime()));
		}
			break;
		case "Boolean":
			pstmt.setBoolean(index, Boolean.parseBoolean((String) parameter));
			break;
		case "Float":
			pstmt.setFloat(index, (float) parameter);
			break;
		case "Double":
			pstmt.setDouble(index, (double) parameter);
			break;
		}
	}

	/**
	 * 拿到对象所有成員變量和的值 参数数需要拿到数据的对象 返回只是成员变量名和值
	 * 
	 * 测试通过
	 * **/
	private Map<String, Object> getAllFieldsValue(Object object) {
		Map<String, Object> properties = new HashMap<String, Object>();
		Field[] fields = object.getClass().getDeclaredFields();
		// ---依次保存成员变量的名字和值 开始
		for (int i = 0; i < fields.length; i++) { // 將object中的成員變量值通過反射存儲
			try {
				fields[i].setAccessible(true);
				properties.put(fields[i].getName(), fields[i].get(object));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		// ----保存结束
		return properties;
	}

	/**
	 * 通过resultSet拿到的列名和值来实例化对象并设值 测试通过
	 * **/
	private <T> T setAllFieldsValue(Element classElement, Class<T> t,
			Map<String, Object> fieldsValue) {
		// --判断classElement是不是为null
		if (classElement == null) {
			classElement = ReadXmlUtil.getClassElement("EntityTable.xml", t);
		}
		// --判断处理结束

		// 实例化一个对象开始
		T entity = null;
		try {
			entity = t.newInstance();
		} catch (InstantiationException e) {
			System.out.println("实例化失败 ");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("实例化失败 ");
			e.printStackTrace();
		}

		// 开始对entity赋值

		for (Iterator<String> i = fieldsValue.keySet().iterator(); i.hasNext();) {
			String coulmn = i.next();

			Iterator<Element> classIterator = classElement.elementIterator();
			while (classIterator.hasNext()) {
				Element prop = classIterator.next();
				if (prop.attributeValue("column").equals(coulmn)) {
					Field field;
					try {
						field = entity.getClass().getDeclaredField(
								prop.attributeValue("name"));
						field.setAccessible(true);

						if (prop.attribute("className") != null && fieldsValue
									.get(prop.attributeValue("column"))!=null) {
							// 对外键对象赋值
							Class<?> father = Class.forName(prop
									.attributeValue("className"));

							Object c = null;
							c = father.newInstance();

							Field id = c.getClass().getDeclaredField("id");
							id.setAccessible(true);
							// 从数据库中传来的对象为BigDecimal时需要转型为double或者为float
							if ("java.math.BigDecimal".equals(fieldsValue
									.get(prop.attributeValue("column"))
									.getClass().getName())) {
								BigDecimal decimal = (BigDecimal) fieldsValue
										.get(prop.attributeValue("column"));
								if (prop.attributeValue("type")
										.equals("Double")) {
									id.set(c, decimal.doubleValue());
								} else if (prop.attributeValue("type").equals(
										"Float")) {
									id.set(c, decimal.floatValue());
								}

							} else {
								id.set(c, fieldsValue.get(prop
										.attributeValue("column")));
							}

							field.set(entity, c);

						} else  if(fieldsValue
									.get(prop.attributeValue("column"))!=null){
							// 非外键直接赋值
							// 从数据库中传来的对象为BigDecimal时需要转型为double或者为float

							if ("java.math.BigDecimal".equals(fieldsValue
									.get(prop.attributeValue("column"))
									.getClass().getName())) {
								BigDecimal decimal = (BigDecimal) fieldsValue
										.get(prop.attributeValue("column"));
								if (prop.attributeValue("type")
										.equals("Double")) {
									field.set(entity, decimal.doubleValue());
								} else if (prop.attributeValue("type").equals(
										"Float")) {
									field.set(entity, decimal.floatValue());
								}

							} else {
								field.set(entity, fieldsValue.get(prop
										.attributeValue("column")));
							}

						}

						break;
					} catch (NoSuchFieldException e) {
						System.out.println("没有这样的字段");
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					}

				}
			}
		}
		// ---结束赋值
		return entity;
	}

	/***
	 * 通过sql 语句来拿到类型 Map 是成员变量对应的 xml 的数据 和成员变量的值
	 * */
	private Map<Element, Object> interpretSqlWhereWithParame(Element element,
			String sql, List<Object> parames) {

		String[] sql_where = sql.split(",");

		Map<Element, Object> results = new HashMap<Element, Object>();

		for (int i = 0; i < sql_where.length; i++) {
			Element prop = getColumnByPropertiesnName(sql_where[i], element);
			results.put(prop, parames.get(i));
		}
		return results;
	}

	private Element getColumnByPropertiesnName(String propertiesName,
			Element element) {
		Iterator<Element> props = element.elementIterator();
		Element a = null;
		while (props.hasNext()) {
			Element prop = props.next();
			if (prop.attributeValue("name").equals(propertiesName)) {
				a = prop;
				break;
			}
		}
		return a;
	}

	private String createOrderBySql(Map<String, String> orderBy, String sql,
			Element element) {
		if (orderBy != null) {
			Iterator<String> iterator = orderBy.keySet().iterator();
			sql = sql + " order by ";
			while (iterator.hasNext()) {
				String key = iterator.next();
				sql = sql
						+ " "
						+ getColumnByPropertiesnName(key, element)
								.attributeValue("column") + " "
						+ orderBy.get(key) + " ,";
			}
			sql = sql.substring(0, sql.lastIndexOf(","));
		}

		return sql;
	}

	/*
	 * 为保存的方法创建sql 语句 sql="insert into t_work(" 不确定elementIterot是有序的遍历 所以我们用map
	 * 字段遍历 数据为空的不加入到sql 语句中 主键 外键注意判断 *
	 */
	private String createInsertSql(String sql, Map<String, Object> fieldsValue,
			Element classElement) {
		// id必须选拼接出来 如 insert into t_work(id,**,***,**) values(null
		Element primaryKey = getColumnByPropertiesnName("id", classElement);
		Boolean idIncrement = Boolean.parseBoolean(primaryKey
				.attributeValue("auto_increment"));
		// 对主键做一个特殊的操作,注意默认放在最前面
		sql = sql + primaryKey.attributeValue("column");
		// 现在拿的是非主键和外键的
		int countColumn = 0;
		Iterator<String> iterator = fieldsValue.keySet().iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			// 我们插入的数据都是要非空的
			if (fieldsValue.get(name) != null) {
				Element prop = getColumnByPropertiesnName(name, classElement);
				// 为非主外键
				if (prop != null
						&& (prop.attribute("key") == null || prop
								.attributeValue("key").equals("foreign"))) {
					sql = sql + "," + prop.attributeValue("column");
					countColumn++;
				}
			}
		}

		sql = sql + ") values(";
		if (idIncrement == true) {
			sql = sql + "null";
		} else {
			sql = sql + "?";
		}

		// 把后面的问号加上去
		for (int i = 0; i < countColumn; i++) {
			sql = sql + ",?";
		}

		return sql + ")";
	}

	/**
	 * 更新的方法是 先通过fieldsValue 拿到所有非空的数据 在去跌代数据并判断是否是可更新的，需要将外键独立开来（通过判断也是可以的）
	 * 
	 * */
	private String createUpdateBySql(String sql,
			Map<String, Object> fieldsValue, Element classElement) {
		Iterator<String> fieldsIterator = fieldsValue.keySet().iterator();
		Boolean flag = false; // 标记是否有可更新项
		while (fieldsIterator.hasNext()) {
			String fieldName = fieldsIterator.next();
			if (fieldsValue.get(fieldName) != null) {
				Element element = getColumnByPropertiesnName(fieldName,
						classElement);
				if (element.attribute("update") == null
						|| element.attributeValue("update").equals("true")) {
					sql = sql + " " + element.attributeValue("column")
							+ "=? , ";
					flag = true;
				}
			}
		}
		if (flag == true) {
			sql = sql.substring(0, sql.lastIndexOf(',')) + " where ";
			Element id = getColumnByPropertiesnName("id", classElement);
			sql = sql + id.attributeValue("column") + "=?";
			return sql;
		} else {
			return null;
		}
	}

	@Override
	public <T> List<T> findAllEntityByCompose(Class<T> t, int firstIndex,
			int maxResult, String sql, List<Object> parames) {
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
					this.setPreparedStatementByPropertieType(pstmt, i, key.getClass().getSimpleName(),
							key);
					i++;
				} catch (SQLException e) {
					e.printStackTrace();
					throw new DBException("抱歉，系统异常");
				}
			}
		}

		// --预处理结束
		// 游标开始
		Element element = ReadXmlUtil.getClassElement("EntityTable.xml", t);
		ResultSet set;
		List<T> result = null;
		try {
			set = pstmt.executeQuery();
			result = this.getObjectByResultSet(set, element, t);
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
	public int getAllCount(String sql, List<Object> parames) {
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
					this.setPreparedStatementByPropertieType(pstmt, i, key.getClass().getSimpleName(),
							key);
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

}
