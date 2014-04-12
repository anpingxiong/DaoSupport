package com.daoSupport.daoHelper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.daoSupport.Log.DaoSupportLog;
import com.daoSupport.util.ReadXmlUtil;

public class EntitySearchHelper {
	/***
	 * 通过sql 语句来拿到类型 Map 是成员变量对应的 xml 的数据 和成员变量的值
	 * */
	public static Map<Element, Object> interpretSqlWhereWithParame(Element element,
			String sql, List<Object> parames) {

		String[] sql_where = sql.split(",");

		Map<Element, Object> results = new HashMap<Element, Object>();

		for (int i = 0; i < sql_where.length; i++) {
			Element prop = EntityUtilHelper.getColumnByPropertiesnName(sql_where[i], element);
			results.put(prop, parames.get(i));
		}
		return results;
	}
	
	public static String createOrderBySql(Map<String, String> orderBy, String sql,
			Element element) {
		if (orderBy != null) {
			Iterator<String> iterator = orderBy.keySet().iterator();
			sql = sql + " order by ";
			while (iterator.hasNext()) {
				String key = iterator.next();
				sql = sql
						+ " "
						+ EntityUtilHelper.getColumnByPropertiesnName(key, element)
								.attributeValue("column") + " "
						+ orderBy.get(key) + " ,";
			}
			sql = sql.substring(0, sql.lastIndexOf(","));
		}

		return sql;
	}
	
	/**
	 * 通过ResultSet 又称游标 拿到你需要的实体类 参数: resultSet 游标 classElement
	 * 是你需要传过来的类在xml中对于的classElement 可以为null
	 * 
	 * @throws SQLException
	 *             测试通过
	 * */
	public static  <T> List<T> getObjectByResultSet(ResultSet resultSet,
			Element classElement, Class<T> t) throws SQLException {
		if (classElement == null)
			classElement = ReadXmlUtil.getClassElement(t);

		List<T> objects = new ArrayList<T>();

		// ---生成实体类开始
		while (resultSet.next()) {

			Map<String, Object> fieldsValue = new HashMap<String, Object>();

			for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {

				fieldsValue.put(resultSet.getMetaData().getColumnLabel(i + 1),
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
	 * 通过resultSet拿到的列名和值来实例化对象并设值 测试通过
	 * **/
	 public static  <T> T setAllFieldsValue(Element classElement, Class<T> t,
			Map<String, Object> fieldsValue) {
		// --判断classElement是不是为null
		if (classElement == null) {
			classElement = ReadXmlUtil.getClassElement(t);
		}
		// --判断处理结束

		// 实例化一个对象开始
		T entity = null;
		try {
			entity = t.newInstance();
		} catch (InstantiationException e) {
		 
			DaoSupportLog.getLogger().warning(t.getName()+"实例化失败 ");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
		
			DaoSupportLog.getLogger().warning(t.getName()+"实例化失败 ");
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

						if (prop.attribute("className") != null
								&& fieldsValue.get(prop
										.attributeValue("column")) != null) {
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

						} else {
							// 非外键直接赋值
							// 从数据库中传来的对象为BigDecimal时需要转型为double或者为float

							if (fieldsValue.get(prop.attributeValue("column")) != null) {
								if ("java.math.BigDecimal".equals(fieldsValue
										.get(prop.attributeValue("column"))
										.getClass().getName())) {
									BigDecimal decimal = (BigDecimal) fieldsValue
											.get(prop.attributeValue("column"));
									if (prop.attributeValue("type").equals(
											"Double")) {
										field.set(entity, decimal.doubleValue());
									} else if (prop.attributeValue("type")
											.equals("Float")) {
										field.set(entity, decimal.floatValue());
									}

								} else {
									field.set(entity, fieldsValue.get(prop
											.attributeValue("column")));
								}

							}
						}
						break;
					} catch (NoSuchFieldException e) {
						 
						DaoSupportLog.getLogger().warning(t.getName()+"没有字段"+coulmn+"请查看配置文件或者注解是否有误");
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


}
