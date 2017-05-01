package assignment1;
import java.util.*;
import java.io.*;
import java.sql.*;

public class DBConnection {
	private static String driver1 = "org.sqlite.JDBC";
	private static String url1 = "jdbc:sqlite:cea";
	
	private static String driver2 = "org.postgresql.Driver";
	private static String url2 = "jdbc:postgresql://localhost:5432/csc343h-c5caoye";
	
	
	
	public static final String PROGRAM_NAME = "DBConnection";
	public static Connection getConnection(Properties props, int c) {
		String driver, url;
		
		if (c == 1){
			driver = driver1;
			url = url1;
		} else {
			driver = driver2;
			url = url2;
		}
		
		if (driver == null || url == null) {
			System.out.println ("No JDBC driver or ab URL specified in properties");
			return null;
		}
		String user = props.getProperty("user", "c5caoye");
		String password = props.getProperty("password", "cy13889613682");
		
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException ce) {
			System.out.println("JDBC Driver not found");
			return null;
		} catch (SQLException ex) {
			javax.swing.JOptionPane.showMessageDialog(null,ex, "Failed to connect with database", javax.swing.JOptionPane.ERROR_MESSAGE);
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
}

