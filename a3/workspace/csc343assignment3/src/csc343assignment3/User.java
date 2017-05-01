package csc343assignment3;
import java.util.*;
import java.sql.*;

public class User 
{
	private static final String TABLE_NAME = "Student";
	
	private String username;
	private int    permission;
	private int    age;
	private String gender;
	//private Integer num_gender;
	private String native_country;
	private List<Integer> course_taken = new ArrayList<Integer>();
	private List<Integer> interest_in = new ArrayList<Integer>();
	private List<Integer> interest_mark = new ArrayList<Integer>();
	private List<Integer> topic_ids = new ArrayList<Integer>();
	private List<String> topic_names = new ArrayList<String>();
	private List<Integer> skill_ids = new ArrayList<Integer>();
	private List<String> skill_names = new ArrayList<String>();
	private List<Integer> skill_in = new ArrayList<Integer>();
	private List<Integer> skill_mark = new ArrayList<Integer>();
	
	private String insert 
		= "insert into students values(?,?,?,?,?)";
	
	PreparedStatement stmt = null;
	
	public void addto (Connection conn) throws SQLException 
	{
		
		if (!this.validate()) 
		{
			System.out.println("Student fields not set");
			System.exit(1);
		}
		
		try 
		{
			stmt = conn.prepareStatement(insert);
			stmt.setString(1, this.username);
			stmt.setInt(2, this.permission);
			stmt.setInt(3, this.age);
			stmt.setString(4, this.gender);
			stmt.setString(5, this.native_country);
			stmt.execute();
		} catch (SQLException e) 
		{
			SQLError.show(e);
		} finally 
		{
			if (stmt != null) {stmt.close();}
		}
	}
	
	public static void printInfo (Connection conn) throws SQLException 
	{
		PrintTable.print(conn, TABLE_NAME);
	}
	
	public void printInterest ()
	{
		Object [] topics = this.interest_in.toArray();
		Object [] marks  = this.interest_mark.toArray();
		int l = Math.min(topics.length, marks.length);
		
		List<Integer> sub1 = new ArrayList<Integer> (this.interest_in.subList(0, l));
		List<Integer> sub2 = new ArrayList<Integer> (this.interest_mark.subList(0, l));
		
		this.interest_in = sub1;
		this.interest_mark = sub2;
		
		for (int i=0; i<l; i++)
		{
			System.out.print(topics[i] + ":" + marks[i] + "\t");
			
		}
	}
	
	public void printSkillHave()
	{
		Object [] skills = this.skill_in.toArray();
		Object [] marks = this.skill_mark.toArray();
		int l = Math.min(skills.length, marks.length);
		
		List<Integer> sub1 = new ArrayList<Integer>(this.skill_in.subList(0, l));
		List<Integer> sub2 = new ArrayList<Integer>(this.skill_mark.subList(0, l));
		
		this.skill_in = sub1;
		this.skill_mark = sub2;
		
		for (int i = 0; i < l; i++) 
		{
			System.out.print(skills[i] + ":" + marks[i] + "\t");
		}
	}
	
	public void setUp (Connection conn) throws SQLException
	{
		getUserCourseInDB(conn);
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from students where username = \'"
				+ this.username + "\'");
		while (rs.next())
		{
			this.permission = rs.getInt(2);
			this.age = rs.getInt(3);
			this.gender = rs.getString(4);
			this.native_country = rs.getString(5);
		}
	}
	
	public boolean checkStudent (Connection conn) throws SQLException
	{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from students where username = \'"
				+ this.username + "\'");
		if (rs.next()) {return true;}
		else {return false;}
	}
	
	private void getUserCourseInDB (Connection conn) throws SQLException
	{
		Statement stmt = conn.createStatement();
		String sql = "select distinct course_editions.course_id from enrollments, course_editions, courses "
				+ "where enrollments.edition_id = course_editions.edition_id "
				+ "and course_editions.course_id = courses.course_id and username = \'" + this.username + "\'";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {this.course_taken.add(rs.getInt(1));}
	}
	
	public void recordCourse (int course) 
	{
		
		if (this.course_taken.contains(course))
		{System.out.println("course already in DB");}
		else {this.course_taken.add(course);}
	}
	
	public void printCourse ()
	{
		System.out.println(this.course_taken);
	}
	
	public void recordInterest(int id)
	{
		this.interest_in.add(id);
	}
	
	public void recordInterestMark(int mark)
	{
		this.interest_mark.add(mark);
	}
	
	public void recordSkill(int id)
	{
		this.skill_in.add(id);
	}
	
	public void recordSkillMark(int id)
	{
		this.skill_mark.add(id);
	}
	
	public void printTopics(Connection conn) throws SQLException
	{
		this.get_topics(conn);
		int l = this.topic_ids.size();
		Object[] ids = this.topic_ids.toArray();
		Object[] topics = this.topic_names.toArray();
		
		for (int i = 0; i < l; i++)
		{
			System.out.println(ids[i] + " : " + topics[i]);
		}
	}
	
	public void printSkills(Connection conn) throws SQLException
	{
		this.get_skills(conn);
		int l = this.skill_ids.size();
		Object[] ids = this.skill_ids.toArray();
		Object[] skills = this.skill_names.toArray();
		
		for (int i =0; i<l; i++)
		{
			System.out.println(ids[i] + " : " + skills[i]);
		}
	}
		
	private boolean validate () 
	{
		String[] genders = new String[] {"m", "f"};
		final Set<String> GENDERS = 
				new HashSet<String>(Arrays.asList(genders));
		if (this.username == null)
				return false;
		if (this.age <= 15 || this.age >= 100)
				return false;
		if (this.gender == null || !GENDERS.contains(this.gender))
			return false;
		if (this.native_country == null)
			return false;
		return true;
	}
	
	public void get_topics(Connection conn) throws SQLException
	{
		this.topic_ids.clear();
		this.topic_names.clear();
		
		Statement stmt = null;
		String query = "select * from topics";
		
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int cols = rs.getMetaData().getColumnCount();
			
			while (rs.next())
			{
				for (int i = 0; i < cols; i++) 
				{
					if (i == 0) {this.topic_ids.add(rs.getInt(i+1));} 
					else {this.topic_names.add(rs.getString(i+1));}
				}
			}
		} catch (SQLException e){SQLError.show(e);}
		finally {if (stmt != null) {stmt.close();}}
	}
	
	public void get_skills(Connection conn) throws SQLException
	{
		this.skill_ids.clear();
		String query = "select * from skills";
		
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int cols = rs.getMetaData().getColumnCount();
			
			while (rs.next())
			{
				for (int i = 0; i < cols; i++)
					if (i == 0) {this.skill_ids.add(rs.getInt(i+1));}
					else {this.skill_names.add(rs.getString(i+1));}
			}
		} catch (SQLException e) {SQLError.show(e);} 
		finally {if (stmt != null) {stmt.close();}}
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNative_country() {
		return native_country;
	}

	public void setNative_country(String native_country) {
		this.native_country = native_country;
	}

	public List<Integer> getInterest_in() {
		return interest_in;
	}

	public void setInterest_in(List<Integer> interest_in) {
		this.interest_in = interest_in;
	}

	public List<Integer> getInterest_mark() {
		return interest_mark;
	}

	public void setInterest_mark(List<Integer> interest_mark) {
		this.interest_mark = interest_mark;
	}

	public List<String> getTopic_names() {
		return topic_names;
	}

	public void setTopic_names(List<String> topic_names) {
		this.topic_names = topic_names;
	}


	public List<String> getSkill_names() {
		return skill_names;
	}

	public void setSkill_names(List<String> skill_names) {
		this.skill_names = skill_names;
	}

	public Integer getNum_gender() {
		if (this.gender.equals("m")) {return 1;}
		else {return 0;}
	}	

	public List<Integer> getCourse_taken() {
		return course_taken;
	}

	public List<Integer> getTopic_ids() {
		return topic_ids;
	}

	public void setTopic_ids(List<Integer> topic_ids) {
		this.topic_ids = topic_ids;
	}

	public List<Integer> getSkill_ids() {
		return skill_ids;
	}

	public void setSkill_ids(List<Integer> skill_ids) {
		this.skill_ids = skill_ids;
	}

	public List<Integer> getSkill_in() {
		return skill_in;
	}

	public void setSkill_in(List<Integer> skill_in) {
		this.skill_in = skill_in;
	}

	public List<Integer> getSkill_mark() {
		return skill_mark;
	}

	public void setSkill_mark(List<Integer> skill_mark) {
		this.skill_mark = skill_mark;
	}

	public static void main(String [] args) throws SQLException
	{
		Properties props = new Properties();
		Connection conn = DBConnection.getConnection(props);
		
		User s = new User();
		s.get_topics(conn);
		s.get_skills(conn);
		System.out.println(s.getSkill_ids());
		System.out.println(s.getSkill_names());
		
	}
	
}

