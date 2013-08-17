package com.project.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

 

public class DBUtil {
  public DBUtil(){}
  static {
	  try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
  }

    public static Connection  getconn(){
		Connection con=null;
		try {
			con=DriverManager.getConnection("jdbc:mysql:///test","root","root");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("aaaa");
		}
		return con;
    	
    }
    public static  PreparedStatement getstst(Connection conn,String sql) throws SQLException{
    	PreparedStatement sts=null;
 		sts=(PreparedStatement) conn.prepareStatement(sql);
 
	return sts;
}
 public static Statement getstst(Connection conn){
	 Statement sts=null;
	 try {
		 sts=conn.createStatement();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return sts;
 }
 public static ResultSet  getrest(Statement sts,String sql) throws SQLException{
	 ResultSet rs=null;
	 	rs=sts.executeQuery(sql);
	 
	 return rs;
	 
 }
  public static void close(Connection conn,Statement sts)
  {
	  if(conn!=null){
		  try {
			conn.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		  conn=null;
	  }
	  if(sts!=null){
		  try {
			sts.close();
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		  sts=null;
	  }
  }
}

