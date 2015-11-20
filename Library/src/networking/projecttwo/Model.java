package networking.projecttwo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver",
			DB_URL = "jdbc:mysql://localhost/board",
			USER = "user", PASS = "password";
	
	private Connection createConnection() {
		Connection connection = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch(SQLException se) {
			System.out.println("Error on creating connection: " + se.getMessage());
			se.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error on creating connection");
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException se) {
				System.out.println("Error on closing connection following exception.");
			}
		}
		
		return connection;
	}
	
	private boolean executeUpdate(String statementString) {
		boolean result = true;
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement();
			statement.executeUpdate(statementString);
		} catch (SQLException se) {
			result = false;
			System.out.println("SQL Error on attempting to execute: " + statementString);
			se.printStackTrace();
		} catch (Exception e) {
			result = false;
			System.out.println("Error on attempting to execute: " + statementString);
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
		}
		
		return result;
	}
	
	private int getNumber(String statementString, String toExtract) {
		int result = 0;
		Connection connection = createConnection();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(statementString);
			resultSet.next();
			result = resultSet.getInt(toExtract);
			resultSet.close();
			connection.close();
			statement.close();
		} catch(SQLException se) {
			System.out.println("Error on attempting to get numerical value from database: " + se.getMessage());
			se.printStackTrace();
		}
		
		return result;
	}
	
	private List<Map<String, String>> getListOfResults(String statementString, List<String> keyList) {
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		Connection connection = createConnection();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultsSet = statement.executeQuery(statementString);
			
			while (resultsSet.next()) {
				Map<String, String> currentRow = new HashMap<String, String>();
				for (String key : keyList) {
					currentRow.put(key, resultsSet.getString(key));
				}
				results.add(currentRow);
			}
			
			resultsSet.close();
			statement.close();
			connection.close();
		} catch(SQLException se) {
			System.out.println("Error attempting to get list of list of results");
			se.getMessage();
		}
		return results;
	}
	
	public int login(String username) {
		boolean userExists = getNumber("SELECT COUNT(id) as count FROM users WHERE username = '" + username + "';", "count") > 0;
		if (!userExists) {
			executeUpdate("INSERT INTO Users(username, joined, lastActive) VALUES('" + username + "', NOW(), NOW());");
		}
		return getNumber("SELECT id FROM users WHERE username = '" + username + "';", "id");
	}
	
	public void updateLastActive(int id) {
		executeUpdate("UPDATE users SET lastActive = NOW() WHERE id = " + id);
	}

	public String checkUpdates(int id) {
		String toPrint = "";
		
		// Check if new users have joined since last time active
		List<String> keyList = new ArrayList<String>();
		keyList.add("users.username");
		keyList.add("groups.name");
		
		String statement = "SELECT Users.username, Groups.name FROM Users, Groups, UsersInGroups WHERE UsersInGroups.joined >= "
				+ "(SELECT lastActive FROM Users WHERE id = " + id + ") AND UsersInGroups.left NOT NULL AND Groups.id = "
				+ "UsersInGroups.group AND Groups.id in (SELECT group FROM UsersInGroups WHERE user = " + id + ")";
		List<Map<String, String>> updates = getListOfResults(statement, keyList);
		
		if (updates.size() > 0) {
			toPrint += "The following updates have occurred since you were last gone: ";
			for (Map<String, String> row : updates) {
				toPrint += row.get("users.username") + " joined " + row.get("groups.name");
			}
		}
		
		return toPrint;
	}

	public void listGroups(int id) {
		// TODO Auto-generated method stub
		
	}

	public void joinGroup(int id, int groupID) {
		// TODO Auto-generated method stub
		
	}

	public void leaveGroup(int id, int groupID) {
		// TODO Auto-generated method stub
		
	}
}
