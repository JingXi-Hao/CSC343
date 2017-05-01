select rank, round(sum(prof_eval)/count(*), 2) as avg_eval
from evaluations
group by rank;