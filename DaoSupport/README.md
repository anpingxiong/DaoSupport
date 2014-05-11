DaoSupport
==========
J2EE  Dao的支持方案，减轻小项目开发使用传统mvc时,dao重复且编写枯燥问题。
通过xml 和 java 的反射机制制作的 Dao封装类,在配好xml 之后只需要这一个Dao的实现类（EntityDaoImpl）就可以实现基本的对数据库的增删改查.


额外需要导入的包如下：
    
 	mysql的连接包：mysql-connector-java-5.1.13-bin.jar
 	解析xml 的包 ：dom4j-1.6.1.jar

问题：
	需要添加Vo的配置，和po的操作是一样的
 

 
 UPDATE LOG:
 	9月16 添加对 EntityDao的批量保存处理(预处理)
 	

    12月2号 除去了jdk7 switch(string)的特性
            添加java.util.logging,日志配置可配置在classpath中，但是名字必须为DaoSupport.properties，比如将日志信息输出到文件中。
            无配置就不需要去关注。
    12月17号 增加注解版本。（xml 可以参考src下的EntityTable.xml,并且开发人员在配置时必须也命名为EntityTable.xml）
             必须要写的注解有@EntityAnnotation(table="t_school")
                          @PrimaryKeyAnnotation(column="id")
                        如果有外键的必须：  @ForeignKeyAnnotation(column="sid",type="Integer")
                         
            1.开发人员可以使用xml 配置实体类和数据库表的对应
            2.开发者可以在实体类上添加注解类来让DaoSupport生成xml，自动的配置实体类和数据库表的对应
                 主要是四个注解：
                     1.@EntityAnnotation(table="t_school") 在类上添加表示实体类对数据库对应t_school表
                     2.@PrimaryKeyAnnotation(column="id",auto_increment=true,update=false) 在实体类成员变量id 上添加表示主键生成策略
                           @PrimaryKeyAnnotation(column="id") 表示自增和不可再次更新
                           @PrimaryKeyAnnotation(column="id",auto_increment=false,update=true)表示不自增并可以更新
                     3.@ForeignKeyAnnotation(column="sid",type="Integer") 在实体类对象引用成员变量上添加表示外键生策略，column表示在数据库中对应的字段，这里为id,对应数据库中的类型为int
                     4.@VariableAnnotation(column="workName")在非代表主外键的成员变量上添加，表示和数据库中表字段对应,如果和数据库名字一致可无需配置
                     
                     
    3月27号 将po 的基本数据类型默认以引用数据类型来处理，这样解决po属性如果为基本数据类型时默认有缺省值问题
                   
    4月17好 需要将主键名为id 写该为用户可自定义                 
                     
	  
                         
简单使用教程
------------
1.配置好EntityTable.xml:请参考jar中的文件
2.调用EntityDaoImpl类：
    如需要添加则EntityDaoImpl.getInstance().save(Object); //表示保存
   
             
