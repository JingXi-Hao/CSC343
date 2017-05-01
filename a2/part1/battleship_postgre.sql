drop schema if exists battleship cascade;
create schema battleship;
set search_path to battleship;

drop table if exists classes;
drop table if exists ships;
drop table if exists battles;
drop table if exists outcomes;

create type ship_types as enum('bb', 'bc');
create type results as enum('sunk', 'damaged', 'ok');

create table classes(
	class varchar(225),
	type ship_types,
	country varchar(225),
	numguns int,
	bore numeric,
	displacement numeric
);

create table ships(
	name varchar(225),
	class varchar(225),
	launched int
);

create table battles(
	name varchar(225),
	date_fought date
);

create table outcomes(
	ship varchar(225),
	battle varchar(225),
	result results
);
