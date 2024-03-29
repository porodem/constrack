package ru.porodem.constrack;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import ru.porodem.constrack.DBScheme2.ConsumptionsTable;
import ru.porodem.constrack.DBScheme2.IncomeTable;


/**Создает подключение к БД.
 *Содержит методы с закардхожеными запросами к БД 
 * @author Dolgopolov Anatoliy
 * */
@Component("beanDBHelper")
public class DBHelper {
	
	@Value("${dbUrl}")
	private String url;
	@Value("${dbUser}")
	private String user;
	@Value("${dbPass}")
	private String password;
	
	/**
	 * Информация о статусе подключения к БД выводимая в текстовом поле.
	 */
    static boolean status;
    
    LocalDate today;
    LocalDate firstDayOfMonth;
    
    public DBHelper() {
    	today = LocalDate.now();
    	firstDayOfMonth = today.withDayOfMonth(1);
    }
    
    public void setUrl(String dbUrl) {
    	url = dbUrl;
    }
    
    public void setUser(String user) {
    	this.user = user;
    }
    
    public void setPass(String pass) {
    	password = pass;
    }
	
	public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            setStatus(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
	
	public boolean isConnectOk() {
		try(Connection conn =this.connect()) {
			
		} catch (SQLException e) {
			e.getMessage();
		}								
									 
        return status;
    }
	
	/**
	 * Добавляет в БД запись о доходе
	 * @param incomer источник дохода
	 * @param rub доход в рублях
	 * @param date когда был получен доход
	 * @return строка с информацией о добавленной в БД записи или с ошибкой
	 */
	public String addIncome(int rub, String incomer, LocalDate date) {
		
		String result;
		int rowsInserted = 0;
		java.sql.Date sqlDate = java.sql.Date.valueOf(date);
		
		String SQL = "INSERT INTO " +
    			IncomeTable.NAME + "(" +
    			IncomeTable.Cols.RUB + ", " +
    			IncomeTable.Cols.INCOMER + ", " +
    			IncomeTable.Cols.DATE +
    			" ) values(?,?,?)";
		
		try(Connection conn = this.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)){
    		pstmt.setInt(1, rub);
    		pstmt.setString(2, incomer);
    		pstmt.setDate(3, sqlDate);
    		
    		rowsInserted = pstmt.executeUpdate();
    	}
    	catch(SQLException e) {
    		System.out.print(e.getMessage());
    	} 
		
		if(rowsInserted > 0) {
			result = "Учтен доход: (" + incomer + ") " +  rub + " руб.";
    	} else {
    		result = PSQLTest.WARNING_DB_WRITE;
    	}
		
		return result;
	}
	
	/**
	 * Добавляет запись о расходе
	 * @return строку с результатом добавления или ошибкой
	 */
	public String addConsumption(LocalDate date, String item, String category, int rub, int unexp) {
		
		String result;
		int rowsInserted = 0;
    	Date sqlDate = Date.valueOf(date);
		
		String SQL = "INSERT INTO " +
				ConsumptionsTable.NAME + "( " +
						 ConsumptionsTable.Cols.DATE + ", " +
						 ConsumptionsTable.Cols.ITEM + ", " + 
						 ConsumptionsTable.Cols.CATEGORY + ", " +
						 ConsumptionsTable.Cols.RUB + ", " +
						 ConsumptionsTable.Cols.UNEXP
						+ " ) values(?,?,?,?,?)";
    	
    	try(Connection conn = this.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)){
    		pstmt.setDate(1, sqlDate);
    		pstmt.setString(2, item);
    		pstmt.setString(3, category);
    		pstmt.setInt(4, rub);
    		pstmt.setInt(5, unexp);
    		
    		rowsInserted = pstmt.executeUpdate();
    	}
    	catch(SQLException e) {
    		System.out.print(e.getMessage());    		
    	}  
    	
    	if(rowsInserted > 0) {
    		result = "Учтен расход: " + item + " (" + category + ") " +  rub + " руб.";
    	} else {
    		result = PSQLTest.WARNING_DB_WRITE;
    	} 
    	
    	return result;
	}
	
	/**
	 * Получает сумму расходов за текущий день
	 */
	public String getTodayCost(LocalDate d) {
		
		String queryResult = "";
    	java.sql.Date sqlDate = java.sql.Date.valueOf(d);
    	
    	String SQL = "SELECT SUM(" +
    			ConsumptionsTable.Cols.RUB +
    			") FROM " +
    			ConsumptionsTable.NAME +
    					" where " +
    					ConsumptionsTable.Cols.DATE + " = ?";
    	
    	try(Connection conn = this.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)) {    		
    		pstmt.setDate(1, sqlDate);    		
    		ResultSet rs = pstmt.executeQuery();    		
    		rs.next();
    		queryResult = String.valueOf(rs.getInt("sum"));
    	}
    	catch(SQLException e) {
        	System.out.print(e.getMessage());	
        }  
    	
    		return queryResult;
	}
	
	/**
	 * получить сумму расходов за последние 10 дней
	 */
	public String get10daysCost() {
		
		String queryResult = "";
		
		Date sqlDate = Date.valueOf(today);
    	LocalDate tenDaysEarly = today.minusDays(10);
    	Date sqlDate10ago = Date.valueOf(tenDaysEarly);
    	
    	String SQL = "SELECT SUM(" +
    			ConsumptionsTable.Cols.RUB +
    			") FROM " +	ConsumptionsTable.NAME +
    					" where " +
    					ConsumptionsTable.Cols.DATE + " between ? and ?";
    	
    	try(Connection conn = this.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)) {    		
    		pstmt.setDate(1, sqlDate10ago );  
    		pstmt.setDate(2, sqlDate);    
    		ResultSet rs = pstmt.executeQuery();    		
    		rs.next();
    		queryResult = String.valueOf(rs.getInt("sum"));
    	}
    	catch(SQLException e) {
        	System.out.print("ex:");
        	System.out.print(e.getMessage());	
        	}  
    	
    		return queryResult;
	}
	
	/**
	 * Возвращает строку с суммой расходов за текущий месяц
	 */
	public String getCurrentMonthCost() {
		
		String queryResult = "";
    	Date sqlDate = Date.valueOf(today);
    	Date sqlDate1 = Date.valueOf(firstDayOfMonth);
    	
    	String SQL = "SELECT SUM(" +
    			ConsumptionsTable.Cols.RUB +
    			") FROM " +
    			ConsumptionsTable.NAME +
    					" where " +
    					ConsumptionsTable.Cols.DATE + 
    			" between ? and ?";
    	
    	try(Connection conn = this.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)) {    		
    		pstmt.setDate(1, sqlDate1 );  
    		pstmt.setDate(2, sqlDate);    
    		ResultSet rs = pstmt.executeQuery();    		
    		rs.next();
    		queryResult = String.valueOf(rs.getInt("sum"));
    	}
    	catch(SQLException e) {
        	System.out.print(e.getMessage());	
        }  
    	
    		return queryResult;
	}
	
	/**
	 * Сколько средств осталось на этот месяц. Это разница между доходами прошлого месяца и суммой расходов за текущий месяц.
	 * @return Строка. Количество средств доступных для трат в текущем месяце. 
	 */
	public String getMonthMoneyLeft() {
		
		String queryResult = "";
		int currentMonthLength = today.lengthOfMonth();
		LocalDate lastDayOfMonth = today.withDayOfMonth(currentMonthLength);
    	Date sqlDate1 = Date.valueOf(firstDayOfMonth);    	
    	Date sqlDate2 = Date.valueOf(lastDayOfMonth);
    	
    	LocalDate prevMonthDateFirst = firstDayOfMonth.minusMonths(1);
    	int prevMonthLength = prevMonthDateFirst.lengthOfMonth();
    	LocalDate prevMonthDateLast = prevMonthDateFirst.withDayOfMonth(prevMonthLength);
    	Date sqlDate3 = Date.valueOf(prevMonthDateFirst);    	
    	Date sqlDate4 = Date.valueOf(prevMonthDateLast);
    	
    	
    	String SQL = "SELECT SUM(" +
    			IncomeTable.Cols.RUB +
    			")-(select sum(" + ConsumptionsTable.Cols.RUB + ") from " + ConsumptionsTable.NAME + " tcons"
    					+ " where tcons." + ConsumptionsTable.Cols.DATE + " between ? and ?) AS moneyleft"
    							+ " FROM " + IncomeTable.NAME + " AS inc"
    									+ " WHERE inc." + IncomeTable.Cols.DATE + " BETWEEN ? AND ?";
    	
    	try(Connection conn = this.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)) {    		
    		pstmt.setDate(1, sqlDate1 );  
    		pstmt.setDate(2, sqlDate2);  
    		pstmt.setDate(3, sqlDate3); 
    		pstmt.setDate(4, sqlDate4); 
    		ResultSet rs = pstmt.executeQuery();    		
    		rs.next();
    		queryResult = String.valueOf(rs.getInt("moneyleft"));
    	}
    	catch(SQLException e) {
        	System.out.print(e.getMessage());	
        	}  
    	
    		return queryResult;
	}

	/**
	 * Непредвиденные расходы
	 * @param month расчетный месяц
	 * @return возвращает строку со списком непредвиденных покупок за текущий месяц
	 */
	public String getUnexpSpends(Month month) {
		
		String queryResult = "";
		//Date sqlDateStart = Date.valueOf(firstDayOfMonth);
		//LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
		//Date sqlDateEnd = Date.valueOf(lastDayOfMonth);
		LocalDate  date1= today.withMonth(month.getValue()).withDayOfMonth(1);
		Date sqlDateStart = Date.valueOf(today.withMonth(month.getValue()).withDayOfMonth(1));
		int monthLength = date1.lengthOfMonth();
		Date sqlDateEnd = Date.valueOf(date1.withDayOfMonth(monthLength));
		
		String SQL = "SELECT " +
				ConsumptionsTable.Cols.DATE + ", " +
				ConsumptionsTable.Cols.ITEM + ", " +
				ConsumptionsTable.Cols.CATEGORY + ", " +
				ConsumptionsTable.Cols.RUB + " " +
				" FROM " +
				ConsumptionsTable.NAME +
						" where " +
						ConsumptionsTable.Cols.UNEXP + " = 1 "+
						"AND " + ConsumptionsTable.Cols.DATE + " between ? and ?";
		
		try(Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(SQL)) {    		
			pstmt.setDate(1, sqlDateStart );  
			pstmt.setDate(2, sqlDateEnd);    
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				queryResult = queryResult + "\n" + String.format("%s %s %s %d",
						rs.getDate(ConsumptionsTable.Cols.DATE),
    					rs.getString(ConsumptionsTable.Cols.ITEM),
    					rs.getString(ConsumptionsTable.Cols.CATEGORY),
    					rs.getInt(ConsumptionsTable.Cols.RUB)) + " руб. ";
    		}
		}
		catch(SQLException e) {
	    	System.out.print(e.getMessage());	
	    	}  
		
			return queryResult;
	}
	
	/**
	 * Расходы по категориям за указанный месяц
	 * @param month расчетный месяц
	 * @param rubLimit расходы превышающие эту величину будут учтены. 
	 * @return Строка со списком расходов по категориям
	 */
public String getMonthByCategory(Month month, int rubLimit) {
		
		String queryResult = "";
		int total = 0;
		
		LocalDate  date1= today.withMonth(month.getValue()).withDayOfMonth(1);
		Date sqlDateStart = Date.valueOf(today.withMonth(month.getValue()).withDayOfMonth(1));
		int monthLength = date1.lengthOfMonth();
		Date sqlDateEnd = Date.valueOf(date1.withDayOfMonth(monthLength));
		
		String SQL = "SELECT " +
				ConsumptionsTable.Cols.CATEGORY + ", " +
				" SUM(" + ConsumptionsTable.Cols.RUB + ") AS rub" +
				" FROM " +
				ConsumptionsTable.NAME +
						" where " +
						ConsumptionsTable.Cols.RUB + " > "+ rubLimit +
						" AND " + ConsumptionsTable.Cols.DATE + " between ? and ?"
								+ " GROUP BY " + ConsumptionsTable.Cols.CATEGORY;
		
		try(Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(SQL)) {    		
			pstmt.setDate(1, sqlDateStart );  
			pstmt.setDate(2, sqlDateEnd);    
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int i = rs.getInt(ConsumptionsTable.Cols.RUB);
				queryResult = queryResult + "\n" + String.format("%s %d",
    					rs.getString(ConsumptionsTable.Cols.CATEGORY),
    					i) + " руб. ";
				System.out.println(queryResult);
				total += i;
				i = 0;
    		}
		}
		catch(SQLException e) {
	    	System.out.print(e.getMessage());	
	    	}  
		
		return queryResult + "\n\t ИТОГО: " + total;
	}
	
/**
 * список дорогих (более <b>rubLimit</b>) покупок за указанный месяц
 * @param month месяц
 * @param rubLimit сумма более которой расходы будут отображены
 * @return список расходов строкой
 */
	public String getExpensive(Month month, int rubLimit) {
		
		String queryResult = "";
		int total = 0;
		
		LocalDate  date1= today.withMonth(month.getValue()).withDayOfMonth(1);
		Date sqlDateStart = Date.valueOf(today.withMonth(month.getValue()).withDayOfMonth(1));
		int monthLength = date1.lengthOfMonth();
		Date sqlDateEnd = Date.valueOf(date1.withDayOfMonth(monthLength));
		
		String SQL = "SELECT " +
				ConsumptionsTable.Cols.DATE + ", " +
				ConsumptionsTable.Cols.ITEM + ", " +
				ConsumptionsTable.Cols.CATEGORY + ", " +
				ConsumptionsTable.Cols.RUB + " " +
				" FROM " +
				ConsumptionsTable.NAME +
						" where " +
						ConsumptionsTable.Cols.RUB + " > "+ rubLimit +
						" AND " + ConsumptionsTable.Cols.DATE + " between ? and ?";
		
		try(Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(SQL)) {    		
			pstmt.setDate(1, sqlDateStart );  
			pstmt.setDate(2, sqlDateEnd);    
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int i = rs.getInt(ConsumptionsTable.Cols.RUB);
				queryResult = queryResult + "\n" + String.format("%s %s %s %d",
						rs.getDate(ConsumptionsTable.Cols.DATE),
    					rs.getString(ConsumptionsTable.Cols.ITEM),
    					rs.getString(ConsumptionsTable.Cols.CATEGORY),
    					i) + " руб. ";
				System.out.println(queryResult);
				total += i;
				i = 0;
    		}
		}
		catch(SQLException e) {
	    	System.out.print(e.getMessage());	
	    	}  
		
		return queryResult + "\n ИТОГО: " + total;
	}
	
	/**
	 * Спписок доходов за указанный месяц
	 * @param month
	 * @param rubLimit
	 * @return
	 */
	public String getIncome(Month month, int rubLimit) {
			
			String queryResult = "";
			int total = 0;
			
			//get array of two(2) sqlDate for use in query
			Date[] dates = this.get2Dates(month);
			
			String SQL = "SELECT " +
					IncomeTable.Cols.DATE + ", " +
					IncomeTable.Cols.INCOMER + ", " +
					IncomeTable.Cols.RUB  +
					" FROM " +
					IncomeTable.NAME +
							" WHERE " +
					IncomeTable.Cols.DATE + " between ? and ?";
			
			try(Connection conn = this.connect();
					PreparedStatement pstmt = conn.prepareStatement(SQL)) {    		
				pstmt.setDate(1, dates[0]);  
				pstmt.setDate(2, dates[1]);    
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					int i = rs.getInt(IncomeTable.Cols.RUB);
					queryResult = queryResult + "\n" + String.format("%s %s %d",
							rs.getDate(IncomeTable.Cols.DATE),
	    					rs.getString(IncomeTable.Cols.INCOMER),
	    					i) + " руб. ";
					System.out.println(queryResult);
					total += i;
					i = 0;
	    		}
			}
			catch(SQLException e) {
		    	System.out.print(e.getMessage());	
		    	}  
			
			return queryResult + "\n ИТОГО: " + total;
		}
	
	/**
	 * сравнение доходов и расходов по месяцам
	 * @return Список месяцев. Для каждого месяца общий доход, расход и разница между ними
	 */
	public String getMonthsDifference() {
		
		String queryResult = "";
		int total = 0;
		
		String SQL = "with tcons as (select \r\n" + 
				"to_char(to_timestamp ((extract(month from buy_date)::text), 'MM'), 'TMmon') date,\r\n" + 
				"sum(cons.rub)\r\n" + 
				"from consumptions cons GROUP BY date)\r\n" + 
				"SELECT \r\n" + 
				"tinc.date AS inc_date,\r\n" + 
				"tinc.sum AS inc_sum,\r\n" + 
				"tcons.date AS cons_date,\r\n" + 
				"tcons.sum AS cons_sum, (tinc.sum - tcons.sum) as month_diff  FROM (\r\n" + 
				"SELECT \r\n" + 
				"to_char(to_timestamp ((extract(month from ddate)::text), 'MM'), 'TMmon') date,\r\n" + 
				"sum(inc.rub)\r\n" + 
				"FROM income inc GROUP BY date) as tinc\r\n" + 
				"LEFT JOIN tcons on tcons.date = tinc.date";
		
		try(Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(SQL)) {      
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				//int i = rs.getInt("month_diff");
				queryResult = queryResult + "\n" + String.format("%s %d %s %d %d",
						rs.getString("inc_date"),
    					rs.getInt("inc_sum"),
    					rs.getString("cons_date"),
    					rs.getInt("cons_sum"),
    					rs.getInt("month_diff")
    					) + " руб. ";
				System.out.println(queryResult);
				//total += i;
				//i = 0;
    		}
		}
		catch(SQLException e) {
	    	System.out.print(e.getMessage());	
	    	}  
		
		return queryResult ;
		
	}
	
	public void execQuery(PreparedStatement s) {
		
	}


	public boolean getStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}
	
	/**
	 * Первое и последнее число указанного месяца. Для использования в SQL запросах.
	 * @param month
	 * @return Массив с двумя числами месяца (первым и последним).
	 */
	private Date[] get2Dates(Month month) {
		
		Date[] sqlDates = new Date[2];
		LocalDate  date1= today.withMonth(month.getValue()).withDayOfMonth(1);
		Date sqlDateStart = Date.valueOf(today.withMonth(month.getValue()).withDayOfMonth(1));
		sqlDates[0] = sqlDateStart;
		int monthLength = date1.lengthOfMonth();
		Date sqlDateEnd = Date.valueOf(date1.withDayOfMonth(monthLength));
		sqlDates[1] = sqlDateEnd;
		
		return sqlDates;
	}

}
