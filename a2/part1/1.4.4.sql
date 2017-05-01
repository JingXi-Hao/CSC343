delete from classes
where bore > 16;

create or replace function check_bore()
	returns trigger as 
	'
	begin
	if new.bore > 16 then
	return null;
	else 
	return new;
	end if;
	end
	'
language plpgsql;

create trigger audit_bore 
before insert or update on classes
for each row
execute procedure check_bore();
