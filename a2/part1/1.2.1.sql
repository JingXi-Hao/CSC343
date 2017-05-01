select ships.name 
from ships, classes 
where classes.class = ships.class 
and classes.displacement > 35000;  
