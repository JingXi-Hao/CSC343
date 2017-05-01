package csc343assignment3;
import java.sql.*;

public class Topic {

	private int topic_id;
	private String topic;
	
	PreparedStatement stmt = null;

	private String insert = "insert into topics values (?,?)";
	
	public static int getID (Connection conn) throws SQLException
	{
		Statement st = null;
	try{
		st = conn.createStatement();
		ResultSet rs = st.executeQuery("select max(topic_id) from topics");
		if (rs.next()) {return rs.getInt(1);}
	} catch (SQLException e) {SQLError.show(e);}
	finally {if (st != null) {st.close();}}
	return 0;
	}
	
	public void insert_topic (Connection conn, String name) throws SQLException
	{
		this.topic_id = getID(conn) + 1;
		this.topic = name;
		
		System.out.print(this.topic);
		
		try
		{
			stmt = conn.prepareStatement(insert);
			stmt.setInt(1, topic_id);
			stmt.setString(2, topic);
			stmt.execute();
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (stmt != null) {stmt.close();}}
	}
}
