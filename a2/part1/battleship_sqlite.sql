drop table if exists classes;
drop table if exists ships;
drop table if exists battles;
drop table if exists outcomes;
drop table if exists ship_types;
drop table if exists results;

create table ship_types(
	type text primary key
);

insert into ship_types values("bb");
insert into ship_types values("bc");

create table results(
	result text primary key
);

insert into results values("sunk");
insert into results values("damaged");
insert into results values("ok");


create table classes(
	class text,
	type text not null references ship_types(type),
	country text,
	numguns int,
	bore numeric,
	displacement numeric,
	primary key(class, type, country)
);

create table ships(
	name text,
	class text references classes(class),
	launched int,
	primary key(name, class)
);

create table battles(
	name text primary key,
	date_fought date
);

create table outcomes(
	ship text references ships(name),
	battle text references battles(name),
	result text references results(result),
	primary key(ship, battle)
);

