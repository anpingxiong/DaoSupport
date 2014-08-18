DaoSupport（支持xml配置和注解配置）
==========

java Dao(data access object)封装 简单方便 节省时间

开源供有兴趣者一起学习


为什么要封装Dao??
=================

*    避免在编写Dao类时重复编写繁杂 sql 语句
*    避免在编写Dao类时小心翼翼预处理sql语句反而还会出现错误
*    避免在编写Dao类时从游标ResultSet中获取数据构建对象是出现错误
*    避免忘记关闭游标
*    等等


总而言之，以前需要做Dao的事情，现在DaoSupport的安全完整的帮你做了。
==================================================================

详细介绍
-------
*    使用方式介绍在./DaoSupport/README.md中查看


简单入门
--------
*    在项目的classpath下新建配置 EntityTable.xml，可以参见 ./xml配置版 EntityTable,xml配置模板 
*    只需要在你需要的地方，比如service层调用EntityDao dao = EntityDaoImpl.getInstance();获取EntityDao对象
*    通过查看EntityDao接口类方法注释中查看，里面有示例。比如添加用户对象 ,User user=new User(); dao.save(user);
