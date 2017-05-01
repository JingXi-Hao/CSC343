select prof_id as "IDs"
from(
	select avg(prof_eval) avg_eval, prof_id
	from evaluations
	group by prof_id
	having avg_eval >= (select avg(avg_eval)
						from(
							select avg(prof_eval) avg_eval
							from evaluations
							group by prof_id) temp)
	order by avg_eval desc
	limit 5) temp2;

/*select avg(prof_eval) from evaluations;*/ ---average of column prof_eval

/* average of average of instructor evaluation for each instructor
select avg(avg_eval)
from(
select avg(prof_eval) avg_eval, prof_id
from evaluations
group by prof_id) temp;*/