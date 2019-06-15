package src;

import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PSQLTest {
	
	private final String url = "jdbc:postgresql://localhost/demo";
    private final String user = "postgres";
    private final String password = "jkl";
    
    private final String table = "consumptions";
    
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
 
        return conn;
    }
    
    public void getFood(String pattern, int cost) {
    	String SQL = "SELECT * FROM " +
    					table +
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
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PSQLTest t = new PSQLTest();
		t.getFood("food", 350);
		//t.getFood();
	}

}
