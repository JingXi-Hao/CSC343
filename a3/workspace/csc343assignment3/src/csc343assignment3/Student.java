package csc343assignment3;
import java.util.*;
import java.sql.*;

public class Student {
	private String username;
	private double age;
	private double gender;
	private String native_country;
	private double distance;
	
	private List<Integer> skill_set = new ArrayList<Integer>(); // Stored skill ids.
	private List<Integer> skill_mark = new ArrayList<Integer>();
	private List<Integer> topic_set = new ArrayList<Integer>();
	private List<Integer> topic_mark = new ArrayList<Integer>();
	
//	private List<String> classes = new ArrayList<String>(); // Classes the student has taken.
//	private List<String> class_mark = new ArrayList<String>(); // Mark in letter grade.
	
	PreparedStatement stmt = null;
	
	
	/*
	 * set up all the infos. fill sets.
	 */
	public void setUp(String username, Connection conn, User user) throws SQLException
	{
		this.username = username;
		
		List<Integer> topics = user.getInterest_in();
		List<Integer> skills = user.getSkill_in();
		
		String q1Array = "";
		String q2Array = "";
		
		
		if (!skills.isEmpty()) 
		{
			StringBuilder skillBuilder = new StringBuilder();
			for (int i = 0; i < skills.size(); i++) {skillBuilder.append(",?");}
			
			q1Array = skillBuilder.deleteCharAt(0).toString();
		} 
		
		String q1 = 
				"select username, age, gender, native_country, skill_id, avg(rank_before) "
				+ "as avarage_skill_level"
				+ " from(select students.username as username, age, "
				+ "case when gender = \'m\' then 1 else 0 end as gender, native_country, "
				+ "skills.skill_id as skill_id, rank_before from students, skill_rankings, skills "
				+ "where students.username = skill_rankings.username and skills.skill_id = skill_rankings.skill_id "
				+ "group by students.username, age, gender, native_country, course_id, skills.skill_id) temp "
				+ "where username = ? "
				+ "and skill_id in (" + q1Array + ") "
				+ "group by username, age, gender, native_country, skill_id";
		
		StringBuilder topicBuilder = new StringBuilder();
		for (int i = 0; i < topics.size(); i++) {topicBuilder.append(",?");}
		
		if (!topics.isEmpty()) {
			StringBuilder skillBuilder = new StringBuilder();
			for (int i = 0; i < skills.size(); i++) {skillBuilder.append(",?");}
			
			q2Array = topicBuilder.deleteCharAt(0).toString();
		} 
		
		String q2 =
				"select username, age, gender, native_country, topic_id, avg(interest_before) "
				+ "as avarage_topic_level"
				+ " from(select students.username as username, age, "
				+ "case when gender = \'m\' then 1 else 0 end as gender, native_country, "
				+ "topics.topic_id as topic_id, interest_before from students,topic_interests, topics "
				+ "where students.username = topic_interests.username and topics.topic_id = topic_interests.topic_id "
				+ "group by students.username, age, gender, native_country, course_id, topics.topic_id) temp "
				+ "where username = ? "
				+ "and topic_id in (" + q2Array + ") " 
				+ "group by username, age, gender, native_country, topic_id";	
		
		try 
		{
			stmt = conn.prepareStatement("select * from students where username = ?");
			stmt.setString(1, this.username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) 
			{
				if (rs.getObject(3) == null) {this.age = 23.94373;}
				else {this.age = rs.getDouble(3);};
				
				if (rs.getObject(4) == null) {this.gender = 0.44;}
				else 
				{
					if (rs.getString(4).equals("m"))
					{
						this.gender = 1;
					} else {this.gender = 0;}
				}
				
				if (rs.getObject(5) == null) {this.native_country = "na";}
				else {this.native_country = rs.getString(5);}
			}
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (stmt!=null) {stmt.close();}}
		
		try
		{
			stmt = conn.prepareStatement(q1);
			stmt.setString(1, this.username);
			for (int i = 0; i < skills.size(); i++) {stmt.setInt(i + 2, skills.get(i));}
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
				this.skill_set.add(rs.getInt(5));
				this.skill_mark.add(rs.getInt(6));
			}
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (stmt!=null) {stmt.close();}}
		
		try
		{
			stmt = conn.prepareStatement(q2);
			stmt.setString(1, this.username);
			for (int i = 0; i < topics.size(); i++) {stmt.setInt(i + 2, topics.get(i));}
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
				this.topic_set.add(rs.getInt(5));
				this.topic_mark.add(6);
			}
		} catch (SQLException e) {SQLError.show(e);}
		finally {if (stmt!=null) {stmt.close();}}
		
	}
	
	public String getUsername() {
		return username;
	}

	public double getAge() {
		return age;
	}

	public String getNative_country() {
		return native_country;
	}

	public List<Integer> getSkill_set() {
		return skill_set;
	}

	public List<Integer> getSkill_mark() {
		return skill_mark;
	}

	public List<Integer> getTopic_set() {
		return topic_set;
	}

	public List<Integer> getTopic_mark() {
		return topic_mark;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getGender() {
		return gender;
	}
}
