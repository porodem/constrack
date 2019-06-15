

create table consumptions(
id serial primary key,
buy_date date NOT NULL,
item varchar(100),
category char,
rub int NOT NULL,
is_unexp int CHECK(is_unexp IN (0,1))
)