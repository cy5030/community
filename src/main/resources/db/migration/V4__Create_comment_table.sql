create table comment
(
	id BIGINT auto_increment,
	parent_id BIGINT not null,
	type int not null,
	commentator BIGINT not null,
	gmt_create BIGINT not null,
	gmt_modified BIGINT not null,
	like_count int default 0 not null,
	comment_text varchar(5000),
	comment_count int default 0 not null,
	constraint comment_pk
		primary key (id)
);

comment on column comment.parent_id is '父类ID';

comment on column comment.type is '父类类型';

comment on column comment.commentator is '评论人ID';

comment on column comment.gmt_create is '评论时间';

comment on column comment.gmt_modified is '修改时间';

comment on column comment.like_count is '点赞数';

comment on column comment.comment_text is '评论内容
';

