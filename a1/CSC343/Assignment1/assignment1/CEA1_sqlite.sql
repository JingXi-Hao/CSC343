drop table if exists instructor;
drop table if exists expertise;
drop table if exists has;
drop table if exists faculty;
drop table if exists research_interest;
drop table if exists of_faculty;
drop table if exists course;
drop table if exists prerequisite;
drop table if exists exclusive;
drop table if exists topic;
drop table if exists has_topic;
drop table if exists edition;
drop table if exists instructs;
drop table if exists teach_skill;
drop table if exists work_skill;
drop table if exists teaches;
drop table if exists student;
drop table if exists rank;
drop table if exists evaluates_skill;
drop table if exists evaluates_topic;
drop table if exists enrols_in;
drop table if exists job;
drop table if exists skill_used;
drop table if exists company_expertise;
drop table if exists has_expertise;
drop table if exists company;
drop table if exists worked_at;
drop table if exists researches;
drop table if exists evaluates;
drop table if exists f_type;
drop table if exists session;
drop table if exists gender;

create table gender
(type text primary key);
insert into gender values('male');
insert into gender values('female');

create table f_type
(type text primary key);

insert into f_type values('teaching');
insert into f_type values('researching');

create table faculty
(code text primary key,
type text not NULL references f_type(type),
name text);

create table researches
(topic text,
faculty_code text references faculty(code),
primary key(topic, faculty_code));

create table instructor
(name text primary key,
gender text not null references gender(type),
age int,
faculty_code text references faculty(code));

create table expertise
(name text primary key);

create table has
(instructor_name text,
expertise_name text,
primary key(instructor_name, expertise_name),
foreign key(instructor_name) references instructor(name),
foreign key(expertise_name) references expertise(name));

create table research_interest
(topic text primary key);

create table of_faculty
(instructor_name text,
faculty_code text,
year_start int,
primary key(instructor_name, faculty_code),
foreign key(instructor_name) references instructor(name),
foreign key(faculty_code) references faculty(code));

create table course
(course_number int,
faculty_code text references faculty(code),
name text,
area_of_study text,
primary key(course_number, faculty_code));

create table prerequisite
(low_level_course int,
low_level_faculty text,
high_level_course int,
high_level_faculty text,
primary key(low_level_course, low_level_faculty, high_level_course, high_level_faculty),
foreign key(low_level_course, low_level_faculty) references course(course_number, faculty_code),
foreign key(high_level_course, high_level_faculty) references course(course_number, faculty_code)
);

create table exclusive
(course1 int,
course1_faculty text,
course2 int,
course2_faculty text,
primary key(course1, course1_faculty, course2, course2_faculty),
foreign key(course1, course1_faculty) references course(course_number, faculty_code),
foreign key(course2, course2_faculty) references course(course_number, faculty_code)
);

create table topic
(topic_name text primary key);

create table has_topic
(course_code int,
faculty_code text, 
topic_name text references topic(topic_name),
primary key(course_code, faculty_code, topic_name),
foreign key(course_code, faculty_code) references course(course_number, faculty_code)
);

create table session
(type text primary key);

insert into session values('monring');
insert into session values('day');
insert into session values('evening');

create table edition
(course_code int,
faculty_code text,
time text not null references session(type),
number_of_students int,
start_date date,
end_date date,
primary key(course_code, faculty_code, time, start_date),
foreign key(course_code, faculty_code) references course(course_number, faculty_code),
constraint date_ok check(start_date < end_date));

create table instructs
(instructor_name text references instructor(name),
course_code int,
faculty_code text,
time text not null references session(type),
start_date date,
primary key(instructor_name, course_code, faculty_code, time, start_date),
foreign key(course_code, faculty_code, time, start_date) references edition(course_code, faculty_code, time, start_date)
);

create table teach_skill
(name text primary key);

create table work_skill
(name text primary key);

create table teaches
(course_code int, 
faculty_code text,
skill_name text references teach_skill(name),
primary key(course_code, faculty_code, skill_name),
foreign key(course_code, faculty_code) references course(course_number, faculty_code)
);

create table student
(username text primary key,
year_of_birth int,
month_of_birth int,
start_year int,
start_month int,
country_of_birth text,
age int constraint check_age check(age > 0),
gender text not null references gender(type));

create table rank
(student text references student(username),
instructor text references instructor(name),
mark int not NULL check(mark between 1 and 5),
primary key(student, instructor));

create table evaluates_skill
(student text references student(username),
skill text references teach_skill(name),
before_mark int not NULL check (before_mark between 1 and 5),
after_mark int not NULL check (after_mark between 1 and 5),
primary key(student, skill, before_mark, after_mark)
);

create table evaluates
(student text references student(username),
skill text references work_skill(name),
before_mark int not NULL check (before_mark between 1 and 5),
after_mark int not NULL check (after_mark between 1 and 5),
primary key(student, skill, before_mark, after_mark));

create table evaluates_topic
(student text references student(username),
topic text references topic(topic_name), 
before_mark int not NULL check (before_mark between 1 and 5),
after_mark int not NULL check (after_mark between 1 and 5),
primary key(student, topic, before_mark, after_mark));

create table enrols_in
(student text references student(username),
course_number int, 
faculty_code text,
start_date date,
time text not null references session(type),
grade numeric constraint check_grade check(grade between 0 and 100),
satisfation int not NULL check(satisfation between 1 and 5),
primary key(student, course_number, faculty_code, time, start_date),
foreign key(course_number, faculty_code, time, start_date) references edition(course_number, faculty_code, time, start_date)
);

create table company
(name text primary key);

create table job
(title text,
company text references company(name),
primary key(title, company));

create table worked_at
(title text,
company text,
student text references student(name),
start_date date,
end_date date,
primary key(title, company, student),
foreign key(title, company) references job(title, company),
constraint check_date check(start_date < end_date));

create table skill_used
(skill text references work_skill(name),
title text,
company text,
primary key(skill, title, company),
foreign key(title, company) references job(title, company)
);

create table company_expertise
(name text primary key);

create table has_expertise
(company text references company(name),
expertise text references company_expertise(name),
primary key (company, expertise));


begin;

insert into instructor values('Marina Barsky', 'female', NULL, 'CSC');
insert into faculty values('CSC', 'teaching', 'Computer Science');
insert into course values(343, 'CSC', 'Introduction to Database', 'computer science');
insert into instructor values('Diane Horton', 'female', NULL, 'CSC');
insert into course values(209, 'CSC', 'Software Tools and Systems Programming', 'computer science');
insert into instructor values('Y. Zheng', 'male', NULL, 'MAT');
insert into faculty values('MAT', 'teaching', 'Mathmatics');
insert into course values(137, 'MAT', 'Calculus', 'mathmatics');
insert into faculty values('STA', 'teaching', 'Statistics');
insert into instructor values('L. AI. Labadi', 'male', NULL, 'STA');
insert into course values(247, 'STA', 'Probability with Computer Application', 'statistics');
insert into course values(207, 'CSC', 'Software Design', 'computer science');
insert into student values('c5caoye', 1996, 6, 2014, 9, 'China', 14, 'male');
insert into student values('c4haojin', 1993, 7, 2013, 9, 'China', 14, 'female');
insert into instructor values('Xander', 'male', NULL, 'CSC');
insert into faculty values('CSC', 'teaching', 'Computer Science');
insert into course values(258, 'CSC', 'Computer Organization', 'computer science');
insert into instructor values('Bahar', 'female', NULL, 'CSC');
insert into course values(236, 'CSC', 'Introduction to the Theory of Computation', 'computer science');
insert into course values(165, 'CSC', 'Mathmatical Expression and Reasoning for Computer Science', 'computer science');
insert into instructor values('Jeniffer', 'female', NULL, 'CSC');
insert into course values(108, 'CSC', 'Introduction to Computer Programming', 'computer science');
insert into course values(148, 'CSC', 'Introduction to Computer Science', 'computer science');
insert into instructor values('David Liu', 'male', NULL, 'CSC');

insert into edition values(343, 'CSC', 'evening', 108, '2016-05-09', '2016-08-15');
insert into edition values(209, 'CSC', 'day', 290, '2015-02-08', '2016-04-30');
insert into edition values(137, 'MAT', 'evening', 300, '2015-02-08', '2016-04-30');
insert into edition values(247, 'STA', 'day', 247, '2015-09-08', '2015-12-30');
insert into edition values(207, 'CSC', 'day', 207, '2015-09-08', '2015-12-30');
insert into edition values(258, 'CSC', 'evening', 290, '2015-09-08', '2016-04-30');
insert into edition values(236, 'CSC', 'evening', 236, '2015-09-08', '2016-12-30');
insert into edition values(165, 'CSC', 'evening', 250, '2014-02-08', '2015-04-30');
insert into edition values(148, 'CSC', 'evening', 258, '2014-02-08', '2015-04-30');
insert into edition values(108, 'CSC', 'day', 400, '2013-09-01', '2013-12-23');

insert into of_faculty values('Marina Barsky', 'CSC', 1999);
insert into of_faculty values('Diane Horton', 'CSC', 1996);
insert into of_faculty values('Y. Zheng', 'MAT', 2003);
insert into of_faculty values('L. AI. Labadi', 'STA', 1989);
insert into of_faculty values('Xander', 'CSC', 2005);
insert into of_faculty values('Bahar', 'CSC', 2002);
insert into of_faculty values('Jeniffer', 'CSC', 2000);
insert into of_faculty values('David Liu', 'CSC',1948);

insert into instructs values('Marina Barsky', 343, 'CSC', 'evening', '2016-05-09');
insert into instructs values('Diane Horton', 207, 'CSC', 'day', '2015-09-08');
insert into instructs values('Diane Horton', 209, 'CSC', 'day', '2015-02-08');
insert into instructs values('Y. Zheng', 137, 'MAT','evening', '2015-02-08');
insert into instructs values('Xander', 258, 'CSC', 'evening', '2015-09-08');
insert into instructs values('Bahar', 236, 'CSC','evening', '2015-09-08');
insert into instructs values('Bahar', 165, 'CSC', 'evening', '2014-02-08');
insert into instructs values('Jeniffer', 108, 'CSC', 'day', '2013-09-01');
insert into instructs values('David Liu', 148, 'CSC', 'evening', '2014-02-08');
insert into instructs values('Jeniffer', 148, 'CSC', 'evening', '2014-02-08');
insert into instructs values('L. AI. Labadi', 247, 'STA', 'day', '2015-09-08');

insert into enrols_in values('c5caoye', 148, 'CSC', '2014-02-08', 'evening', 74, 3);
insert into enrols_in values('c5caoye', 108, 'CSC', '2013-09-01', 'day', 100, 5);
insert into enrols_in values('c5caoye', 165, 'CSC', '2014-02-08', 'evening', 90, 2);
insert into enrols_in values('c5caoye', 137, 'MAT', '2015-02-08', 'evening', 74, 1);
insert into enrols_in values('c5caoye', 247, 'STA', '2015-09-08', 'day', 74, 3);
insert into enrols_in values('c4haojin', 258, 'CSC', '2015-09-08', 'evening', 100, 5);
insert into enrols_in values('c4haojin', 343, 'CSC', '2016-05-09', 'evening', 100, 5);
insert into enrols_in values('c5caoye', 209, 'CSC', '2015-02-08', 'day', 100, 5);
insert into enrols_in values('c4haojin', 207, 'CSC', '2015-09-08', 'day', 99, 1);
insert into enrols_in values('c4haojin', 236, 'CSC', '2015-09-08', 'evening', 85, 4);

insert into company values('UofT');
insert into company values('Amazon');
insert into job values('TA', 'UofT');
insert into job values('sellor', 'Amazon');

insert into worked_at values('TA', 'UofT', 'c4haojin', '2014-09-08', '2015-05-30');
insert into worked_at values('sellor', 'Amazon', 'c5caoye', '2015-01-01', '2015-06-23');

insert into work_skill values('study');
insert into work_skill values('writing');

insert into skill_used values('study', 'TA', 'UofT');
insert into skill_used values('writing', 'sellor', 'Amazon');

insert into evaluates values('c5caoye', 'writing', 1, 2);
insert into evaluates values('c4haojin', 'study', 4, 5);

insert into teach_skill values('python');
insert into teach_skill values('verilog');
insert into teach_skill values('java');
insert into teach_skill values('calculus');
insert into teach_skill values('probability');
insert into teach_skill values('reasoning');
insert into teach_skill values('database');
insert into teach_skill values('C');
insert into teach_skill values('basic logic');
insert into teach_skill values('data structure');

insert into teaches values(108, 'CSC', 'python');
insert into teaches values(258, 'CSC', 'verilog');
insert into teaches values(207, 'CSC', 'java');
insert into teaches values(137, 'MAT', 'calculus');
insert into teaches values(247, 'STA', 'probability');
insert into teaches values(236, 'CSC', 'reasoning');
insert into teaches values(343, 'CSC', 'database');
insert into teaches values(209, 'CSC', 'C');
insert into teaches values(165, 'CSC', 'basic logic');
insert into teaches values(148, 'CSC', 'data structure');

insert into evaluates_skill values('c5caoye', 'python', 1, 3);
insert into evaluates_skill values('c5caoye', 'data structure', 1, 2);
insert into evaluates_skill values('c5caoye', 'basic logic', 2, 4);
insert into evaluates_skill values('c5caoye', 'calculus', 1, 2);
insert into evaluates_skill values('c5caoye', 'probability', 1, 2);
insert into evaluates_skill values('c5caoye', 'C', 1, 3);
insert into evaluates_skill values('c4haojin', 'verilog',2, 4);
insert into evaluates_skill values('c4haojin','java', 1, 5);
insert into evaluates_skill values('c4haojin','reasoning', 2, 4);
insert into evaluates_skill values('c4haojin','database', 1, 5);

insert into rank values('c5caoye', 'Jeniffer', 2);
insert into rank values('c5caoye', 'David Liu', 3);
insert into rank values('c5caoye', 'Bahar', 5);
insert into rank values('c5caoye', 'Y. Zheng', 1);
insert into rank values('c5caoye', 'L. AI. Labadi', 4);
insert into rank values('c5caoye', 'Diane Horton', 3);
insert into rank values('c4haojin', 'Xander', 3);
insert into rank values('c4haojin', 'Diane Horton', 4);
insert into rank values('c4haojin', 'Bahar', 5);
insert into rank values('c4haojin', 'Marina Barsky', 5);

insert into topic values('python');
insert into topic values('verilog');
insert into topic values('java');
insert into topic values('calculus');
insert into topic values('probability');
insert into topic values('reasoning');
insert into topic values('database');
insert into topic values('C');
insert into topic values('basic logic');
insert into topic values('data structure');

insert into has_topic values(108, 'CSC', 'python');
insert into has_topic values(258, 'CSC', 'verilog');
insert into has_topic values(207, 'CSC', 'java');
insert into has_topic values(137, 'MAT', 'calculus');
insert into has_topic values(247, 'STA', 'probability');
insert into has_topic values(236, 'CSC', 'reasoning');
insert into has_topic values(343, 'CSC', 'database');
insert into has_topic values(209, 'CSC', 'C');
insert into has_topic values(165, 'CSC', 'basic logic');
insert into has_topic values(148, 'CSC', 'data structure');

insert into evaluates_topic values('c5caoye', 'python', 1, 3);
insert into evaluates_topic values('c5caoye', 'data structure', 1, 2);
insert into evaluates_topic values('c5caoye', 'basic logic', 2, 4);
insert into evaluates_topic values('c5caoye', 'calculus', 1, 2);
insert into evaluates_topic values('c5caoye', 'probability', 1, 2);
insert into evaluates_topic values('c5caoye', 'C', 1, 3);
insert into evaluates_topic values('c4haojin', 'verilog',2, 4);
insert into evaluates_topic values('c4haojin','java', 1, 5);
insert into evaluates_topic values('c4haojin','reasoning', 2, 4);
insert into evaluates_topic values('c4haojin','database', 1, 5);

commit;


