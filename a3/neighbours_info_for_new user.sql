/* By grades for new user */
select course as BEST_RECOMMENDATIONS_BY_GRADE
from(
	select course_id, course, average_grade,
		case when 90 <= average_grade and average_grade >= 100 then 'A+'
			 when 85 <= average_grade and average_grade >= 89 then 'A'
			 when 80 <= average_grade and average_grade >= 84 then 'A-'
			 when 77 <= average_grade and average_grade >= 79 then 'B+'
			 when 73 <= average_grade and average_grade >= 76 then 'B'
			 when 70 <= average_grade and average_grade >= 72 then 'B-'
			 when 67 <= average_grade and average_grade >= 69 then 'C+'
			 when 63 <= average_grade and average_grade >= 66 then 'C'
			 when 60 <= average_grade and average_grade >= 62 then 'C-'
			 when 57 <= average_grade and average_grade >= 59 then 'D+'
			 when 53 <= average_grade and average_grade >= 56 then 'D'
			 when 50 <= average_grade and average_grade >= 52 then 'D-'
			 else 'F'
			 end as letter_grade
	from(		 
		select course_editions.course_id as course_id, (dept_code || course_number) as course, avg(max_grade) as average_grade
		from enrollments, course_editions, courses, letter_grades
		where username in ('960120', 'JiH', 'cai', 'ccccc', 'ilovechicken','en', 'madame_id', 'ncjc','nielie','raz24','student335') and course_editions.edition_id = enrollments.edition_id
			and courses.course_id = course_editions.course_id and letter_grades.letter_grade = enrollments.letter_grade
			and course_editions.course_id not in (/* list of courses new user input */)
		group by course_editions.course_id
		order by average_grade desc) temp
	limit 5) temp1;

/* By course evaluations for new user */
select course as BEST_RECOMMENDATIONS_BY_EVALUATION_SCORE
from(
	select course_editions.course_id, (dept_code || course_number) as course, avg(course_ranking) as average_course_eval
	from enrollments, course_editions, courses
	where username in ('960120', 'JiH', 'cai', 'ccccc', 'ilovechicken','en', 'madame_id', 'ncjc','nielie','raz24','student335') and course_editions.edition_id = enrollments.edition_id
		and courses.course_id = course_editions.course_id
		and course_editions.course_id not in (/* list of courses new user input */)
	group by course_editions.course_id
	order by average_course_eval desc
	limit 5) temp;

/* By promoting interests for new user */
select course as BEST_RECOMMENDATIONS_BY_PROMOTING_INTERESTS
from(
	select courses.course_id, (dept_code || course_number) as course, avg((interest_after - interest_before)) as avg_interest_increase
	from topic_interests, courses
	where courses.course_id = topic_interests.course_id and username in ('960120', 'JiH', 'cai', 'ccccc', 'ilovechicken','en', 'madame_id', 'ncjc','nielie','raz24','student335') 
		and topic_id in (5, 82, 72, 52) and courses.course_id not in (/* list of courses new user input */)
	group by courses.course_id
	order by avg_interest_increase desc
	limit 5) temp;
	
/* By improving skills for new user */
select course as BEST_RECOMMENDATIONS_BY_IMPROVING_SKILLS
from(
	select username, skill_id, courses.course_id, (dept_code || course_number) as course, avg((rank_after - rank_before)) as avg_skill_increase
	from skill_rankings, courses
	where courses.course_id = skill_rankings.course_id and username in ('960120', 'JiH', 'cai', 'ccccc', 'ilovechicken','en', 'madame_id', 'ncjc','nielie','raz24','student335') 
		and skill_id in (57, 7, 40, 35, 77) and courses.course_id not in (/* list of courses new user input */)
	group by courses.course_id
	order by avg_skill_increase desc
	limit 5) temp;
