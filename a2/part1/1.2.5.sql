select distinct c1.ship
from (select * from battles b1 inner join  outcomes o1 on b1.name = o1.battle) c1,
(select * from battles b2 inner join outcomes o2 on b2.name = o2.battle) c2
where c1.result = 'damaged'
	and c2.result <> 'damaged';
