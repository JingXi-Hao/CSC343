package csc343assignment3;
import java.util.*;
import java.sql.*;

public class Edition {
	
	
	private Integer edition_id;
	private String username;
	private String letterGrade;
	private Integer course_ranking;
	private Integer instr_ranking;
	private String update = 
			"update enrollments set letterGrade=?,"
			+ " course_ranking=?, "
			+ "instr_ranking=? "
			+ "where eidtion_id=? and username=?";
	
	private String insert =
			"insert into enrollments "
			+ "values(?,?,?,?,?)";
	
	PreparedStatement stmt = null;
	
	
	private boolean isEnrol(Connection conn) throws SQLException
	{
		Statement st = null;
		String query = "select * from enrollments where edition_id = "
				+ "\'" + this.edition_id + "\'" 
				+ "and username = " + "\'" + this.username + "\'";
		try 
		{
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (!rs.next()) {return false;}
			return true;
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (st!=null) {st.close();}}
		
		return true;
	}
	

	public void enterExp (Connection conn) throws SQLException
	{
		
		if (!this.validate())
		{
			System.out.print("Fields not set");
			System.exit(1);
		}
		
		if (this.isEnrol(conn))
		{
			try
			{
				stmt = conn.prepareStatement(update);
				stmt.setString(1, this.letterGrade);
				stmt.setInt(2, this.course_ranking);
				stmt.setInt(3, this.instr_ranking);
				stmt.setInt(4, this.edition_id);
				stmt.setString(5, this.username);
				
				stmt.execute();
				
			}
			catch(SQLException e) {SQLError.show(e);}
			finally {if (stmt != null) {stmt.close();}}
		}
		else
		{
			try
			{
				stmt = conn.prepareStatement(insert);
				stmt.setInt(1, this.edition_id);
				stmt.setString(2,  this.username);
				stmt.setString(3,  this.letterGrade);
				stmt.setInt(4, this.course_ranking);
				stmt.setInt(5,  this.instr_ranking);
				
				stmt.execute();
			}
			catch(SQLException e) {SQLError.show(e);}
			finally {if (stmt != null) {stmt.close();}}
		}
	}
	
	private boolean validate() 
	{
		Set<String> grades = new HashSet<String>(
				Arrays.asList(new String [] {"A", "B", "C", "D", "F"}));
		
		if (!grades.contains(this.letterGrade)) {
			System.out.println("letter grade error");
			return false;
			}
		if (this.course_ranking > 5 || this.course_ranking < 1) {
			System.out.println("course_rank error");
			return false;
			}
		if (this.instr_ranking > 5 || this.instr_ranking < 1) {
			System.out.println("instr_ranking error");
			return false;
			}
		
		return true;
	}

	public static int getID (Connection conn) throws SQLException
	{
		Statement st = null;
		try
		{
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("select max(course_id) from courses");
			if (rs.next()) {return rs.getInt(1);}
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (st != null) {st.close();}}
		return 1;
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


	public String getLetterGrade() {
		return letterGrade;
	}


	public void setLetterGrade(String letterGrade) {
		this.letterGrade = letterGrade;
	}


	public Integer getCourse_ranking() {
		return course_ranking;
	}


	public void setCourse_ranking(Integer course_ranking) {
		this.course_ranking = course_ranking;
	}


	public Integer getInstr_ranking() {
		return instr_ranking;
	}


	public void setInstr_ranking(Integer instr_ranking) {
		this.instr_ranking = instr_ranking;
	}
	
	
}
