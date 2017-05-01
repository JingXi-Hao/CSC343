package csc343assignment3;
import java.sql.*;

public class Skill_ex {
	private Integer course_id;
	private Integer edition_id;
	private String username;
	private Integer skill_id;
	private Integer rank_before;
	private Integer rank_after;
	
	PreparedStatement stmt = null;
	
	private String check =
			"select * from skill_rankings where username = ? and edition_id = ? and skill_id = ?";
	private String insert =
			"insert into skill_rankings values (?,?,?,?,?,?)";
	private String update = 
			"update skill_rankings set rank_before = ?, rank_after = ? "
			+ "where username = ? and edition_id = ? and skill_id = ?";
	
	public void enterSkill(Connection conn) throws SQLException
	{
		if (validate()) {
			if (check(conn)) {
				try {
					stmt = conn.prepareStatement(insert);
					stmt.setInt(1, course_id);
					stmt.setInt(2, edition_id);
					stmt.setString(3, username);
					stmt.setInt(4, skill_id);
					stmt.setInt(5, rank_before);
					stmt.setInt(6, rank_after);

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
					stmt.setInt(1, rank_before);
					stmt.setInt(2, rank_after);
					stmt.setString(3, username);
					stmt.setInt(4, edition_id);
					stmt.setInt(5, skill_id);
					stmt.execute();
				} catch (SQLException e) {
					SQLError.show(e);
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}

			} 
		} else {
			System.out.println("Fields not set");
		}
	}
	
	private boolean check (Connection conn) throws SQLException
	{
		try {
			stmt = conn.prepareStatement(check);
			stmt.setString(1, username);
			stmt.setInt(2, edition_id);
			stmt.setInt(3, skill_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {return false;}
			return true;
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (stmt!=null) {stmt.close();}}
		
		return true;
	}
	
	
	private boolean validate()
	{
		if (rank_before < 1 || rank_before > 5) {return false;}
		if (rank_after < 1 || rank_after > 5) {return false;}
		if (rank_after < rank_before) {return false;}
		return true;
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

	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}

	public Integer getRank_before() {
		return rank_before;
	}

	public void setRank_before(Integer rank_before) {
		this.rank_before = rank_before;
	}

	public Integer getRank_after() {
		return rank_after;
	}

	public void setRank_after(Integer rank_after) {
		this.rank_after = rank_after;
	}
	
	
	
}
