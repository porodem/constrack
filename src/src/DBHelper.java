package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	
	public void execQuery(PreparedStatement s) {
		
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
}
