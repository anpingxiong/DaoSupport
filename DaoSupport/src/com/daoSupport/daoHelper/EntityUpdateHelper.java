package com.daoSupport.daoHelper;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

public class EntityUpdateHelper {
	/**
	 * 更新的方法是 先通过fieldsValue 拿到所有非空的数据 在去跌代数据并判断是否是可更新的，需要将外键独立开来（通过判断也是可以的）
	 * 
	 * */
	public static String createUpdateBySql(String sql,
			Map<String, Object> fieldsValue, Element classElement) {
		Iterator<String> fieldsIterator = fieldsValue.keySet().iterator();
		Boolean flag = false; // 标记是否有可更新项
		while (fieldsIterator.hasNext()) {
			String fieldName = fieldsIterator.next();
			if (fieldsValue.get(fieldName) != null) {
				Element element = EntityUtilHelper.getColumnByPropertiesnName(fieldName,
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
			Element id = EntityUtilHelper.getColumnByPropertiesnName("id", classElement);
			sql = sql + id.attributeValue("column") + "=?";
			return sql;
		} else {
			return null;
		}
	}

}
