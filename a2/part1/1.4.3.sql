
delete from outcomes
where outcomes.ship in
(select ship from outcomes except select name from ships);

create or replace function check_outcome()
	returns trigger as
	'
	begin
	if not exists
		(select * from ships
		where ships.name = new.ship)
	then
	return null;
	else
	return new;
	end if;
	end;
	'
language plpgsql;

create trigger audit_outcomes_ship
before insert or update on outcomes
for each row
execute procedure check_outcome();
