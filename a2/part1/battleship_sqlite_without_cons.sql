drop table if exists classes;
drop table if exists ships;
drop table if exists battles;
drop table if exists outcomes;

create table classes(
	class text,
	type text,
	country text,
	numguns int,
	bore numeric,
	displacement numeric
);

create table ships(
	name text,
	class text,
	launched int
);

create table battles(
	name text,
	date_fought date
);

create table outcomes(
	ship text,
	battle text,
	result text
);

