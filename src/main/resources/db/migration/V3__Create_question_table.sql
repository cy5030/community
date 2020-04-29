create table question
(
	id BIGINT auto_increment,
	title varchar(50) not null,
	description varchar(5000) not null,
	tag varchar(50) not null,
	creator BIGINT not null,
	gmt_create BIGINT not null,
	gmt_modified BIGINT not null,
	like_count int default 0,
	view_count int default 0,
	comment_count int default 0,
	constraint question_pk
		primary key (id)
);

