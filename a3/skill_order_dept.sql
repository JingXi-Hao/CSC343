select distinct skills.skill_id, skill
from skills, courses, course_skills
where skills.skill_id = course_skills.skill_id and courses.course_id = course_skills.course_id
order by dept_code;

/*select dept_code, skill
from skills, courses, course_skills
where skills.skill_id = course_skills.skill_id and courses.course_id = course_skills.course_id
order by dept_code;

select count(skill)
from skills;*/

/*select count(skill)
from (select distinct skills.skill_id, skill
from skills, courses, course_skills
where skills.skill_id = course_skills.skill_id and courses.course_id = course_skills.course_id
order by dept_code) t;*/