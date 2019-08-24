https://stackoverflow.com/questions/411517/management-of-java-string-resources
10,08,19 Оптимизация кода проверки подключения к БД

04.08.19 Документирование классов DBHelper, PSQLTest
внедрение значений полей для подключения к базе в DBHelper через аннотации @Value. Создание бина DBHelper (@Component) 
-
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

added database.properties with info for DB connection ( Spring )

Полное текстовое представление месяца (напр. "Август")
	Month queryMonth;
	queryMonth.getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("ru", "RU"));
	где TextStyle.ТипСтиля FULL или FULL_STANDALONE для названия вида - Января либо Январь
	
Сохраняется название последней нажатой кнопки в отчетах. Что позволяет уйти от частого использования кнопки "сброс" которая сбрасывала месяц отчета до текущего. 
Ранее месяц убывал с каждым нажатием на любую клавишу. Теперь он всегда текущий месяц, если клавиша отчета нажата первый раз в очереди повторяющихся нажатий.
//check which button was used last time, it other then reset month to current month
    	if(btnInUse != BTN_IN_USE_EXPENSIVE) { //если кнопка не была нажата в предыдущий раз
        	btnInUse = BTN_IN_USE_EXPENSIVE;
        	queryMonth = LocalDate.now().getMonth();	//месяц для запросов текущий
			btnShowCategMnth.setText("по категориям");  //вернуть надпись по умолчанию для других кнопок.
    	}
    	
 дата в месяц текстом https://stackoverflow.com/questions/9094392/get-month-name-from-number-in-postgresql
 
 дублирующийся код по вычислению начальной и конечной даты sql.Date для использования в запросе - теперь вынесен в метод private Date[] get2Dates(Month month)