## 문제점: 댓글 좋아요/싫어요 작동 시 아무런 테이블이 없으면 값이 여러개 출력되거나 아예 안됨, 하나쯤은 넣어놓고 실행.
***

## 문제점2: 댓글 작성시, 좋아요 / 싫어요 토글기능이 안됨 (목록갔다가 와야함) 이 부분은 노가다작업이고, 시간이 부족해서 차후에 개선된 방법을 배우면 해보겠음

----



### 테이블(멤버테이블은 동일함으로 제외)

alter table member_table add fileAttached int default 0;

create table heart_table(
id bigint auto_increment primary key,
mid bigint,
cid bigint,
constraint foreign key (mid) references member_table(id) on delete cascade,
constraint foreign key (cid) references comment_table(id) on delete cascade
);

create table no_table(
id bigint auto_increment primary key,
mid bigint,
cid bigint,
constraint foreign key (mid) references member_table(id) on delete cascade,
constraint foreign key (cid) references comment_table(id) on delete cascade
);

create table board_table(
id bigint auto_increment primary key,
boardWriter varchar(50),
boardPass varchar(20),
boardTitle varchar(50),
boardContents varchar(500),
createdAt datetime default now(),
boardHits int default 0,
fileAttached int default 0,
boardId bigint,
constraint foreign key (boardId) references member_table(id) on delete cascade
);


create table board_file_table(
id bigint auto_increment primary key,
originalFileName varchar(100),
storedFileName varchar(100),
boardId bigint,
constraint foreign key (boardId) references board_table(id) on delete cascade
);

create table member_file_table(
id bigint auto_increment primary key,
originalFileName varchar(100),
storedFileName varchar(100),
memberId bigint,
constraint foreign key (memberId) references member_table(id) on delete cascade
);



create table comment_table(
commentWriter varchar(50),
id bigint auto_increment primary key,
commentContents varchar(500),
commentCreatedDate datetime default now(),
commentPassword varchar(50),
boardId bigint,
memberId bigint,
cnt int default 0,
constraint foreign key (memberId) references member_table(id) on delete cascade,
constraint foreign key (boardId) references board_table(id) on delete cascade
);




