select outcomes.ship, c.displacement, c.numguns 
from outcomes left outer join 
   	(select ships.name, classes.displacement, classes.numguns from classes, ships where classes.class = ships.class) c
   	on outcomes.ship = c.name 
where battle = 'Guadalcanal';


