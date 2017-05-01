package csc343assignment3;
import java.sql.*;
import java.util.*;

public class PrintTable {
	public static final String PROGRAM_NAME = "PrintTable";
	
	public static void print (Connection conn, String tblName) throws SQLException {
		Statement stmt = null;
		String query = "select * from " + tblName;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int cols = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i=0; i<cols; i++)
					System.out.print(rs.getObject(i+1)+"|");
				System.out.println("\n");
			}
		} catch (SQLException e ) {
	    	SQLError.show(e);
		} finally {
		    if (stmt != null) { stmt.close(); }
		}
	}
	
	
	public static void main(String[] args) throws SQLException
	{
		Properties props = new Properties();
		java.sql.Connection conn = DBConnection.getConnection(props);
		print(conn, "students");
	}
}