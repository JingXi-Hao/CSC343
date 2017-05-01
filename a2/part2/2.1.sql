select cls_level, count(*) as num_courses
from evaluations
where course_eval >= 3.5
group by cls_level;