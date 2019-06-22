package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import src.DBScheme2.ConsumptionsTable;
import src.DBScheme2.IncomeTable;

public class DBHelper {
	
	private final String url = "jdbc:postgresql://localhost/demo";
    private final String user = "postgres";
    private final String password = "jkl";
    
    private String status;

	
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
	
	public boolean addIncome(int rub, String incomer, Date d) {
		
		int rowsInserted = 0;
		
		DBHelper dbhelper = this;
		String date = new SimpleDateFormat("YYYY-MM-dd").format(d);
		java.sql.Date sqlDate = java.sql.Date.valueOf(date);
		
		String SQL = "INSERT INTO " +
    			IncomeTable.NAME + "(" +
    			IncomeTable.Cols.RUB + ", " +
    			IncomeTable.Cols.INCOMER + ", " +
    			IncomeTable.Cols.DATE +
    			" ) values(?,?,?)";
		
		try(Connection conn = dbhelper.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)){
    		pstmt.setInt(1, rub);
    		pstmt.setString(2, incomer);
    		pstmt.setDate(3, sqlDate);
    		
    		rowsInserted = pstmt.executeUpdate();
    	}
    	catch(SQLException e) {
    		System.out.print("ex:");
    		System.out.print(e.getMessage());
    	} 
		
		return (rowsInserted==0 ? false:true);
	}
	
	public boolean addConsumption(Date d, String item, String category, int rub, int unexp) {
		
		int rowsInserted = 0;
		
		String date = new SimpleDateFormat("YYYY-MM-dd").format(d);
    	java.sql.Date sqlDate = java.sql.Date.valueOf(date);
		
		String SQL = "INSERT INTO " +
				ConsumptionsTable.NAME + "( " +
						 ConsumptionsTable.Cols.DATE + ", " +
						 ConsumptionsTable.Cols.ITEM + ", " + 
						 ConsumptionsTable.Cols.CATEGORY + ", " +
						 ConsumptionsTable.Cols.RUB + ", " +
						 ConsumptionsTable.Cols.UNEXP
						+ " ) values(?,?,?,?,?)";
    	//textArea.setText(SQL);
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
    		System.out.print("ex:");
    		System.out.print(e.getMessage());
    		
    	}  
    	
    	return (rowsInserted==0 ? false:true);
	}
	
	public String getTodayCost() {
		String queryResult = "";
		LocalDate today = LocalDate.now();
    	java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
    	String SQL = "SELECT SUM(" +
    			ConsumptionsTable.Cols.RUB +
    			") FROM " +
    			ConsumptionsTable.NAME +
    					" where " +
    					ConsumptionsTable.Cols.DATE + 
    			" = ?";
    	try(Connection conn = this.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)) {    		
    		pstmt.setDate(1, sqlDate);    		
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
