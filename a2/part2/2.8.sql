/* predict the average score for this instructor */
select round(avg(avg_prof_eval), 2) as predicted_prof_eval
from(
	/* Find 15 nearest neighbours */
	select prof_id, avg_prof_eval, (case when language = 'englis' then 1 else 0 end) +
		(age - 45) * (age - 45) + (case when gender = 'female' then 1 else 0 end) +
		(bty_avg - 6) * (bty_avg - 6) + (case when ethnicity = 'not minority' then 1 else 0 end) as distance
	from (select prof_id, avg(prof_eval) as avg_prof_eval, language, age, gender, bty_avg, ethnicity
					   from evaluations
					   group by prof_id) temp
	order by 3
	limit 15) temp2;
	
/* Find 15 nearest neighbours */
/*select prof_id 
from(
	select prof_id, avg_prof_eval, (case when language = 'englis' then 1 else 0 end) +
		(age - 45) * (age - 45) + (case when gender = 'female' then 1 else 0 end) +
		(bty_avg - 6) * (bty_avg - 6) + (case when ethnicity = 'not minority' then 1 else 0 end) as distance
	from (select prof_id, avg(prof_eval) as avg_prof_eval, language, age, gender, bty_avg, ethnicity
					   from evaluations
					   group by prof_id) temp
	order by 3
	limit 15) temp2;*/

