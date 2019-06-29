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

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.porodem.constrack.DBScheme2.ConsumptionsTable;
import ru.porodem.constrack.DBScheme2.IncomeTable;


/**@author Dolgopolov Anatoliy */
public class DBHelper {
	
	/* implemented with applicationContext.xml
	private final String url = "jdbc:postgresql://localhost/demo";
    private final String user = "postgres";
    private final String password = "jkl";
    */
	
	private String url;
	private String user;
	private String password;
	
    private String status;
    
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
            setStatus("connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
 
        return conn;
    }
	
	public boolean isConnectOk() {
		boolean status = false;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            status = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
 
        return status;
    }
	
	public boolean addIncome(int rub, String incomer, LocalDate date) {
		
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
		
		return (rowsInserted==0 ? false:true);
	}
	
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
	
	public String getTodayCost() {
		
		String queryResult = "";
    	java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.now());
    	
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

//непредвиденные расходы
	public String getUnexpSpends() {
		
		String queryResult = "";
		Date sqlDateStart = Date.valueOf(firstDayOfMonth);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
		Date sqlDateEnd = Date.valueOf(lastDayOfMonth);
		
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
	
	//запрос на крупные покупки 
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
	
	//TO DO fix for propriate result
	 public String getFood(String pattern, int cost) {
		 
		 	String result = "";
		 
	    	String SQL = "SELECT * FROM " +
	    			ConsumptionsTable.NAME +
	    					" where RUB > ? and ITEM = ?";
	    	try(Connection conn = this.connect();
	    			PreparedStatement pstmt = conn.prepareStatement(SQL)) {
	    		
	    		pstmt.setInt(1, cost);
	    		pstmt.setString(2, pattern);
	    		
	    		ResultSet rs = pstmt.executeQuery();
	    		
	    		while(rs.next()) {
	    			System.out.println(String.format("%s %d",
	    					rs.getString("item"),
	    					rs.getInt("rub")));
	    		}
	    		
	    	} catch(SQLException e) {
	    		System.out.print("ex:");
	    		System.out.print(e.getMessage());
	    	}  	 
	    	
	    	return "some string";
	    }
	
	public void execQuery(PreparedStatement s) {
		
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	//check if user input only numbers for RUB value
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

}
