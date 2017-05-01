package csc343assignment3;
import java.sql.*;

public class Topic_ex {
	private Integer course_id;
	private Integer edition_id;
	private String username;
	private Integer topic_id;
	private Integer interest_before;
	private Integer interest_after;
	
	PreparedStatement stmt = null;
	
	private String check =
			"select * from topic_interests where username = ?, and edition_id = ? and topic_id = ?";
	private String insert =
			"insert into topic_interests values (?,?,?,?,?,?)";
	private String update =
			"update topic_interests set interest_before = ? , interest_after = ? "
			+ "where username = ?, and edition_id = ? and topic_id = ?";
	
	private boolean check (Connection conn) throws SQLException
	{
		try {
			stmt = conn.prepareStatement(check);
			stmt.setString(1, username);
			stmt.setInt(2, edition_id);
			stmt.setInt(3, topic_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {return false;}
			return true;
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (stmt!=null) {stmt.close();}}
		
		return true;
	}
	
	
	private boolean validate()
	{
		if (interest_before < 1 || interest_before > 5) {return false;}
		if (interest_after < 1 || interest_after > 5) {return false;}
		if (interest_after < interest_before) {return false;}
		return true;
	}
	
	
	public void enterSkill(Connection conn) throws SQLException
	{
		if (validate()) {
			if (check(conn)) {
				try {
					stmt = conn.prepareStatement(insert);
					stmt.setInt(1, course_id);
					stmt.setInt(2, edition_id);
					stmt.setString(3, username);
					stmt.setInt(4, topic_id);
					stmt.setInt(5, interest_before);
					stmt.setInt(6, interest_after);

					stmt.executeQuery();
				} catch (SQLException e) {
					SQLError.show(e);
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			}

			else {
				try {
					stmt = conn.prepareStatement(update);
					stmt.setInt(1, interest_before);
					stmt.setInt(2, interest_after);
					stmt.setString(3, username);
					stmt.setInt(4, edition_id);
					stmt.setInt(5, topic_id);
					stmt.execute();
				} catch (SQLException e) {
					SQLError.show(e);
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}

			} 
		} else {System.out.println("Fields not set");}
	}


	public Integer getCourse_id() {
		return course_id;
	}


	public void setCourse_id(Integer course_id) {
		this.course_id = course_id;
	}


	public Integer getEdition_id() {
		return edition_id;
	}


	public void setEdition_id(Integer edition_id) {
		this.edition_id = edition_id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public Integer getTopic_id() {
		return topic_id;
	}


	public void setTopic_id(Integer topic_id) {
		this.topic_id = topic_id;
	}


	public Integer getInterest_before() {
		return interest_before;
	}


	public void setInterest_before(Integer interest_before) {
		this.interest_before = interest_before;
	}


	public Integer getInterest_after() {
		return interest_after;
	}


	public void setInterest_after(Integer interest_after) {
		this.interest_after = interest_after;
	}
	
	
	
}
