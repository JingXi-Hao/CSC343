--select class, count(*) as numsunk
--from ships, outcomes
--where name = ship and class in (select class from (select class, count(name) as numships
--from ships
--group by class
--having count(name) >= 3) t) 
--group by class
--having result = 'sunk';

select class, count(*) as numsunk
from ships, outcomes
where name = ship and result = 'sunk' and class in
(select class from (select class, count(name) as numships
	from ships group by class having count(name) >= 3) t)
group by class;
