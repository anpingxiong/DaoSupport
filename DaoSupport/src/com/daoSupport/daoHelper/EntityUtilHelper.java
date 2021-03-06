package com.daoSupport.daoHelper;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

public class EntityUtilHelper {
	
	
	private final static int ShortType   =0;
	private final static int IntegerType =1;
	private final static int LongType    =2;
	private final static  int FloatType   =3;
	private final static int DoubleType  =4;
	private final static int BooleanType =5;
	private final static int StringType  =6;
	private final static int DateType    =7;
	/**
	 * 拿到对象所有成員變量和的值 参数数需要拿到数据的对象 返回只是成员变量名和值
	 * 
	 * 测试通过
	 * **/
	public static Map<String, Object> getAllFieldsValue(Object object) {
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
	 * 
	 * xionganping
	 * TODO 通过类的成员变量的名字来拿到DB中的列名
	 * @param propertiesName
	 * @param element
	 * @return
	 * @return_type:Element
	 *
	 */
	public static Element getColumnByPropertiesnName(String propertiesName,
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
	
	/**
	 * 通过预处理匹配参数
	 * 
	 * @throws SQLException
	 * 
	 *   测试通过
	 * */

	
	public static  void setPreparedStatementByPropertieType(PreparedStatement pstmt,
			int index, String type, Object parameter) throws SQLException {
		
		int changeTypeBeIntValue = changePropertiesTypeToInt(type);
		 
		switch (changeTypeBeIntValue) {
		case IntegerType:
			pstmt.setInt(index, (int) parameter);
			break;
		case LongType:
			pstmt.setLong(index, (int) (parameter));
			break;
		case StringType:
			pstmt.setString(index, String.valueOf(parameter));
			break;
		case ShortType:
			pstmt.setShort(index, (short) parameter);
			break;
		case DateType: {
			java.util.Date time = (Date) parameter;

			pstmt.setDate(index, new java.sql.Date(time.getTime()));
		}
			break;
		case BooleanType:
			pstmt.setBoolean(index, Boolean.parseBoolean((String) parameter));
			break;
		case FloatType:
			pstmt.setFloat(index, (float) parameter);
			break;
		case DoubleType:
			pstmt.setDouble(index, (double) parameter);
			break;
		default:
			System.out.println("请你在xml 中正确的配置属性的类型！");
		
		}
	}

	private  static int changePropertiesTypeToInt(String type){
		int result =-1;
		if(type.equals("Integer")){
			result = IntegerType;
		}else if(type.equals("Long")){
			result =LongType;
		}else if(type.equals("String")){
			result = StringType;
		}else if(type.equals("Short")){
			result = ShortType;
		}else if(type.equals("Date")){
			result = DateType;
		}else if(type.equals("Boolean")){
			result = BooleanType;
		}else if (type.equals("Float")){
			result = FloatType;
		}else if(type.equals("Double")){
			result = DoubleType;
		}
		return result;
	}
	 
}
