//user class to log operations info

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class user {
	
	String loginUsername;
	int numQueries;
	int numUpdates;
	 
	 
	
	
	public user(String loginUsername, Connection con)  {
		
		this.loginUsername = loginUsername;
		
		
			
			try {
				this.numQueries = getNumQ(con);
				this.numUpdates = getNumU(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		
		
	}
	
public void enterUpdate(Connection connection) {
		
		try {
			Statement statement = connection.createStatement();
			numUpdates++;
			
			String query = "update operationscount set num_updates = "+ numUpdates + " where login_username ="+ "'" + loginUsername +"'";
			//update operationscount set num_queries = 1 where login_username = 'root';
			
			statement.executeUpdate(query);
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
}
	
	
	
	//when a user enteried a wuery its recored here
	public void enterQuery(Connection connection) {
		//establish statement connection
		
		try {
			Statement statement = connection.createStatement();
			numQueries++;
			
			String query = "update operationscount set num_queries = "+ numQueries + " where login_username ="+ "'" + loginUsername +"'";
			//update operationscount set num_queries = 1 where login_username = 'root';
			
			statement.executeUpdate(query);
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	
	private int getNumQ(Connection con) throws SQLException {
		Statement statement = con.createStatement();
		
		//usenername is found from outsdie method. now we will get the updates and qerues ran
		String getNumQ = "select num_queries from operationscount where login_username ="+ "'" + loginUsername +"'";
		ResultSet resultSet = statement.executeQuery (getNumQ);
		if(resultSet.next()) {
			int r = resultSet.getInt("num_queries");
			return r;
		}
		
		return -1;
	
		
	}
	
	private int getNumU(Connection con) throws SQLException {
		Statement statement = con.createStatement();
		
		//usenername is found from outsdie method. now we will get the updates and qerues ran
		String getNumQ = "select num_updates from operationscount where login_username ="+ "'" + loginUsername +"'";
		ResultSet resultSet = statement.executeQuery (getNumQ);
		
		if(resultSet.next()) {
			int r = resultSet.getInt("num_updates");
			return r;
		}
		
		
		//this.numQueries = 0;
	
		return -1;
	}
	
	
	
	
	
	
	public String toString() {
		return "username " + loginUsername + " queries ran " + numQueries + " updates made " + numUpdates;
	}

}
