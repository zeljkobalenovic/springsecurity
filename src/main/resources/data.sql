insert into users (username,password,enabled)
values ('test','test',true);

insert into users (username,password,enabled)
values ('baki','baki',true);

insert into authorities (username,authority)
values ('test','ROLE_USER');

insert into authorities (username,authority)
values ('baki','ROLE_ADMIN');