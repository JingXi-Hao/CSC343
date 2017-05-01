select distinct topics.topic_id, topic
from topics, course_topics, courses
where topics.topic_id = course_topics.topic_id and courses.course_id = course_topics.course_id
order by dept_code;

/*select count(topic)
from (select distinct topics.topic_id, topic
from topics, course_topics, courses
where topics.topic_id = course_topics.topic_id and courses.course_id = course_topics.course_id
order by dept_code) t;*/