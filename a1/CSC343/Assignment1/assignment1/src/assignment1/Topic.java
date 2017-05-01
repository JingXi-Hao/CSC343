package assignment1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Topic {

	private String name, faculty_code;
	private int course_code;
	private int bf, af;
	private String username;
	
	PreparedStatement stmt = null;
	
	private String insert = "insert into evaluates_topic values(?,?,?,?)";
	private String update = "update evaluates_topic set before_mark=?, after_mark=? where student=? and topic=?";
	
	private boolean is_rated(Connection conn) throws SQLException {
		Statement st = null;
		String query = "select * from evaluates_topic where student=\""+this.username+"\" and topic=\""+this.name+"\" and before_mark=\"" + this.bf + "\" and after_mark=\"" + this.af + "\"";
		
		try{
			st=conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (!rs.next()) {return false;}
			return true;
		} catch (SQLException e) {
			SQLError.show(e);
		} finally {
			if (st != null) {st.close();}
		}
		return true;
	}
	
	public void enterTopic(Connection conn) throws SQLException {
		if (!this.validate()){
			System.out.println("Fields not set.");
			System.exit(1);
		}
		
		if (this.is_rated(conn)){
			try {
				stmt = conn.prepareStatement(update);
				stmt.setInt(1, this.bf);
				stmt.setInt(2, this.af);
				stmt.setString(3, this.username);
				stmt.setString(4, this.name);
				stmt.execute();
			}catch (SQLException e) {
				SQLError.show(e);
			} finally {
				if (stmt != null) {stmt.close();}
			}
		} else {
			try {
				stmt = conn.prepareStatement(insert);
				stmt.setString(1, this.username);
				stmt.setString(2, this.name);
				stmt.setInt(3, this.bf);
				stmt.setInt(4, this.af);
				stmt.execute();
			} catch (SQLException e) {
				SQLError.show(e);
			} finally {
				if (stmt != null) {stmt.close();}
			}
		}
	}
	
	private boolean validate() {
		if (bf <1 || bf >5)
			return false;
		if (af <1 || af >5)
			return false;
		return true;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getFaculty_code() {
		return faculty_code;
	}



	public void setFaculty_code(String faculty_code) {
		this.faculty_code = faculty_code;
	}



	public int getCourse_code() {
		return course_code;
	}



	public void setCourse_code(int course_code) {
		this.course_code = course_code;
	}



	public int getBf() {
		return bf;
	}



	public void setBf(int bf) {
		this.bf = bf;
	}



	public int getAf() {
		return af;
	}



	public void setAf(int af) {
		this.af = af;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
