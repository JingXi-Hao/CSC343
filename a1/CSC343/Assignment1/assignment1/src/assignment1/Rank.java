package assignment1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Rank {
	private String student, prof;
	private int mark;
	
	private String insert = "insert into rank values(?,?,?)";
	private String update = "update rank set mark=? where student=? and instructor=?";
	
	PreparedStatement stmt = null;
	
	private boolean isRanked(Connection conn) throws SQLException {
		Statement st = null;
		String query = "select * from rank where student=\"" + this.student + "\" and instructor=\""+this.prof+"\"";
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (!rs.next()) {return false;}
		} catch (SQLException e) {
			SQLError.show(e);
		} finally {
			if (st != null) {st.close();}
		}
		return true;
	}
	
	public void enterRank(Connection conn) throws SQLException {
		if (!this.validate()){
			System.out.print("Fileds not set.");
			System.exit(1);
		}
		
		if (this.isRanked(conn)) {
			try {
				stmt = conn.prepareStatement(update);
				stmt.setInt(1, this.mark);
				stmt.setString(2, this.student);
				stmt.setString(3, this.prof);
				stmt.execute();
			}catch (SQLException e) {
				SQLError.show(e);
			} finally {
				if (stmt != null) {stmt.close();}
			}
		} else {
			try {
				stmt = conn.prepareStatement(insert);
				stmt.setString(1, this.student);
				stmt.setString(2, this.prof);
				stmt.setInt(3, this.mark);
				stmt.execute();
			}catch (SQLException e) {
				SQLError.show(e);
			} finally {
				if (stmt != null) {stmt.close();}
			}
			}
	}
	
	private boolean validate(){
		if (this.mark < 1 || this.mark > 5)
			return false;
		return true;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public String getProf() {
		return prof;
	}

	public void setProf(String prof) {
		this.prof = prof;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}
	
	
}
