
Описание таблиц

create table consumptions(
id serial primary key,
buy_date date NOT NULL,
item varchar(100),
category char,
rub int NOT NULL,
is_unexp int CHECK(is_unexp IN (0,1))
)

CREATE TABLE public.income
(
    id integer NOT NULL DEFAULT nextval('income_id_seq'::regclass),
    rub integer NOT NULL,
    incomer character(40) COLLATE pg_catalog."default",
    ddate date NOT NULL,
    CONSTRAINT income_pkey PRIMARY KEY (id)
)

added converting from java.util.Date to java.time.LocalDate

added statistic for 10 days, month.
