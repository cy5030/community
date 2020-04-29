alter table USER
	add bio BIGINT;
alter table USER
	add avatar_url varchar(200);
alter table USER alter column ID BIGINT auto_increment;
