import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.result.ResultSetMetaData;



public class GUI  extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFrame window = new JFrame("SQL CLient Application");
	
	private static final int HEIGHT = 700;
	private static final int WIDTH = 1000;
	
	private static final int TOP_AREA_WIDTH = 400;
	private static final int TOP_AREA_HEIGHT = 150;
	
	private static final int INPUT_LAYOUT = 10;

	
	private static final int MARGIN = 10;
	
	private JButton connect, clear, execute , clearSQLWindow;
	
	JPanel connectionArea , buttonArea , SQLArea , SQLResultArea;

	
	JLabel details = new JLabel("Connection details ");
	JLabel filler = new JLabel("hello world!");

	
	JLabel URL = new JLabel("DB URL Propities ");
	String[] dataBases = {"project3.properties","bikeDB.properties"};
	JComboBox<String> pList = new JComboBox<String>(dataBases);

	JLabel userPropities = new JLabel("User Propities ");
	String[] users = {"root.properties","client1.properties","client2.properties"};
	JComboBox<String> userList = new JComboBox<String>(users);


	JLabel username = new JLabel("Username ");
	JTextField usernameInput = new JTextField(INPUT_LAYOUT);
	
	JLabel password = new JLabel("Password ");
	JPasswordField  passwordInput = new JPasswordField (INPUT_LAYOUT);
	
	
	JLabel enter = new JLabel("Enter SQL command\n");
	
	JTextField enterCmd = new JTextField(10);
	
	JLabel resultArea = new JLabel("SQL Result Area");
	
	JLabel conStat = new JLabel("NO CONNECTION");
	

	JTable tbl = new JTable();
	

    JTextArea sqlTextArea = new JTextArea(5, 30);
    
 
    //we have 2 sets. one for the database and one for the password
    Properties propertiesDB = new Properties();
    FileInputStream dataBaseFile = null;
   
    
    
    Properties propertiesUser = new Properties();
    FileInputStream userInfoFile = null;
    MysqlDataSource dataSource = null;
    
    String DBFile;
    String UserFile;
    
    Connection connection;
    Connection operationslog;
    String url = "jdbc:mysql://localhost:3306/operationslog";
    ResultSet resultSet;
    
    DefaultTableModel modle;
	


	JLabel connectedTo = new JLabel("Connected to ");
	
	
	Statement statement;

	
	
	user operationalUser;
	
	Properties operationP = new Properties();
	FileInputStream pFile = null;
    MysqlDataSource pData = null;
	String opFile = "operationslog.properties";
	
	
	
	
	
	
	private boolean connect(String DBFile, String rootFile, String usernameInput,String passwordInput) {
		try {
			//read in all of the properties files
			dataBaseFile = new FileInputStream(DBFile);
			userInfoFile = new FileInputStream(rootFile);
			
			propertiesDB.load(dataBaseFile);
			propertiesUser.load(userInfoFile);
			
			dataSource = new MysqlDataSource();
			
			dataSource.setURL(propertiesDB.getProperty("MYSQL_DB_URL"));
			dataSource.setUser(propertiesUser.getProperty("MYSQL_DB_USERNAME"));
			dataSource.setPassword(propertiesUser.getProperty("MYSQL_DB_PASSWORD"));
			
			
			//this is my genius password validation system
			if(dataSource.getUser().equalsIgnoreCase(usernameInput) && dataSource.getPassword().equalsIgnoreCase(passwordInput)) {
	    		//System.out.println("username: " + dataSource.getUser());
		    	//System.out.println("password: " + dataSource.getPassword());
		    	
		    	String un = dataSource.getUser();
		    	
		    	connection = dataSource.getConnection();
		    	//conet ot op log database as root user. this way we are able to update tables 
		    	try {//log into operations databases
		    		//operationslog = DriverManager.getConnection(url, "root", "password");
		    		
		    		
		    		pFile = new FileInputStream(opFile);
		    		pData = new MysqlDataSource();
		    		
		    		operationP.load(pFile);
		    		pData = new MysqlDataSource();
		    		pData.setURL(operationP.getProperty("MYSQL_DB_URL"));
		        	
		    		pData.setUser(operationP.getProperty("MYSQL_DB_USERNAME"));
		    		pData.setPassword(operationP.getProperty("MYSQL_DB_PASSWORD"));
		    		
		    		
		    		operationslog = pData.getConnection();		    		
		    		
		    		
		    		//System.out.println("log connection good");
		    		//so this is the error line
		    		operationalUser = new user(un, operationslog);
		    		
		    		conStat.setForeground(Color.yellow);
		    	}
		    	catch(Exception e2) {
					JOptionPane.showMessageDialog(null,"Error connection to operations log database", "Error", JOptionPane.ERROR_MESSAGE);

		    	}
		    	

		    	return true;
	    		
	    	}
	    	
	    	else {
	    		//JOptionPane.showMessageDialog(null,"Incorrect username or password", "Error", JOptionPane.ERROR_MESSAGE);
	    		conStat.setText("NOT CONNECTED - User credentails do not match properites file!");
	    		return false;
	    	}
			
			
			
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error connection to database", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
			
		}
		

	}

	
	public GUI(){
		
		
		window.setSize(WIDTH,HEIGHT);
		window.setResizable(false);
	
		window.setLayout(null);
		//**************************
		//top ara
		JPanel top = new JPanel();
		top.setBounds(MARGIN,MARGIN,110,20);
		details.setForeground(Color.blue);
		top.add(details);
		//top.setBackground(c1);
		window.add(top);
		//main connection area
		//**********************************************************************
		connectionArea = new JPanel();
		connectionArea.setBounds(MARGIN,30,TOP_AREA_WIDTH,TOP_AREA_HEIGHT);
		
		//connectionArea.setBackground(c1);
		
		connectionArea.setLayout(new GridLayout(5,2));;
		
		//
		/*
		connectionArea.add(details);
		connectionArea.add(filler);
		
		*/
		
		connectionArea.add(URL);
		
		connectionArea.add(pList);
		

		connectionArea.add(userPropities);
		connectionArea.add(userList);
		
		connectionArea.add(username);
		connectionArea.add(usernameInput);
		
		connectionArea.add(password);
		connectionArea.add(passwordInput);
		passwordInput.setEchoChar('*');

		
		
		window.add(connectionArea);
		//**********************************************************************
		
		//button area
		//**********************************************************************
		buttonArea = new JPanel();
		
		buttonArea.setBounds(MARGIN*2,HEIGHT/4,WIDTH-100,50);
		
		//buttonArea.setBackground(c2);
		
		connect = new JButton("Connect to DataBase");
		connect.addActionListener(this);
		
		clear = new JButton("Clear SQL command");
		clear.addActionListener(this);
		
		execute = new JButton("Execute SQL command");
		execute.addActionListener(this);
		
		
		
		buttonArea.add(connect);
		connect.setForeground(Color.blue);
		//we want to add 5 fillers
		buttonArea.add(clear);
		clear.setForeground(Color.red);
		buttonArea.add(execute);
		execute.setForeground(Color.green);
		
		
		
		window.add(buttonArea);
		//**********************************************************************
		
		//sql command area
		//**********************************************************************
		SQLArea = new JPanel();
		
		SQLArea.setBounds(TOP_AREA_WIDTH + 100,MARGIN,TOP_AREA_WIDTH,TOP_AREA_HEIGHT);
		
		//SQLArea.setBackground(c3);
		
		SQLArea.add(enter);
		enter.setForeground(Color.blue);
		
		sqlTextArea.setWrapStyleWord(true);
        sqlTextArea.setLineWrap(true);
        SQLArea.add(new JScrollPane(sqlTextArea));
		
		
		
		//SQLArea.add(enterCmd);
		
		
		
	

		
		window.add(SQLArea);
		
		//**********************************************************************
		
		
		//connected bar
		//**********************************************************************

        JPanel conStatus = new JPanel();
        
        conStatus.setBounds(MARGIN,245,950,25);
        conStatus.setLayout(getLayout());
        conStat.setForeground(Color.red);
        
        conStatus.setBackground(new Color(0,0,0));
        conStatus.add(conStat);
        
        window.add(conStatus);
        
        
		//**********************************************************************
      
        
        //we need the top label area
        JPanel exeWindow = new JPanel();
        exeWindow.setBounds(MARGIN, 300, TOP_AREA_WIDTH, 25);
        JLabel RW = new JLabel("SQL Execution Result Window");
        RW.setForeground(Color.blue);
        exeWindow.add(RW);
        
        window.add(exeWindow);
        
      //result area
        SQLResultArea = new JPanel();
        
        SQLResultArea.setBounds(10,325,WIDTH-100,300);
        
        Dimension tableSize = new Dimension(800, 290);
        tbl.setPreferredScrollableViewportSize(tableSize);
        
        SQLResultArea.add(new JScrollPane(tbl));

        window.add(SQLResultArea);
        

        JPanel sqlButtons = new JPanel();
        
        sqlButtons.setBounds(MARGIN,625,WIDTH,45);
        
        clearSQLWindow = new JButton("Clear Result Window");
		
        sqlButtons.add(clearSQLWindow);
        clearSQLWindow.addActionListener(this);
        
        
        window.add(sqlButtons);
		
 
		window.setVisible(true);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == connect) {
			connect c = new connect();
			c.actionPerformed(e);
		}
		
		else if(e.getSource() == execute) {
			//check if not connected
			if(connection == null) {
				JOptionPane.showMessageDialog(null,"Need database connection", "Error", JOptionPane.ERROR_MESSAGE);

			}
			
			else {
				executeCmd ex = new executeCmd();
				ex.actionPerformed(e);
			}
			
		}
		
		else if(e.getSource() == clear) {
			clear c = new clear();
			c.actionPerformed(e);
		}
		
		else if (e.getSource() == clearSQLWindow) {
			//System.out.println("Clear sql window");
			
			try {
				modle.setRowCount(0);
				modle.setColumnCount(0);
			}
			
			catch(Exception e2){
				//if the user clears with no data
				
			}

		}
		
	}
	
	class connect implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent e) {
			
	
			//System.out.println("Connecting to database!");
			//we will have the logic to connect to the database here
			
			DBFile = (String)pList.getSelectedItem();
			UserFile = (String)userList.getSelectedItem();
			
			//System.out.println("Files in use: " + DBFile +" " + UserFile);
			
			//validate database connection
			if(connect(DBFile,UserFile,usernameInput.getText(),passwordInput.getText())) {
				conStat.setText(dataSource.getUrl());
				//System.out.println(u.toString());
			}
			
			else {
				//if the databae does not validate for some reason
				JOptionPane.showMessageDialog(null,"Connection Error. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);

			}
			
			
			
			
			}
			
		}
	
	
	class executeCmd implements ActionListener{
		
	
		
		
	
	
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//table.executeQuery(connection, sqlTextArea.getText());
			
		
			try {
				statement = connection.createStatement();

				String command = "";
				
				//here we would decide if we need to execure update or execute query. we will use a query for now just to demo
				
				
				command = sqlTextArea.getText();
				
				//System.out.println("Executing query " + command);

				//determine the type of statment. execute or select
				
				//System.out.println(command.substring(0,6) + " statment");
				
				if(command.substring(0,1).equalsIgnoreCase("s")) {	//since the only statments that start with S are select. 

					//select statment
					//System.out.println("select statement");
					selectstatement sl = new selectstatement();
					sl.executeSelect(command);
				}
				
				else {	//if it dorsent start with s then its problt an update statment
					//update statment
					//System.out.println("update statment");
					updatestatement us = new updatestatement();
					us.executeUpdate(command);
				}
				
			} catch (SQLException e1) {
	    		JOptionPane.showMessageDialog(null, e1.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);

				//e1.printStackTrace();
			}
		}	
}
	
	class clear implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println("Clearing query");
			sqlTextArea.setText("");
			//clears the query, very basic
			
		}
		
}
	
	class updatestatement{
		
		public void executeUpdate(String command) {
			
			//determine if the update is sucesfun
			try {
				
				int rowsEffected = 0;
								
				rowsEffected = statement.executeUpdate(command);
	    		JOptionPane.showMessageDialog(null,rowsEffected + " rows updated", "Sucessful Update", JOptionPane.DEFAULT_OPTION);
	    		
	    		//operations log takes place last to make sure only sucessful transactions
	    		operationalUser.enterUpdate(operationslog);
	    
			}
			
			catch(SQLException e ) {
	    		JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}
	
	
	class selectstatement{
		public void executeSelect(String command) {
			try {

				
				int cols = 0; 

				resultSet = statement.executeQuery (command);
				ResultSetMetaData RSMD = (ResultSetMetaData) resultSet.getMetaData();
				 
				//needs to be casted i guess
				modle = (DefaultTableModel) tbl.getModel();

				 
				 cols = RSMD.getColumnCount();
				 //for some reason we have to use a vector???
				 //arraylist are the same thing java is wack
				 Vector<String> columnNames = new Vector<String>();
				 
				 for(int i = 0; i < cols ; i++) {
				        columnNames.add(RSMD.getColumnName(i+1));

				 }
				 
				 modle.setColumnIdentifiers(columnNames);
				 
				 //i dont antacpate more then 9999 rows so i think we are safe for now
				 //i tried to load this up to max value but my pc blue screened lol
				 String[] row = new String[9999];
				 
				 while(resultSet.next()) {
					 
					 for(int i = 1; i < cols+1; i++) {	//loop through each colum value to add it to the row data
						 
						 row[i-1] = resultSet.getString(i);
					 }
					 
					 modle.addRow(row);
					 
					 
				 }
		    		//operations log takes place last to make sure only sucessful transactions

				 operationalUser.enterQuery(operationslog);
				  
				  
				 
			} catch (SQLException e1) {

	    		JOptionPane.showMessageDialog(null, e1.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);

			}
		}
	}
}





