package com.daoSupport.daoHelper;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

public class EntitySaveHelper {
	/*
	 * 为保存的方法创建sql 语句 sql="insert into t_work(" 不确定elementIterot是有序的遍历 所以我们用map
	 * 字段遍历 数据为空的不加入到sql 语句中 主键 外键注意判断 *
	 */
	public static String createInsertSql(String sql, Map<String, Object> fieldsValue,
			Element classElement) {
		// id必须选拼接出来 如 insert into t_work(id,**,***,**) values(null
		Element primaryKey = EntityUtilHelper.getColumnByPropertiesnName("id", classElement);
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
				Element prop = EntityUtilHelper.getColumnByPropertiesnName(name, classElement);
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

}
