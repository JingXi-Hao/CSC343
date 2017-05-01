select prof_id as "IDs"
from(
	select avg(bty_avg) avg_bty, prof_id
	from evaluations
	group by prof_id
	having avg_bty >= (select avg(avg_bty)
					   from(
							select avg(bty_avg) as avg_bty
							from evaluations
							group by prof_id) temp)
	order by avg_bty desc
	limit 5) temp2;
	
---The answers are not the same with the answers in question 6.