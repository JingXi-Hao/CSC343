delete from ships 
where ships.class in (select class from ships except select class from classes);

create or replace function check_class()
	returns trigger as
	'
	begin
		if not exists
			(select * from classes
			where classes.class = new.class)
		then
		return null;
		else
		return new;
		end if;
		
		return new;
	end;
	'
language plpgsql;

create trigger audit_ships
	before insert or update on ships
	for each row
	execute procedure check_class();
