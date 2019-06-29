package ru.porodem.constrack;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
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

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.DropMode;

//change import src.DBScheme2 to select right DB
public class PSQLTest extends JFrame implements ItemListener{
	
	private final String WARNING_NUM = "В поле сумма (руб.) допустимы только цифры!";
	public static final String WARNING_DB_WRITE = "Ошибка записи в базу";	
	private final String ERROR_DB_CONNECTION = "Ошибка подключения к базе";	
	private String rubInputType = "";
	
	Month queryMonth;
	
	public PSQLTest() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Constrack");
		getContentPane().setLayout(null);
		
		ButtonGroup btnGroup = new ButtonGroup();
		
		txSum = new JTextField();
		txSum.setBounds(24, 96, 86, 20);
		getContentPane().add(txSum);
		txSum.setColumns(10);
		
		JButton btnRunQuery = new JButton("Go");
		btnRunQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				switch(rubInputType) {
				case "income": addIncome();
					break;
				case "cost": addConsumption();
					break;
				}
			}
		});
		btnRunQuery.setBounds(24, 232, 136, 23);
		getContentPane().add(btnRunQuery);
		
		JButton btnResetReports = new JButton("сброс");
		btnResetReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				queryMonth =  LocalDate.now().getMonth();
				cleanTextArea();
				btnShowExpensive.setText(">1000");
			}
		});
		btnResetReports.setBounds(273, 139, 89, 23);
		getContentPane().add(btnResetReports);
		
		comboType = new JComboBox();
		comboType.setModel(new DefaultComboBoxModel(new String[] {"\u0435\u0434\u0430", "\u0445\u043E\u0437\u0442\u043E\u0432\u0430\u0440\u044B", "\u043C\u0435\u0434\u0435\u0446\u0438\u043D\u0430", "\u0441\u0447\u0435\u0442\u0430", "\u0442\u0440\u0430\u043D\u0441\u043F\u043E\u0440\u0442", "\u043E\u0434\u0435\u0436\u0434\u0430", "\u0440\u0430\u0437\u0432\u043B\u0435\u0447\u0435\u043D\u0438\u0435", "\u0434\u0440\u0443\u0433\u043E\u0435"}));
		comboType.setBounds(24, 168, 136, 22);
		getContentPane().add(comboType);
		
		spinDate = new JSpinner();
		spinDate.setModel(new SpinnerDateModel(new Date(), new Date(1560531600000L), new Date(1592154000000L), Calendar.DAY_OF_YEAR));
		//spinDate.set
		spinDate.setBounds(24, 201, 136, 20);
		getContentPane().add(spinDate);
		
		comboIncomer = new JComboBox();
		comboIncomer.setModel(new DefaultComboBoxModel(new String[] {"\u043C\u0443\u0436", "\u0436\u0435\u043D\u0430", "\u0434\u0440\u0443\u0433\u043E\u0435"}));
		comboIncomer.setBounds(102, 36, 106, 22);
		getContentPane().add(comboIncomer);
		
		chckbxUnexp = new JCheckBox("\u041D\u0435\u043F\u0440\u0435\u0434\u0432\u0438\u0434\u0435\u043D\u043D\u044B\u0439");
		chckbxUnexp.setBounds(97, 10, 133, 23);
		getContentPane().add(chckbxUnexp);
		
		txItem = new JTextField();
		txItem.setBounds(24, 130, 136, 20);
		getContentPane().add(txItem);
		txItem.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u041A\u0430\u0442\u0435\u0433\u043E\u0440\u0438\u044F");
		lblNewLabel.setBounds(24, 153, 86, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("\u0440\u0443\u0431.");
		lblNewLabel_1.setBounds(120, 99, 27, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel label = new JLabel("сумма");
		label.setBounds(24, 81, 46, 14);
		getContentPane().add(label);
		
		JLabel label_3 = new JLabel("сегодня");
		label_3.setBounds(178, 161, 52, 14);
		getContentPane().add(label_3);
		lblTodayCost = new JLabel("0");
		lblTodayCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTodayCost.setForeground(Color.BLACK);
		lblTodayCost.setBounds(235, 162, 45, 14);
		getContentPane().add(lblTodayCost);
		
		JLabel label_5 = new JLabel("10 дней");
		label_5.setBounds(178, 184, 46, 14);
		getContentPane().add(label_5);
		
		lbl10cost = new JLabel("0");
		lbl10cost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl10cost.setForeground(Color.BLUE);
		lbl10cost.setBounds(235, 184, 46, 14);
		getContentPane().add(lbl10cost);
		
		JLabel label_7 = new JLabel("месяц");
		label_7.setBounds(178, 207, 46, 14);
		getContentPane().add(label_7);
		
		lblMonthCost = new JLabel("0");
		lblMonthCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMonthCost.setForeground(Color.BLUE);
		lblMonthCost.setBounds(235, 207, 46, 14);
		getContentPane().add(lblMonthCost);
		
		txtStatus = new JLabel("porodem@gmail.com");
		txtStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtStatus.setBounds(24, 526, 351, 14);
		getContentPane().add(txtStatus);
		
		
		context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		
		//dbhelper = new DBHelper();
		dbhelper = context.getBean("beanDBHelper",DBHelper.class);
		
		if(dbhelper.isConnectOk()?true:false) {
			txtStatus.setText("connected");
			txtStatus.setForeground(Color.GREEN);
		} else {
			txtStatus.setText(ERROR_DB_CONNECTION);
			txtStatus.setForeground(Color.RED);
		}
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setDropMode(DropMode.INSERT);
		textArea.setBounds(10, 266, 365, 249);
		getContentPane().add(textArea);
		
		JLabel label_4 = new JLabel("Осталось");
		label_4.setBounds(178, 230, 48, 14);
		getContentPane().add(label_4);
		
		lblMoneyLeft = new JLabel("0");
		lblMoneyLeft.setBounds(236, 230, 46, 14);
		getContentPane().add(lblMoneyLeft);
		
		JButton btnShowUnexp = new JButton("непредвиденное");
		btnShowUnexp.setBounds(227, 35, 136, 23);
		btnShowUnexp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showUnexpSpends();
			}
		});
		getContentPane().add(btnShowUnexp);
		
		btnShowExpensive = new JButton(">1000");
		btnShowExpensive.setBounds(227, 60, 136, 23);
		btnShowExpensive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showExpensive();
			}
		});
		getContentPane().add(btnShowExpensive);
		
		JButton btnShowIncomes = new JButton("Доходы");
		btnShowIncomes.setBounds(227, 112, 136, 23);
		getContentPane().add(btnShowIncomes);
		
		JButton btnShowPrevMonths = new JButton("расх. прошл. мес");
		btnShowPrevMonths.setBounds(227, 85, 136, 23);
		getContentPane().add(btnShowPrevMonths);
		
		radioCost = new JRadioButton("расход");
		radioCost.setBounds(24, 10, 70, 23);
		radioCost.addItemListener(this);
		radioCost.setEnabled(true);
		getContentPane().add(radioCost);
		
		radioIncome = new JRadioButton("доход");
		radioIncome.setBounds(24, 35, 67, 23);
		radioIncome.addItemListener(this);		
		getContentPane().add(radioIncome);		
		
		radioQuery = new JRadioButton("отчет");
		radioQuery.setBounds(24, 60, 109, 23);
		radioQuery.addItemListener(this);
		getContentPane().add(radioQuery);
		
		JLabel label_1 = new JLabel("товар");
		label_1.setBounds(24, 116, 46, 14);
		getContentPane().add(label_1);
		
		btnGroup.add(radioCost);
		btnGroup.add(radioIncome);
		btnGroup.add(radioQuery);
		
		label_2 = new JLabel("ОТЧЕТЫ");
		label_2.setBounds(269, 14, 60, 14);
		getContentPane().add(label_2);		
		
		queryMonth = LocalDate.now().getMonth();

	}
	
	ClassPathXmlApplicationContext context;
    
    DBHelper dbhelper;
   
    private JButton btnShowExpensive;
    
    private JTextField txSum;
    private JComboBox comboType;
    private JSpinner spinDate;
    private JCheckBox chckbxUnexp;
    private JComboBox comboIncomer;
    private JTextField txItem;
    private JLabel lblTodayCost;
    private JLabel lbl10cost;
    private JLabel lblMonthCost;
    private JLabel lblMoneyLeft;
    private JLabel txtStatus;
    private JTextArea textArea; 
    
    private JRadioButton radioIncome;
    private JRadioButton radioCost;
    private JRadioButton radioQuery;
    
    String addedTodayLog = "";
    String recordInfo = "";
    private JLabel label_2;
    
    
    public void updateTextArea(String newRec) {
    	addedTodayLog = addedTodayLog + newRec + "\n";
    	textArea.setText(addedTodayLog); 
    }
    
    public void cleanTextArea() {
    	addedTodayLog = "";
    	textArea.setText(addedTodayLog); 
    }
    
    public void addIncome() {
    	
    	if(!isInteger(txSum.getText())) {
    		updateTextArea(WARNING_NUM);
    		return;
    	}
    	
    	int rub = this.getSumFieldValue();
    	String incomer = (String)comboIncomer.getSelectedItem();
    	Date d = (Date)spinDate.getValue();
    	LocalDate date= d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	boolean querySuccess = dbhelper.addIncome(rub, incomer, date);
    	
    	if(querySuccess) {
    		updateTextArea("Учтен доход: (" + incomer + ") " +  rub + " руб.");
    	} else {
    		updateTextArea(WARNING_DB_WRITE);
    	}
    	
    	txItem.setText("");
    	txSum.setText("");
    }    
    
    public void addConsumption() {
    	
    	if(!isInteger(txSum.getText())) {
    		updateTextArea(WARNING_NUM);
    		return;
    	}
    	
    	String category = (String)comboType.getSelectedItem();
    	Date d = (Date)spinDate.getValue();  
    	LocalDate date= d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	int rub = Integer.valueOf(txSum.getText());
    	String item = txItem.getText(); 	
    	showLog(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    	boolean unexpectedCons = chckbxUnexp.isSelected(); 
    	int unexp = unexpectedCons?1:0;    	
    	
    	String queryResult = dbhelper.addConsumption(date, item, category, rub, unexp);
    	updateTextArea(queryResult);  	    	
    	
    	txItem.setText("");
    	txSum.setText("");
    	getSpendStatistic();
    }
    
    public void showUnexpSpends() {
    	String unexpSpendMonth = dbhelper.getUnexpSpends();
    	updateTextArea("Непердвиденные расходы\n");
    	updateTextArea(unexpSpendMonth);
    }
    
    public void showExpensive() {
    	
    	int rubSum = 0;
    	
    	if(!isInteger(txSum.getText())) {
    		//updateTextArea(WARNING_NUM);
    		rubSum = 1000;
    	} else {
    		rubSum = this.getSumFieldValue();
    	} 	   	
    	
    	String expeniveList = dbhelper.getExpensive(queryMonth, rubSum);
    	String monthString = queryMonth.getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, new java.util.Locale("Cyrylic", "Cyrylic"));
    	cleanTextArea();
    	
    	updateTextArea("\n" + "расходы больше " + rubSum + " за месяц " +  monthString +"\n");
    	updateTextArea(expeniveList);
    	queryMonth = queryMonth.minus(1);
    	this.showLog(queryMonth.toString());
    	btnShowExpensive.setText(monthString + " > " + rubSum );
    }
    
    public void getSpendStatistic() {    		
    		String todayCost = dbhelper.getTodayCost();		
    		lblTodayCost.setText(todayCost); 
    		
    		String cost10Days = dbhelper.get10daysCost();
    		lbl10cost.setText(cost10Days);
    		
    		String currentMonthCost = dbhelper.getCurrentMonthCost();
    		lblMonthCost.setText(currentMonthCost);
    		
    		String moneyLeft = dbhelper.getMonthMoneyLeft();
    		lblMoneyLeft.setText(moneyLeft);
    }
      
    
    
    
    public void refreshStatus(String newStatus) {
    	txtStatus.setText(newStatus);
    }
    
    //for debbug
    public void showLog(String msg) {
    	System.out.println("Log: " + msg);
    }
    
    public int getSumFieldValue() {
    	return Integer.valueOf(txSum.getText());
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
		t.setSize(new Dimension(400,590));
		t.setVisible(true);
		t.getSpendStatistic();
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(radioCost.isSelected()) {
			rubInputType = "cost";
			showLog("cost");
			isRadioCost(true, false);
			
		} else if(radioIncome.isSelected()) {
			rubInputType = "income";
			showLog("income");
			isRadioCost(false, false);
		}
		else if(radioQuery.isSelected()) {
			rubInputType = "query";
			showLog("query");
			isRadioCost(true, true);
		}
		
	}
	
	//set access for right elements
	private void isRadioCost(boolean isCost, boolean isQuery) {		
		boolean b = isQuery?isCost:!isCost;
		comboIncomer.setEnabled(b);
		comboType.setEnabled(isCost);
		chckbxUnexp.setEnabled(isCost);
		txItem.setEnabled(isCost);
	}
}
