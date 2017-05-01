select name
from ships, classes 
where ships.class = classes.class
except
select s1.name
from ships s1, ships s2, classes c1, classes c2
where c1.class = s1.class and c2.class = s2.class and s1.name <> s2.name
		and c1.bore = c2.bore and c1.numguns < c2.numguns;