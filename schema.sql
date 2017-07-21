create table account (
	-- just assume all are required (not null.)
	id serial primary key,
	partner_account varchar(64) not null,
	partner_blz varchar(64) not null,
	bank_name varchar(128) not null,
	partner_name varchar(256) not null,
	booking_text varchar(256) not null,
	subject varchar(256) not null,
	booking_date timestamp with time zone not null,
	transfer_type varchar(64) not null,
	currency varchar(64) not null,
	amount numeric not null
);
