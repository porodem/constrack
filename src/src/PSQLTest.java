package src;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerDateModel;

import src.DBScheme.ConsumptionsTable;
import src.DBScheme.ConsumptionsTable.*;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;


public class PSQLTest extends JFrame{
	
	public PSQLTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Constrack");
		getContentPane().setLayout(null);
		
		txCost = new JTextField();
		txCost.setBounds(24, 32, 86, 20);
		getContentPane().add(txCost);
		txCost.setColumns(10);
		
		JButton btnAddRec = new JButton("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u0440\u0430\u0441\u0445\u043E\u0434");
		btnAddRec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addConsumption();
			}
		});
		btnAddRec.setBounds(24, 232, 136, 23);
		getContentPane().add(btnAddRec);
		
		txtField = new JTextField();
		txtField.setBounds(24, 283, 351, 136);
		getContentPane().add(txtField);
		txtField.setColumns(10);
		
		txtStatus = new JTextPane();
		txtStatus.setText("status");
		txtStatus.setBounds(24, 430, 351, 20);
		getContentPane().add(txtStatus);
		
		comboType = new JComboBox();
		comboType.setModel(new DefaultComboBoxModel(new String[] {"\u0435\u0434\u0430", "\u0445\u043E\u0437\u0442\u043E\u0432\u0430\u0440\u044B", "\u043C\u0435\u0434\u0435\u0446\u0438\u043D\u0430", "\u0441\u0447\u0435\u0442\u0430", "\u0442\u0440\u0430\u043D\u0441\u043F\u043E\u0440\u0442", "\u043E\u0434\u0435\u0436\u0434\u0430", "\u0440\u0430\u0437\u0432\u043B\u0435\u0447\u0435\u043D\u0438\u0435", "\u0434\u0440\u0443\u0433\u043E\u0435"}));
		comboType.setBounds(24, 168, 136, 22);
		getContentPane().add(comboType);
		
		spinDate = new JSpinner();
		spinDate.setModel(new SpinnerDateModel(new Date(1560531600000L), new Date(1560531600000L), new Date(1592154000000L), Calendar.DAY_OF_YEAR));
		spinDate.setBounds(24, 201, 136, 20);
		getContentPane().add(spinDate);
		
		txIncome = new JTextField();
		txIncome.setBounds(205, 32, 86, 20);
		getContentPane().add(txIncome);
		txIncome.setColumns(10);
		
		JComboBox comboIncomer = new JComboBox();
		comboIncomer.setModel(new DefaultComboBoxModel(new String[] {"\u043C\u0443\u0436", "\u0436\u0435\u043D\u0430", "\u0434\u0440\u0443\u0433\u043E\u0435"}));
		comboIncomer.setBounds(205, 63, 136, 22);
		getContentPane().add(comboIncomer);
		
		JSpinner spinIncomeDate = new JSpinner();
		spinIncomeDate.setModel(new SpinnerDateModel(new Date(1560531600000L), new Date(1560531600000L), new Date(1592154000000L), Calendar.MONTH));
		spinIncomeDate.setBounds(205, 96, 121, 20);
		getContentPane().add(spinIncomeDate);
		
		JButton btnAddIncome = new JButton("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u0434\u043E\u0445\u043E\u0434");
		btnAddIncome.setBounds(205, 127, 136, 23);
		getContentPane().add(btnAddIncome);
		
		chckbxUnexp = new JCheckBox("\u041D\u0435\u043F\u0440\u0435\u0434\u0432\u0438\u0434\u0435\u043D\u043D\u044B\u0439");
		chckbxUnexp.setBounds(24, 59, 136, 23);
		getContentPane().add(chckbxUnexp);
		
		txItem = new JTextField();
		txItem.setBounds(24, 106, 136, 20);
		getContentPane().add(txItem);
		txItem.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u041A\u0430\u0442\u0435\u0433\u043E\u0440\u0438\u044F");
		lblNewLabel.setBounds(24, 149, 86, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("\u0440\u0443\u0431.");
		lblNewLabel_1.setBounds(120, 35, 46, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel label = new JLabel("\u0442\u043E\u0432\u0430\u0440");
		label.setBounds(24, 89, 46, 14);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u0440\u0430\u0441\u0445\u043E\u0434");
		label_1.setBounds(24, 11, 46, 14);
		getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("\u0434\u043E\u0445\u043E\u0434");
		label_2.setBounds(205, 11, 46, 14);
		getContentPane().add(label_2);
		
		JLabel label_3 = new JLabel("\u0420\u0430\u0441\u0445\u043E\u0434\u044B \u0441\u0435\u0433\u043E\u0434\u043D\u044F");
		label_3.setBounds(182, 172, 96, 14);
		getContentPane().add(label_3);
		
		lblTodayCost = new JLabel("0");
		lblTodayCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTodayCost.setForeground(Color.BLACK);
		lblTodayCost.setBounds(284, 172, 57, 14);
		getContentPane().add(lblTodayCost);
		
		JLabel label_5 = new JLabel("\u0420\u0430\u0441\u0445\u043E\u0434\u044B 10 \u0434\u043D\u0435\u0439");
		label_5.setBounds(182, 197, 96, 14);
		getContentPane().add(label_5);
		
		JLabel lbl10cost = new JLabel("0");
		lbl10cost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl10cost.setForeground(Color.BLUE);
		lbl10cost.setBounds(284, 197, 46, 14);
		getContentPane().add(lbl10cost);
		
		JLabel label_7 = new JLabel("\u0420\u0430\u0441\u0445\u043E\u0434\u044B \u0437\u0430 \u043C\u0435\u0441\u044F\u0446");
		label_7.setBounds(182, 222, 96, 14);
		getContentPane().add(label_7);
		
		JLabel lblMonthCost = new JLabel("0");
		lblMonthCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMonthCost.setForeground(Color.BLUE);
		lblMonthCost.setBounds(284, 222, 46, 14);
		getContentPane().add(lblMonthCost);
	}
	
	private final String url = "jdbc:postgresql://localhost/demo";
    private final String user = "postgres";
    private final String password = "jkl";
   
    private JTextField txCost;
    private JTextField txtField;
    private JTextPane txtStatus;
    private JComboBox comboType;
    private JSpinner spinDate;
    private JTextField txIncome;
    private JCheckBox chckbxUnexp;
    private JTextField txItem;
    private JLabel lblTodayCost;
    
    
    public void addConsumption() {
    	String category = (String)comboType.getSelectedItem();
    	Date d = (Date)spinDate.getValue();
    	String date = new SimpleDateFormat("YYYY-MM-dd").format(d);
    	java.sql.Date sqlDate = java.sql.Date.valueOf(date);
    	int rub = Integer.valueOf(txCost.getText());
    	String item = txItem.getText();
    	
    	
    	
    	boolean unexpectedCons = chckbxUnexp.isSelected(); 
    	int unexp = unexpectedCons?1:0;
    	  	
    	txtStatus.setText(category + " " + date + " " + rub + " chkbx:"+ unexpectedCons);
    	
    	String SQL = "INSERT INTO " +
				ConsumptionsTable.NAME + "( " +
						 ConsumptionsTable.Cols.DATE + ", " +
						 ConsumptionsTable.Cols.ITEM + ", " + 
						 ConsumptionsTable.Cols.CATEGORY + ", " +
						 ConsumptionsTable.Cols.RUB + ", " +
						 ConsumptionsTable.Cols.UNEXP
						+ " ) values(?,?,?,?,?)";
    	txtField.setText(SQL);
    	DBHelper db = new DBHelper();
    	try(Connection conn = db.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)){
    		pstmt.setDate(1, sqlDate);
    		pstmt.setString(2, item);
    		pstmt.setString(3, category);
    		pstmt.setInt(4, rub);
    		pstmt.setInt(5, unexp);
    		
    		ResultSet rs = pstmt.executeQuery();
    	}
    	catch(SQLException e) {
    		System.out.print("ex:");
    		System.out.print(e.getMessage());
    	}  
    	
    	txItem.setText("");
    	txCost.setText("");
    	getTodayCost();
    	
    }
    
    public void getTodayCost() {
    	//String today = new SimpleDateFormat("YYYY-MM-dd").format(new Date().getTime());
    	LocalDate today = LocalDate.now();
    	java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
    	String SQL = "SELECT SUM(" +
    			ConsumptionsTable.Cols.RUB +
    			") FROM " +
    			ConsumptionsTable.NAME +
    					" where " +
    					ConsumptionsTable.Cols.DATE + 
    			" = ?";
    	DBHelper db = new DBHelper();
    	try(Connection conn = db.connect();
    			PreparedStatement pstmt = conn.prepareStatement(SQL)) {
    		
    		pstmt.setDate(1, sqlDate);
    		
    		ResultSet rs = pstmt.executeQuery();
    		
    		rs.next();
    		
    		
    		//System.out.println(String.valueOf(rs.getInt(ConsumptionsTable.Cols.RUB)));
    		lblTodayCost.setText(String.valueOf(rs.getInt("sum")));
    		
    	} catch(SQLException e) {
    		System.out.print("ex:");
    		System.out.print(e.getMessage());
    	}  	    
    }
    
    public void getFood(String pattern, int cost) {
    	String SQL = "SELECT * FROM " +
    			ConsumptionsTable.NAME +
    					" where RUB > ? and ITEM = ?";
    	DBHelper db = new DBHelper();
    	try(Connection conn = db.connect();
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
    
    public void refreshStatus(String newStatus) {
    	txtStatus.setText(newStatus);
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DBHelper dbh = new DBHelper();
		//dbh.connect();
		PSQLTest t = new PSQLTest();
		t.setSize(new Dimension(400,500));
		t.setVisible(true);
		t.getTodayCost();
		//t.getFood();
	}
}
