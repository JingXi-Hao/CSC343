package csc343assignment3;

import java.util.*;
import java.io.*;
import java.sql.*;

public class DBConnection {
	private static String driver = "org.sqlite.JDBC";
	private static String url	 = "jdbc:sqlite:C:\\Users\\cy804\\workspace\\csc343assignment3\\cea";
	
	public static final String PROGRAM_NAME = "DBConnection";
	
	public static Connection getConnection(Properties props) {
		
		//String user 	= props.getProperty("user", "c5caoye");
		//String password = props.getProperty("password", "cy13889613682");
	
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url);
		} catch (ClassNotFoundException ce) {
			System.out.println("JDBC Driver not found");
			return null;
		} catch (SQLException ex){
			javax.swing.JOptionPane.showMessageDialog(null, ex, "Failed to connect with database", javax.swing.JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
	}
	
	public static void closeConnection (java.sql.Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			}
			catch(SQLException e) {
				SQLError.print(e);
			} 
		}
	}
	
	   public static void main (String [] args) throws IOException, SQLException  {
	    	Properties props = new Properties();
	    	
	    	Connection conn = getConnection (props);
	    	if (conn == null) {
	    		System.out.println("DB connection error");
	    	}
	    	else {
	    		System.out.println("Yes! connection works");
	    		conn.close();
	    	}
	    			
	    }
	
}
