select country from classes
except
select c1.country
from classes c1, classes c2
where c1.country <> c2.country and c1.numguns < c2.numguns;