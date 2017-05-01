delete from classes 
where exists
(select * from (select classes.class from 
classes left outer join ships on classes.class = ships.class
group by classes.class
having count(ships.name) < 3) b where b.class = classes.class);

