package csc343assignment3;
import java.sql.*;

public class Skill {
	
	private Integer skill_id;
	private String skill;

	PreparedStatement stmt = null;
	
	private String insert = "insert into skills values (?,?)";
	
	public static int getID (Connection conn) throws SQLException
	{
		Statement st = null;
		try
		{
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("select max(skill_id) from skills");
			if (rs.next()) {return rs.getInt(1);}
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (st != null) {st.close();}}
		return 0;
		
	}

	
	public void insert_skill (Connection conn, String name) throws SQLException
	{
		this.skill_id = getID(conn) + 1;
		this.skill = name;
		try
		{
			stmt = conn.prepareStatement(insert);
			stmt.setInt(1, skill_id);
			stmt.setString(2, skill);
			stmt.execute();
		}  catch (SQLException e) {SQLError.show(e);}
		finally {if (stmt != null) {stmt.close();}}	
	}
}
