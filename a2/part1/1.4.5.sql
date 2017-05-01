
delete from classes
where numguns > 9 and bore > 14;

create or replace function check_numguns()
returns trigger as 
	'
	begin
	if new.numguns > 9 and new.bore > 14
	then return null;
	else return new;
	end if;
	end
	'
language plpgsql;

create trigger audit_numguns
before insert or update on classes
for each row
execute procedure check_numguns();
