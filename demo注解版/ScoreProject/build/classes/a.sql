use test;
drop table  if exists t_score;
drop table  if exists t_student;
drop table  if exists t_course;
drop table  if exists t_class;


create table t_class(
   id int primary key auto_increment,
   classNo varchar(10) 
);

insert into t_class values(null,'1102');
insert into t_class values(null,'1103');
insert into t_class values(null,'1104');
insert into t_class values(null,'1105');


create table t_course(
	id int primary key auto_increment,
	name varchar(20)
);

insert into t_course values(null,'语文');
insert into t_course values(null,'数学');
insert into t_course values(null,'英语');
insert into t_course values(null,'化学');


create table t_student(
	id int primary key auto_increment,
	studentNo varchar(10),
	studentName varchar(20),
	cid  int ,
	FOREIGN key (cid) references t_class(id) 
);

create table t_score(
	id int primary key auto_increment,
	cid int,
	sid int,
	score float,
	FOREIGN key (cid) references t_course(id),
    FOREIGN key (sid) references t_student(id)
);
