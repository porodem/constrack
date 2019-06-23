package ru.porodem.constrack;

public class DBScheme {
	public static final class ConsumptionsTable {
		
		public static final String NAME = "consumptions";
		
		public static final class Cols {
			public static final String DATE = "buy_date";
			public static final String ITEM = "item";
			public static final String CATEGORY = "category";
			public static final String RUB = "rub";
			public static final String UNEXP = "is_unexp";
		}
	}
	
	public static final class IncomeTable {
		
		public static final String NAME = "income";
		
		public static final class Cols{
			public static final String RUB = "rub";
			public static final String INCOMER = "incomer";
			public static final String DATE = "ddate";
		}
	}
	

}
