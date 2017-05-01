
delete from outcomes
where outcomes.battle in
(select battle from ships except select name from battles);

create or replace function check_battle()
	returns trigger as
	'
	begin
	if not exists
		(select * from battles
		where battles.name = new.battle)
	then
	return null;
	else
	return new;
	end if;
	end;
	'
language plpgsql;

create trigger audit_outcomes
before insert or update on outcomes
for each row
execute procedure check_battle();
