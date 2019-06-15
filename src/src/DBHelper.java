package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
	
	private final String url = "jdbc:postgresql://localhost/demo";
    private final String user = "postgres";
    private final String password = "jkl";

	
	public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            //txtStatus.setText("connected to DB");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
 
        return conn;
    }
}
