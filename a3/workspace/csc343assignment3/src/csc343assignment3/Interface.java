package csc343assignment3;
import java.util.*;
import java.io.*;
import java.sql.*;


public class Interface {
	public static final String PROGRAM_NAME = "Interface";
	
	public static void main(String[] args) throws IOException, SQLException{
		
		Properties props = new Properties();
		java.sql.Connection conn =  DBConnection.getConnection(props);
		
		System.out.println("===============================");
		System.out.println("===============================");
		
		Scanner scanner = new Scanner(System.in);
		
		User user = null;
		int login = 0;
		while (login == 0) {
			System.out.println("1:login | 2:sign up");
			String login_type = scanner.next();
			
			switch(login_type) {
			case "1":
				System.out.println("Username:");
				user = new User();
				user.setUsername(scanner.next());
				if (user.checkStudent(conn)) {
					System.out.println("logged in as " + user.getUsername());
					user.setUp(conn);
					login = 1;
				} else {
					System.out.println("User not exists");
				}
			break;
			
			case "2":
				user = new User();
				System.out.println("Username:");
				user.setUsername(scanner.next());
				System.out.println("Permission:");
				user.setPermission(scanner.nextInt());
				System.out.println("age:");
				user.setAge(scanner.nextInt());
				System.out.println("gender:");
				user.setGender(scanner.next());
				System.out.println("native_country:");
				user.setNative_country(scanner.next());
				
				user.addto(conn);
				
				System.out.println("logged in as " + user.getUsername());
				login = 1;
				break;
			
			default:
				System.out.println("Invalid input");
			break;
			
			}
		}
		
		PrintTable.print(conn, "courses");
		
		System.out.println("Enter course ID taken");
		System.out.println("Enter -1 to finish this step.");
		int course = 0;
		int MAX_COURSE = Edition.getID(conn);
		while (course == 0) 
		{
			System.out.println("Enter course code: (1-" + MAX_COURSE + ")");
			int course_in = scanner.nextInt();
			if (course_in == -1) 
			{
				System.out.println("You entered following course:");
				user.printCourse();
				course = 1;
				break;
			} else if (course_in < 1 || course_in > MAX_COURSE) {System.out.println("Invalid input");}
			  else {user.recordCourse(course_in);}
		}
		
		
		
		
		
		System.out.println("\n===============================");
		System.out.println("Enter c to continue");
		int conti = 0;
		while (conti == 0) {
			if (scanner.next() != null) {conti = 1;}
		}
		
	
		
		
		PrintTable.print(conn, "topics");
		int TOPICID = Topic.getID(conn);
		
		System.out.println("\n===============================");
		System.out.println("Enter interest of the topic");
		System.out.println("Enter -1 to finish this step,");
		
		int topic = 0;
		int count = 0;
		int con = 0;
		while (topic == 0)
		{
			con = 0;
			System.out.println("Enter topic code (1-" + TOPICID + ")");
			String in = scanner.next();
			int id_in = Integer.parseInt(in);
			if (id_in == -1)
			{
				if (count >= 5) {
					topic = 1;
					System.out.println("You entered following:");
					user.printInterest();
					break;
				} else {
					System.out.println("You must enter at least five topics, you entered " + count);
					con = 1;
					}
			} else if (id_in < 1 || id_in > TOPICID) {System.out.println("Invalid input"); con = 1;}
			  else {user.recordInterest(id_in);}
				
			if (con == 0) {
				System.out.println("Your interest to this topic");
				in = scanner.next();
				int mark_in = Integer.parseInt(in);
				if (mark_in == -1) {
					if (count >= 5) {
						topic = 1;
						System.out.println("You entered following:");
						user.printInterest();
						break;
					} else {
						System.out.println("You must enter at least five topics, you entered " + count);
						con = 1;

					}
				} else if (mark_in < 1 || mark_in > 5) {
					System.out.println("Invalid input");
			
				} else {
					user.recordInterestMark(mark_in);
					count++;
				} 
			}
		}
		
		System.out.println("\n===============================");
		System.out.println("Enter c to continue");
		conti = 0;
		while (conti == 0) {
			if (scanner.next() != null) {conti = 1;}
		}
		
		PrintTable.print(conn, "skills");
		int SKILLID = Skill.getID(conn);
		System.out.println("\n===============================");
		System.out.println("Enter your skill level (1-" + SKILLID + ")");
		System.out.println("Enter -1 to finish this step,");
		
		int skill = 0;
		count = 0;
		
		while (skill == 0)
		{
			con = 0;
			System.out.print("Enter skill id");
			String in = scanner.next();
			int id_in = Integer.parseInt(in);
			if (id_in == -1)
			{
				if (count >= 5) {
					skill = 1;
					System.out.println("You entered following:");
					user.printSkillHave();
					break;
				} else {
					System.out.println("You must enter at least five topics, you entered " + count); 
					con = 1;
					}
			} else if (id_in < 1 || id_in > SKILLID) {System.out.println("Invalid input"); con = 1;}
			  else {user.recordSkill(id_in);}
			
			if (con == 0) {
				System.out.println("Enter skill level");
				in = scanner.next();
				id_in = Integer.parseInt(in);
				if (id_in == -1) {
					if (count >= 5) {
						skill = 1;
						System.out.println("You entered following:");
						user.printSkillHave();
						break;
					} else {
						System.out.println("You must enter at least five topics, you entered " + count);
						con = 1;
						
					}
				} else if (id_in < 1 || id_in > 5) {
					System.out.println("Invalid input");
				
				} else {
					user.recordSkillMark(id_in);
					count++;
				} 
			}
		}
		
		System.out.println("\n===============================");
		System.out.println("Enter c to continue");
		conti = 0;
		while (conti == 0) {
			if (scanner.next() != null) {conti = 1;}
		}
		
		System.out.println("\n===============================");
		System.out.println("All info acquired, generrating recommanded course..........");
		
		Recomd recomd = new Recomd();
		recomd.SetUpStudents(conn, user);
		
		System.out.println("You want to rank the course recommend by: ");
		System.out.println("1:grade|2:interest|3:skill|4:satisfaction");
		
		int choice = scanner.nextInt();
		
		switch (choice)
		{
		case 1: recomd.printRec_grade(conn, user); break;
		case 2: recomd.printRec_interest(conn, user); break;
		case 3: recomd.printRec_skill(conn, user); break;
		case 4: recomd.printRec_evaluation(conn, user); break;
		default: System.out.println("Invalid input"); break;
			
		}
		
		
		System.out.println("\n===============================");
		System.out.println("Do you want to enter your course experience? yes or no");
		int res = 0;
		while (res == 0)
		{
			String sel = scanner.next();
			if (sel.equals("yes"))
			{
				res = 1;
				
				List<Integer> courses = user.getCourse_taken();
				for (int c : courses)
				{
					System.out.println("Enter course experience for " + c + "? yes or no");
					String ex_in = scanner.next();
					if (ex_in.equals("yes"))
					{
						Edition ed = new Edition();
						ed.setEdition_id(c);
						ed.setUsername(user.getUsername());
						System.out.println("Enter your letter grade for this course: (A,B,C,D,F)");
						ed.setLetterGrade(scanner.next());
						System.out.println("Enter ranking for this course: (1-5)");
						ed.setCourse_ranking(scanner.nextInt());
						System.out.println("Enter instructor ranking for this course: (1-5)");
						ed.setInstr_ranking(scanner.nextInt());
						ed.enterExp(conn);
					}
					else if (!ex_in.equals("no")) {System.out.println("Invalid input.");}
					
				}
				
			}
			else if (sel.equals("no"))
			{
				res = 1;
			}
			else
			{
				System.out.println("Invalid input.");
			}
		}
		
		
		System.out.println("Enter new skill/topics? yes or no");
		
		res = 0;
		while (res == 0)
		{
			String sel = scanner.next();
			if (sel.equals("yes"))
			{
				int run = 0;
				while(run == 0)
				{
					System.out.println("Please choose:");
					System.out.println("0:quit|1:new topic|2:new skill|3:enter skill improv. 4:|enter interest dynamic");
					int choice1 = scanner.nextInt();
					
					switch(choice1)
					{
					case 0: run = 1; break;
					case 1:
						Topic t = new Topic();
						System.out.println("Enter topic name");
						String topic_name = scanner.next();
						t.insert_topic(conn, topic_name);
						break;
					case 2:
						Skill s = new Skill();
						System.out.println("Enter skill name");
						String skill_name = scanner.next();
						s.insert_skill(conn, skill_name);
						break;
					case 3:
						Skill_ex se = new Skill_ex();
						System.out.println("Course id:");
						se.setCourse_id(scanner.nextInt());
						System.out.println("Edition_id");
						se.setEdition_id(scanner.nextInt());
						System.out.println("Username:");
						se.setUsername(scanner.next());
						System.out.println("Skill ID:");
						se.setSkill_id(scanner.nextInt());
						System.out.println("Rank before:");
						se.setRank_before(scanner.nextInt());
						System.out.println("Rank after:");
						se.setRank_after(scanner.nextInt());
						se.enterSkill(conn);
						break;
					case 4:
						Topic_ex te = new Topic_ex();
						System.out.println("Course id:");
						te.setCourse_id(scanner.nextInt());
						System.out.println("Edition_id");
						te.setEdition_id(scanner.nextInt());
						System.out.println("Username:");
						te.setUsername(scanner.next());
						System.out.println("Topic ID:");
						te.setTopic_id(scanner.nextInt());
						System.out.println("Interest before:");
						te.setInterest_before(scanner.nextInt());
						System.out.println("Interest after:");
						te.setInterest_after(scanner.nextInt());
						te.enterSkill(conn);
						break;
					}
					
				}
			}
			else if (sel.equals("no")) {res = 1;}
			else {System.out.println("Invalid output");}
		}
		
		
		scanner.close();
	}
	
}

