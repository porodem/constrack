package ru.porodem.constrack;


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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

import java.awt.Component;
import javax.swing.JTextArea;

//change import src.DBScheme2 to select right DB
public class PSQLTest extends JFrame{
	
	private final String WARNING_NUM = "В поле сумма (руб.) допустимы только цифры!";
	private final String WARNING_DB_WRITE = "Ошибка записи в базу";	
	
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
		
		comboType = new JComboBox();
		comboType.setModel(new DefaultComboBoxModel(new String[] {"\u0435\u0434\u0430", "\u0445\u043E\u0437\u0442\u043E\u0432\u0430\u0440\u044B", "\u043C\u0435\u0434\u0435\u0446\u0438\u043D\u0430", "\u0441\u0447\u0435\u0442\u0430", "\u0442\u0440\u0430\u043D\u0441\u043F\u043E\u0440\u0442", "\u043E\u0434\u0435\u0436\u0434\u0430", "\u0440\u0430\u0437\u0432\u043B\u0435\u0447\u0435\u043D\u0438\u0435", "\u0434\u0440\u0443\u0433\u043E\u0435"}));
		comboType.setBounds(24, 168, 136, 22);
		getContentPane().add(comboType);
		
		spinDate = new JSpinner();
		spinDate.setModel(new SpinnerDateModel(new Date(), new Date(1560531600000L), new Date(1592154000000L), Calendar.DAY_OF_YEAR));
		//spinDate.set
		spinDate.setBounds(24, 201, 136, 20);
		getContentPane().add(spinDate);
		
		spinIncomeDate = new JSpinner();
		spinIncomeDate.setModel(new SpinnerDateModel(new Date(), new Date(1560531600000L), new Date(1592154000000L), Calendar.MONTH));
		spinIncomeDate.setBounds(205, 96, 121, 20);
		getContentPane().add(spinIncomeDate);
		
		txIncome = new JTextField();
		txIncome.setBounds(205, 32, 86, 20);
		getContentPane().add(txIncome);
		txIncome.setColumns(10);
		
		comboIncomer = new JComboBox();
		comboIncomer.setModel(new DefaultComboBoxModel(new String[] {"\u043C\u0443\u0436", "\u0436\u0435\u043D\u0430", "\u0434\u0440\u0443\u0433\u043E\u0435"}));
		comboIncomer.setBounds(205, 63, 136, 22);
		getContentPane().add(comboIncomer);
		
		
		
		JButton btnAddIncome = new JButton("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u0434\u043E\u0445\u043E\u0434");
		btnAddIncome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addIncome();
			}
		});
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
		
		lbl10cost = new JLabel("0");
		lbl10cost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl10cost.setForeground(Color.BLUE);
		lbl10cost.setBounds(284, 197, 46, 14);
		getContentPane().add(lbl10cost);
		
		JLabel label_7 = new JLabel("\u0420\u0430\u0441\u0445\u043E\u0434\u044B \u0437\u0430 \u043C\u0435\u0441\u044F\u0446");
		label_7.setBounds(182, 222, 96, 14);
		getContentPane().add(label_7);
		
		lblMonthCost = new JLabel("0");
		lblMonthCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMonthCost.setForeground(Color.BLUE);
		lblMonthCost.setBounds(284, 222, 46, 14);
		getContentPane().add(lblMonthCost);
		
		txtStatus = new JLabel("porodem@gmail.com");
		txtStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtStatus.setBounds(24, 430, 351, 14);
		getContentPane().add(txtStatus);
		
		dbhelper = new DBHelper();
		txtStatus.setText(dbhelper.isConnectOk()?"connected" :"conection failed");
		
		textArea = new JTextArea();
		textArea.setBounds(24, 283, 317, 134);
		getContentPane().add(textArea);

	}
    
    DBHelper dbhelper;
   
    private JTextField txCost;
    private JComboBox comboType;
    private JSpinner spinDate;
    private JSpinner spinIncomeDate;
    private JTextField txIncome;
    private JCheckBox chckbxUnexp;
    private JComboBox comboIncomer;
    private JTextField txItem;
    private JLabel lblTodayCost;
    private JLabel lbl10cost;
    JLabel lblMonthCost;
    private JLabel txtStatus;
    private JTextArea textArea;       
    
    String addedTodayLog = "";
    String recordInfo = "";
    
    public void updateTextArea(String newRec) {
    	addedTodayLog = addedTodayLog + newRec + "\n";
    	textArea.setText(addedTodayLog);    	
    }
    
    public void addIncome() {
    	
    	if(!isInteger(txIncome.getText())) {
    		updateTextArea(WARNING_NUM);
    		return;
    	}
    	
    	int rub = this.getIncomeFieldValue();
    	String incomer = (String)comboIncomer.getSelectedItem();
    	Date d = (Date)spinIncomeDate.getValue();
    	LocalDate date= d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	boolean querySuccess = dbhelper.addIncome(rub, incomer, date);
    	if(querySuccess) {
    		updateTextArea("Учтен доход: " + incomer + " " +  rub + " руб.");
    	} else {
    		updateTextArea(WARNING_DB_WRITE);
    	}
    	
    	txItem.setText("");
    	txCost.setText("");
    }    
    
    public void addConsumption() {
    	
    	if(!isInteger(txCost.getText())) {
    		updateTextArea(WARNING_NUM);
    		return;
    	}
    	
    	String category = (String)comboType.getSelectedItem();
    	Date d = (Date)spinDate.getValue();  
    	LocalDate date= d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	int rub = Integer.valueOf(txCost.getText());
    	String item = txItem.getText(); 	
    	showLog(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    	boolean unexpectedCons = chckbxUnexp.isSelected(); 
    	int unexp = unexpectedCons?1:0;
    	
    	
    	boolean querySuccess = dbhelper.addConsumption(date, item, category, rub, unexp);
    	if(querySuccess) {
    		updateTextArea("Учтен расход: " + item + " " + category + " " +  rub + " руб.");
    	} else {
    		updateTextArea(WARNING_DB_WRITE);
    	}  	    	
    	
    	txItem.setText("");
    	txCost.setText("");
    	getSpendStatistic();
    }
    
    public void getSpendStatistic() {    		
    		String todayCost = dbhelper.getTodayCost();		
    		lblTodayCost.setText(todayCost); 
    		
    		String cost10Days = dbhelper.get10daysCost();
    		lbl10cost.setText(cost10Days);
    		
    		String currentMonthCost = dbhelper.getCurrentMonthCost();
    		lblMonthCost.setText(currentMonthCost);
    }
       
    
    public void refreshStatus(String newStatus) {
    	txtStatus.setText(newStatus);
    }
    
    //for debbug
    public void showLog(String msg) {
    	System.out.println("Log: " + msg);
    }
    
    public int getIncomeFieldValue() {
    	return Integer.valueOf(txIncome.getText());
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

	public static void main(String[] args) {
		
		PSQLTest t = new PSQLTest();
		t.setSize(new Dimension(400,500));
		t.setVisible(true);
		t.getSpendStatistic();
		
		
	}
}
