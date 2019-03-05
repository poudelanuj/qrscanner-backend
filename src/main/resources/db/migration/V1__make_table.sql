create table users
(
  id_user   bigint auto_increment
    primary key,
  email               varchar(255) not null,
  password            varchar(255) null,
  constraint UK2jg5d0b9dx236rtll2uylnv7a
    unique (email)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table roles
(
  id_role   bigint auto_increment
    primary key,
  role_name varchar(60) null,
  constraint UK_epk9im9l9q67xmwi4hbed25do
    unique (role_name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table user_roles
(
  id_user bigint not null,
  id_role bigint not null,
  primary key (id_user, id_role),
  constraint FKrcmv344t6l0beetcs8u4xhpd
    foreign key (id_role) references roles (id_role),
  constraint FKt3ccjnypjbg03cawlifd4pod7
    foreign key (id_user) references users (id_user)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;