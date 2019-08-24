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

//change import src.DBScheme2 to select right DB (in DBHelper.java) !!!

public class PSQLTest extends JFrame implements ItemListener{	
	
	
	public PSQLTest() {
		
		//* * * * *  GUI ELEMENTS * * * * * *
		
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
				btnShowIncomes.setText("Доходы");
				btnShowCategMnth.setText("по категориям");
			}
		});
		btnResetReports.setBounds(295, 188, 120, 23);
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
		label_3.setBounds(180, 140, 52, 14);
		getContentPane().add(label_3);
		lblTodayCost = new JLabel("0");
		lblTodayCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTodayCost.setForeground(Color.BLACK);
		lblTodayCost.setBounds(241, 139, 45, 14);
		getContentPane().add(lblTodayCost);
		
		JLabel label_5 = new JLabel("10 дней");
		label_5.setBounds(180, 184, 46, 14);
		getContentPane().add(label_5);
		
		lbl10cost = new JLabel("0");
		lbl10cost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl10cost.setForeground(Color.BLUE);
		lbl10cost.setBounds(240, 183, 46, 14);
		getContentPane().add(lbl10cost);
		
		JLabel label_7 = new JLabel("месяц");
		label_7.setBounds(180, 207, 46, 14);
		getContentPane().add(label_7);
		
		lblMonthCost = new JLabel("0");
		lblMonthCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMonthCost.setForeground(Color.BLUE);
		lblMonthCost.setBounds(240, 206, 46, 14);
		getContentPane().add(lblMonthCost);
		
		txtStatus = new JLabel("porodem@gmail.com");
		txtStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtStatus.setBounds(24, 526, 351, 14);
		getContentPane().add(txtStatus);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setDropMode(DropMode.INSERT);
		textArea.setBounds(10, 277, 365, 238);
		getContentPane().add(textArea);
		
		JLabel label_4 = new JLabel("Осталось");
		label_4.setBounds(180, 230, 57, 14);
		getContentPane().add(label_4);
		
		lblMoneyLeft = new JLabel("0");
		lblMoneyLeft.setBounds(241, 229, 46, 14);
		getContentPane().add(lblMoneyLeft);
		
		btnShowUnexp = new JButton("непредвиденное");
		btnShowUnexp.setBounds(295, 36, 120, 23);
		btnShowUnexp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showUnexpSpends();
			}
		});
		getContentPane().add(btnShowUnexp);
		
		btnShowExpensive = new JButton(">1000");
		btnShowExpensive.setBounds(295, 60, 120, 23);
		btnShowExpensive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showExpensive();
			}
		});
		getContentPane().add(btnShowExpensive);
		
		btnShowCategMnth = new JButton("по категориям");
		btnShowCategMnth.setBounds(295, 135, 120, 23);
		btnShowCategMnth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMonthByCat();
			}
		});
		getContentPane().add(btnShowCategMnth);
		
		btnShowIncomes = new JButton("Доходы");
		btnShowIncomes.addActionListener(new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				showIncome();
			}
		});
		btnShowIncomes.setBounds(295, 110, 120, 23);
		getContentPane().add(btnShowIncomes);
		
		btnDiff = new JButton("сравнение");
		btnDiff.setBounds(295, 161, 120, 23);
		btnDiff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showDifference();
			}
		});
		getContentPane().add(btnDiff);
		
		JButton btnShowPrevMonths = new JButton("расх. прошл. мес");
		btnShowPrevMonths.setBounds(295, 85, 120, 23);
		getContentPane().add(btnShowPrevMonths);
		
		radioCost = new JRadioButton("расход");
		radioCost.setBounds(24, 10, 70, 23);
		radioCost.addItemListener(this);
		radioCost.setEnabled(true);
		radioCost.setSelected(true);
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
		label_2.setBounds(296, 14, 60, 14);
		getContentPane().add(label_2);		
		
		JLabel label_6 = new JLabel("вчера");
		label_6.setBounds(180, 163, 46, 14);
		getContentPane().add(label_6);
		
		lblYesterday = new JLabel("0");
		lblYesterday.setBounds(241, 161, 46, 14);
		getContentPane().add(lblYesterday);
		
		JLabel label_8 = new JLabel("прш. мес");
		label_8.setBounds(181, 250, 55, 14);
		getContentPane().add(label_8);
		
		JLabel lblPrevMonth = new JLabel("0");
		lblPrevMonth.setBounds(240, 249, 46, 14);
		getContentPane().add(lblPrevMonth);
		
		// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		
		queryMonth = LocalDate.now().getMonth();
		
		context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		
				dbhelper = context.getBean("beanDBHelper",DBHelper.class);
				
				if(dbhelper.isConnectOk()) {
					txtStatus.setText("connected");
					txtStatus.setForeground(Color.GREEN);
				} else {
					txtStatus.setText(ERROR_DB_CONNECTION);
					txtStatus.setForeground(Color.RED);
				}

	}
   
    private JButton btnShowExpensive;
    private JButton btnShowUnexp;
    private JButton btnShowIncomes;
    private JButton btnShowCategMnth;
    private JButton btnDiff;
    
    private JTextField txSum;
    private JComboBox comboType;
    private JSpinner spinDate;
    private JCheckBox chckbxUnexp;
    private JComboBox comboIncomer;
    private JTextField txItem;
    private JLabel lblTodayCost;
    private JLabel lblYesterday;
    private JLabel lbl10cost;
    private JLabel lblMonthCost;
    private JLabel lblMoneyLeft;
    private JLabel txtStatus;
    private JLabel label_2;
    private JTextArea textArea; 
    
    private JRadioButton radioIncome;
    private JRadioButton radioCost;
    private JRadioButton radioQuery;
    
    private final String WARNING_NUM = "В поле сумма (руб.) допустимы только цифры!";
	public static final String WARNING_DB_WRITE = "Ошибка записи в базу";	
	private final String ERROR_DB_CONNECTION = "Ошибка подключения к базе";	
	
	private static final int BTN_IN_USE_EXPENSIVE = 1;
	private static final int BTN_IN_USE_CATEGORY = 2;
	private static final int BTN_IN_USE_INCOME = 3;
	private static final int BTN_IN_USE_UNEXPECTED = 4;
	
	/**
	 * идентификатор кнопки которая может нажиматься несколько раз для получения различных результатов. Одна из констант, например: {@link PSQLTest#BTN_IN_USE_EXPENSIVE}
	 */
	private int btnInUse = 0;	
	
	/**
	 * Определяет, что собой представляют цифры введенные в поле "сумма".
	 * Может быть "<b>income</b>"(доход) или "<b>cost</b>"(расход).
	 */
	private String rubInputType = "";
	
	Month queryMonth;
	
	ClassPathXmlApplicationContext context;
    
    DBHelper dbhelper;
    
    String addedTodayLog = "";
    String recordInfo = "";
    
    public static void main(String[] args) {
		
		PSQLTest t = new PSQLTest();
		t.setSize(new Dimension(440,590));
		t.setVisible(true);
		t.getSpendStatistic();
		
	}
    
    /**
     * Обновляет текст в поле информации
     * @param newRec Строка которая добавится присоединится к строке {@link PSQLTest#addedTodayLog}
     */
    private void updateTextArea(String newRec) {
    	addedTodayLog = addedTodayLog + newRec + "\n";
    	textArea.setText(addedTodayLog); 
    }
    
    /**
     * Очищает текстовое поле
     */
    private void cleanTextArea() {
    	addedTodayLog = "";
    	textArea.setText(addedTodayLog); 
    }
    
    /**
     * Добавление записи о доходе в БД.
     */
    private void addIncome() {
    	
    	if(!ConstrackService.isInteger(txSum.getText())) {
    		updateTextArea(WARNING_NUM);
    		return;
    	}
    	
    	int rub = this.getSumFieldValue();
    	String incomer = (String)comboIncomer.getSelectedItem();
    	Date d = (Date)spinDate.getValue();
    	LocalDate date= d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	String querySuccess = dbhelper.addIncome(rub, incomer, date);
    	
    	updateTextArea(querySuccess);
    	
    	txItem.setText("");
    	txSum.setText("");
    }    
    
    /**
     * Добавление в БД записи о расходе
     */
    private void addConsumption() {
    	
    	if(!ConstrackService.isInteger(txSum.getText())) {
    		updateTextArea(WARNING_NUM);
    		return;
    	}
    	
    	String category = (String)comboType.getSelectedItem();
    	Date d = (Date)spinDate.getValue();  
    	LocalDate date = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	int rub = Integer.valueOf(txSum.getText());
    	String item = txItem.getText(); 	
    	boolean unexpectedCons = chckbxUnexp.isSelected(); 
    	int unexp = unexpectedCons?1:0;    	
    	
    	String queryResult = dbhelper.addConsumption(date, item, category, rub, unexp);
    	
    	updateTextArea(queryResult);    	
    	txItem.setText("");
    	txSum.setText("");
    	getSpendStatistic();
    }
    
    /**
     * Действие кнопки "Непредвиденные расходы"
     */
    private void showUnexpSpends() {  
    	
    	if(btnInUse != BTN_IN_USE_UNEXPECTED) {
        	btnInUse = BTN_IN_USE_UNEXPECTED;
        	queryMonth = LocalDate.now().getMonth();
			btnShowCategMnth.setText("по категориям");
			btnShowIncomes.setText("доходы");
			btnShowExpensive.setText("> 1000");
    	}
    	
    	String unexpSpendMonth = dbhelper.getUnexpSpends(queryMonth);
    	String monthString = queryMonth.getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, new java.util.Locale("ru", "RU"));
    	updateTextArea("Непердвиденные расходы за " + queryMonth + "\n");
    	updateTextArea(unexpSpendMonth);  
    	btnShowUnexp.setText(monthString);
    	queryMonth = queryMonth.minus(1);
    }
    
    /**
     * Действие для кнопки "Показать дорогие расходы"
     */
    private void showExpensive() {
    	
    	//check which button was used last time, it other then reset month to current month
    	if(btnInUse != BTN_IN_USE_EXPENSIVE) {
        	btnInUse = BTN_IN_USE_EXPENSIVE;
        	queryMonth = LocalDate.now().getMonth();
			btnShowCategMnth.setText("по категориям");
			btnShowIncomes.setText("доходы");
    	}
    	
    	int rubSum = 0;
    	
    	if(!ConstrackService.isInteger(txSum.getText())) {
    		//updateTextArea(WARNING_NUM);
    		rubSum = 1000;
    	} else {
    		rubSum = this.getSumFieldValue();
    	} 	   	
    	
    	String expeniveList = dbhelper.getExpensive(queryMonth, rubSum);
    	String monthString = queryMonth.getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, new java.util.Locale("ru", "RU"));
    	cleanTextArea();
    	
    	updateTextArea("\n" + "расходы " + monthString +  " больше " + rubSum + "\n");
    	updateTextArea(expeniveList);
    	queryMonth = queryMonth.minus(1);
    	ConstrackService.showLog(queryMonth.toString());
    	btnShowExpensive.setText(monthString + " > " + rubSum );
    }
    
    /**
     * Для кнопки "Расходы по категориям за месяц"
     */
    private void showMonthByCat() {
    	
    	if(btnInUse != BTN_IN_USE_CATEGORY) {
        	btnInUse = BTN_IN_USE_CATEGORY;
        	queryMonth = LocalDate.now().getMonth();
        	btnShowExpensive.setText(">1000");
        	btnShowIncomes.setText("доходы");
    	}
    	
    	String result = dbhelper.getMonthByCategory(queryMonth, 1);
    	String monthString = queryMonth.getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, new java.util.Locale("ru", "RU"));
    	cleanTextArea();
    	
    	updateTextArea("\n" + "расходы " + monthString + " по категориям\n");
    	updateTextArea(result);
    	queryMonth = queryMonth.minus(1);
    	ConstrackService.showLog(queryMonth.toString());
    	btnShowCategMnth.setText(monthString );
    }
    
    /**
     * Для кнопки "Показать доходы"
     */
    private void showIncome() {
    	
    	if(btnInUse != BTN_IN_USE_INCOME) {
        	btnInUse = BTN_IN_USE_INCOME;
        	queryMonth = LocalDate.now().getMonth();
        	btnShowExpensive.setText(">1000");
        	btnShowCategMnth.setText("по категориям");
    	}
    	
    	String result = dbhelper.getIncome(queryMonth, 1);
    	
    	String monthString = queryMonth.getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("ru", "RU"));
    	cleanTextArea();
    	
    	updateTextArea("\n" + "доходы " +  monthString +"\n");
    	updateTextArea(result);
    	queryMonth = queryMonth.minus(1);
    	ConstrackService.showLog(queryMonth.toString() + " " + monthString);
    	btnShowIncomes.setText(monthString );
    }
    
    /**
     * Для кнопки "Разница между доходом и расходом"
     */
    private void showDifference() {
    	String result = dbhelper.getMonthsDifference();
    	updateTextArea(result);
    }
    
    /**
     * Обновляет статистику расходов за разные промежутки времени и отображает их.
     */
    private void getSpendStatistic() {    		
    		String todayCost = dbhelper.getTodayCost(LocalDate.now());		
    		lblTodayCost.setText(todayCost); 
    		
    		String yesterdayCost = dbhelper.getTodayCost(LocalDate.now().minusDays(1));
    		System.out.println(yesterdayCost);
    		lblYesterday.setText(yesterdayCost); 
    		
    		String cost10Days = dbhelper.get10daysCost();
    		lbl10cost.setText(cost10Days);
    		
    		String currentMonthCost = dbhelper.getCurrentMonthCost();
    		lblMonthCost.setText(currentMonthCost);
    		
    		String moneyLeft = dbhelper.getMonthMoneyLeft();
    		lblMoneyLeft.setText(moneyLeft);
    } 
    
    private void refreshStatus(String newStatus) {
    	txtStatus.setText(newStatus);
    }
    
    
    
    private int getSumFieldValue() {
    	return Integer.valueOf(txSum.getText());
    }
    
    
    

	

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(radioCost.isSelected()) {
			rubInputType = "cost";
			ConstrackService.showLog("cost");
			isRadioCost(true, false);
			
		} else if(radioIncome.isSelected()) {
			rubInputType = "income";
			ConstrackService.showLog("income");
			isRadioCost(false, false);
		}
		else if(radioQuery.isSelected()) {
			rubInputType = "query";
			ConstrackService.showLog("query");
			isRadioCost(true, true);
		}
		
	}
	
	/**
	 * Определяет доступ к элементам интерфейса, в зависимости от положений radiobutton
	 * @param isCost <code>true</code> если включен режим записи расходов <code>false</code> если режим записи доходов
	 * @param isQuery включен ли режим отчета
	 */
	private void isRadioCost(boolean isCost, boolean isQuery) {		
		boolean b = isQuery?isCost:!isCost;
		comboIncomer.setEnabled(b);
		comboType.setEnabled(isCost);
		chckbxUnexp.setEnabled(isCost);
		txItem.setEnabled(isCost);
	}
}
