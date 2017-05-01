select gender, count(*) as num_profs
from evaluations
where prof_eval >= 3.5
group by gender;