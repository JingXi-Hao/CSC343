/* Mode female to female */
-- select mode_female_to_female
-- from(
-- 	select max(counts) as max_counts, bty_ftof as mode_female_to_female
-- 	from(
-- 		select count(*) as counts, bty_f1lower as bty_ftof
-- 		from(
-- 			select bty_f1lower from evaluations where gender = 'female'
-- 			union all
-- 			select bty_f1upper from evaluations where gender = 'female'
-- 			union all 
-- 			select bty_f2upper from evaluations where gender = 'female') counts_ftof
-- 		group by bty_f1lower) ftof) temp;
	
/* Mode female to male or female*/
select
	case when gender = 'female' then 'female to female'
		 else 'female to male'
	end as groups, mode
from(
	select max(counts) as max_counts, mode, gender
	from(
		select count(*) as counts, bty_f1lower as mode, gender
		from(
			select bty_f1lower, gender from evaluations
			union all
			select bty_f1upper, gender from evaluations
			union all
			select bty_f2upper, gender from evaluations) counts_ftom
		group by mode, gender) ftom
	group by gender) temp

union

/* Mode male to female or male */
select
	case when gender = 'female' then 'male to female'
		 else 'male to male'
	end as groups, mode
from(
	select max(counts) as max_counts, mode, gender
	from(
		select count(*) as counts, bty_m1lower as mode, gender
		from(
			select bty_m1lower, gender from evaluations
			union all
			select bty_m1upper, gender from evaluations
			union all
			select bty_m2upper, gender from evaluations) counts_ftom
		group by mode, gender) ftom
	group by gender) temp;