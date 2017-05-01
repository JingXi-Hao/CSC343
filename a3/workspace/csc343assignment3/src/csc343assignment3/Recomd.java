package csc343assignment3;
import java.util.*;
import java.sql.*;

public class Recomd {
	private List<String> student_names = new ArrayList<String>();
	private List<Student> students = new ArrayList<Student>();
//	private List<Integer> student_dis = new ArrayList<Integer>();
	private List<Student> resultSet = new ArrayList<Student>();
	
	private PreparedStatement stmt = null;
	
	/*
	 * Store all usernames into students_names
	 */
	private void getStudents(Connection conn) throws SQLException
	{
		Statement st = null;
		String query = "select username from students";
		
		try
		{
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			int cols = rs.getMetaData().getColumnCount();
			
			while (rs.next())
			{
				for (int i=0; i<cols; i++) 
				{
					student_names.add((String) rs.getObject(i+1)); 
				}
			}
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (st!=null) {st.close();}}
	}
	
	/*
	 * Hint Step 2
	 */
	public void SetUpStudents (Connection conn, User student) throws SQLException
	{
		this.getStudents(conn);
		//System.out.println(this.student_names);
		
		for (int i=0; i<student_names.size(); i++)
		{
			Student s = new Student();
			//System.out.println(student_names.get(i));
			s.setUp(student_names.get(i), conn, student);
			students.add(s);
			
			//System.out.println(student.getNum_gender());
			
			double d = 0;
			d += student.getAge() - s.getAge();
			d += student.getNum_gender() - s.getGender();
			if (!s.getNative_country().equals(student.getNative_country())) {d++;}
			
			for (int j=0; i<student.getInterest_in().size(); i++)
			{
				int k = s.getTopic_set().indexOf(student.getInterest_in().get(j));
				if (k > 0) {d += student.getInterest_mark().get(i) - s.getTopic_mark().get(k);}
				else {d += 5;}
			}
			
			for (int j=0; j<student.getSkill_in().size(); j++)
			{
				int k = s.getSkill_set().indexOf(student.getSkill_in().get(j));
				if (k>0) {d += student.getSkill_mark().get(j) - s.getSkill_mark().get(k);}
				else {d += 5;}
			}
			
			s.setDistance(d);
		}
		
		if (this.resultSet.contains(student.getUsername()))
		{this.resultSet.remove(student.getUsername());}
		
		for (int i=0; i<15; i++) 
		{
			this.resultSet.add(this.popMin());
		}

	}
	
	/*
	 * Print recommanding classes for user that already in DB
	 * order by grade.
	 */
	public void printRec_grade (Connection conn, User user) throws SQLException 
	{	
		List<String> names = new ArrayList<String>();
		String nb = "";
		for (Student s : this.resultSet) {names.add(s.getUsername());}
		if (!names.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < names.size(); i++) {builder.append(",?");}
			nb = builder.deleteCharAt(0).toString();
		}
		
		String c = "";
		List<Integer> courseTaken = user.getCourse_taken();
		if (!courseTaken.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < courseTaken.size(); i++) {builder.append(",?");}
			c = builder.deleteCharAt(0).toString();
		}
		
		String sql = "select course_id, course, average_grade, "
				+ "case when 90 <= average_grade and average_grade >= 100 then 'A+'"
				+ " when 85 <= average_grade and average_grade >= 89 then 'A' "
				+ "when 80 <= average_grade and average_grade >= 84 then 'A-' "
				+ "when 77 <= average_grade and average_grade >= 79 then 'B+' "
				+ "when 73 <= average_grade and average_grade >= 76 then 'B' "
				+ "when 70 <= average_grade and average_grade >= 72 then 'B-' "
				+ "when 67 <= average_grade and average_grade >= 69 then 'C+' "
				+ "when 63 <= average_grade and average_grade >= 66 then 'C' "
				+ "when 60 <= average_grade and average_grade >= 62 then 'C-' "
				+ "when 57 <= average_grade and average_grade >= 59 then 'D+' "
				+ "when 53 <= average_grade and average_grade >= 56 then 'D' "
				+ "when 50 <= average_grade and average_grade >= 52 then 'D-' "
				+ "else 'F' end as letter_grade from ( select course_editions.course_id as course_id, "
				+ "(dept_code || course_number) as course, avg(max_grade) as average_grade "
				+ "from enrollments, course_editions, courses, letter_grades where username in (" + nb + ") "
				+ "and course_editions.edition_id = enrollments.edition_id and courses.course_id = course_editions.course_id "
				+ "and letter_grades.letter_grade = enrollments.letter_grade and course_editions.course_id not in ( "+ c + ") "
				+ "group by course_editions.course_id order by average_grade desc) temp limit 5";
		
		try
		{
			stmt = conn.prepareStatement(sql);
			
			int i;
			for (i = 0; i < names.size(); i++) {stmt.setString(i + 1, names.get(i));}
			for (int j = 0; j < courseTaken.size(); j++) {stmt.setInt(i + j + 1, courseTaken.get(j));}
			
			ResultSet rs = stmt.executeQuery();
			int cols = rs.getMetaData().getColumnCount();
			while (rs.next())
			{
				for (int j=0; j<cols; j++) {System.out.print(rs.getObject(j+1) + "|");}
				System.out.println("\n");
			}
		} catch (SQLException e ) {
	    	SQLError.show(e);
		} finally {
		    if (stmt != null) { stmt.close(); }
		}
	}
	
	public void printRec_evaluation (Connection conn, User user) throws SQLException
	{
		List<String> names = new ArrayList<String>();
		String nb = "";
		for (Student s : this.resultSet) {names.add(s.getUsername());}
		
		if (!names.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < names.size(); i++) {builder.append(",?");}
			nb = builder.deleteCharAt(0).toString();
		}
		
		String c = "";
		List<Integer> courseTaken = user.getCourse_taken();
		if (!courseTaken.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < courseTaken.size(); i++) {builder.append(",?");}
			c = builder.deleteCharAt(0).toString();
		}
		
		String sql = "select course as BEST_RECOMMENDATIONS_BY_EVALUATION_SCORE "
				+ "from( select course_editions.course_id, (dept_code || course_number) as course, "
				+ "avg(course_ranking) as average_course_eval from enrollments, course_editions, courses where username in (" + nb + ") "
				+ "and course_editions.edition_id = enrollments.edition_id and courses.course_id = course_editions.course_id "
				+ "and course_editions.course_id not in (" + c + ") "
				+ "group by course_editions.course_id order by average_course_eval desc limit 5) temp";
		
		try
		{
			stmt = conn.prepareStatement(sql);
			
			int i;
			for (i = 0; i < names.size(); i++) {stmt.setString(i + 1, names.get(i));}
			for (int j = 0; j < courseTaken.size(); j++) {stmt.setInt(i + j + 1, courseTaken.get(j));}
			ResultSet rs = stmt.executeQuery();
			int cols = rs.getMetaData().getColumnCount();
			while (rs.next())
			{
				for (int j=0; j<cols; j++)
				{
					System.out.print(rs.getObject(j+1) + "|");
				}
				System.out.println("\n");
			}
		} catch (SQLException e ) {
	    	SQLError.show(e);
		} finally {
		    if (stmt != null) { stmt.close(); }
		}
	}
	
	public void printRec_interest (Connection conn, User user) throws SQLException
	{
		List<String> names = new ArrayList<String>();
		String nb = "";
		for (Student s : this.resultSet) {names.add(s.getUsername());}
		
		if (!names.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < names.size(); i++) {builder.append(",?");}
			nb = builder.deleteCharAt(0).toString();
		}
		
		String c = "";
		List<Integer> courseTaken = user.getCourse_taken();
		if (!courseTaken.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < courseTaken.size(); i++) {builder.append(",?");}
			c = builder.deleteCharAt(0).toString();
		}
		
		String t = "";
		List<Integer> topics = user.getInterest_in();
		if (!topics.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < topics.size(); i++) {builder.append(",?");}
			t = builder.deleteCharAt(0).toString();
		}
		
		String sql = "select course as BEST_RECOMMENDATIONS_BY_PROMOTING_INTERESTS from( "
				+ "select courses.course_id, (dept_code || course_number) as course, avg((interest_after - interest_before)) as avg_interest_increase "
				+ "from (select course_id,edition_id,username,topic_id,interest_before, "
				+ "case when interest_after is null then interest_before else interest_after end as interest_after "
				+ "from topic_interests) topic_interests, courses "
				+ "where courses.course_id = topic_interests.course_id "
				+ "and username in (" + nb + ") "
				+ "and topic_id in (" + t + ") and courses.course_id not in (" + c + ") "
				+ "group by courses.course_id order by avg_interest_increase desc limit 5) temp";
		
		try
		{
			stmt = conn.prepareStatement(sql);
			
			int i;
			for (i = 0; i < names.size(); i++) {stmt.setString(i + 1, names.get(i));}
			int k;
			for (k = 0; k < topics.size(); k++) {stmt.setInt(i + k + 1, topics.get(k));}
			for (int j = 0; j < courseTaken.size(); j++) {stmt.setInt(i + k + j + 1, courseTaken.get(j));}
			ResultSet rs = stmt.executeQuery();
			int cols = rs.getMetaData().getColumnCount();
			while (rs.next())
			{
				for (int j=0; j<cols; j++)
				{
					System.out.print(rs.getObject(j+1) + "|");
				}
				System.out.println("\n");
			}
		} catch (SQLException e ) {
	    	SQLError.show(e);
		} finally {
		    if (stmt != null) { stmt.close(); }
		}
	}
	
	public void printRec_skill (Connection conn, User user) throws SQLException
	{
		List<String> names = new ArrayList<String>();
		String nb = "";
		for (Student s : this.resultSet) {names.add(s.getUsername());}
		
		if (!names.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < names.size(); i++) {builder.append(",?");}
			nb = builder.deleteCharAt(0).toString();
		}
		
		String c = "";
		List<Integer> courseTaken = user.getCourse_taken();
		if (!courseTaken.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < courseTaken.size(); i++) {builder.append(",?");}
			c = builder.deleteCharAt(0).toString();
		}
		
		String s = "";
		List<Integer> skills = user.getSkill_in();
		if (!skills.isEmpty())
		{
			StringBuilder builder = new StringBuilder();
			for (@SuppressWarnings("unused") int i : skills) {builder.append(",?");}
			s = builder.deleteCharAt(0).toString();
		}
		
		String sql = "select course as BEST_RECOMMENDATIONS_BY_IMPROVING_SKILLS from( "
				+ "select username, skill_id, courses.course_id, "
				+ "(dept_code || course_number) as course, "
				+ "avg((rank_after - rank_before)) as avg_skill_increase "
				+ "from (select course_id,edition_id,username,skill_id,rank_before, "
				+ "case when rank_after is null then rank_before else rank_after end as rank_after "
				+ "from skill_rankings) skill_rankings, courses "
				+ "where courses.course_id = skill_rankings.course_id "
				+ "and username in (" + nb + ")"
				+ "and skill_id in (" + s + ")"
				+ "and courses.course_id not in (" + c + ") "
				+ "group by courses.course_id order by avg_skill_increase desc limit 5) temp";
		
		try
		{
			stmt = conn.prepareStatement(sql);
			
			int i;
			for (i = 0; i < names.size(); i++) {stmt.setString(i + 1, names.get(i));}
			int k;
			for (k = 0; k < skills.size(); k++) {stmt.setInt(i + k + 1, skills.get(k));}
			for (int j = 0; j < courseTaken.size(); j++) {stmt.setInt(i + k + j + 1, courseTaken.get(j));}
			ResultSet rs = stmt.executeQuery();
			int cols = rs.getMetaData().getColumnCount();
			while (rs.next())
			{
				for (int j=0; j<cols; j++)
				{
					System.out.print(rs.getObject(j+1) + "|");
				}
				System.out.println("\n");
			}
		} catch (SQLException e ) {
	    	SQLError.show(e);
		} finally {
		    if (stmt != null) { stmt.close(); }
		}
	}
	
	
	public List<Student> getResultSet() {
		return resultSet;
	}

	public void setResultSet(List<Student> resultSet) {
		this.resultSet = resultSet;
	}

	private Student popMin () 
	{
		if (students.isEmpty()) {return null;}
		
		int index = 0;
		double min = students.get(0).getDistance();
		
		for (Student s : students)
		{
			if (s.getDistance() < min) {index = students.indexOf(s);}
		}
		
		 Student result = students.get(index);
		 students.remove(index);
		 
		 return result;
	}
	
	
	
}
