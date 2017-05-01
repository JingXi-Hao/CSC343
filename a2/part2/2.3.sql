select cls_level, count(course_eval) as num_courses
from evaluations
where cls_level = 'lower' and course_eval >(select avg(course_eval) from evaluations where cls_level = 'lower')

union

select cls_level, count(course_eval) as num_courses
from evaluations
where cls_level = 'upper' and course_eval > (select avg(course_eval) from evaluations where cls_level = 'upper');