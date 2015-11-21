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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Model {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver",
			DB_URL = "jdbc:mysql://localhost/board",
			USER = "user", PASS = "password";
	
	private static SessionFactory factory;
	
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
	
	private int getNumber(String statementString) {
		int result = 0;
		Connection connection = createConnection();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(statementString);
			resultSet.next();
			result = resultSet.getInt(1);
			resultSet.close();
			connection.close();
			statement.close();
		} catch(SQLException se) {
			System.out.println("Error on attempting to get numerical value from database: " + se.getMessage());
			se.printStackTrace();
		}
		
		return result;
	}
	
	private String getString(String statementString) {
		String result = "";
		Connection connection = createConnection();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(statementString);
			resultSet.next();
			result = resultSet.getString(1);
			resultSet.close();
			connection.close();
			statement.close();
		} catch(SQLException se) {
			System.out.println("Error on attempting to get numerical value from database: " + se.getMessage());
			se.printStackTrace();
		}
		
		return result;
	}
	
	private List<Map<String, String>> getListOfResults(String statementString, String... toExtract) {
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		Connection connection = createConnection();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultsSet = statement.executeQuery(statementString);
			
			while (resultsSet.next()) {
				Map<String, String> currentRow = new HashMap<String, String>();
				for (String key : toExtract) {
					currentRow.put(key, resultsSet.getString(key));
				}
				results.add(currentRow);
			}
			
			resultsSet.close();
			statement.close();
			connection.close();
		} catch(SQLException se) {
			System.out.println("Error attempting to get list of list of results");
			se.printStackTrace();
		}
		return results;
	}
	
	private List<Integer> getListOfIntegerResults(String statementString, String toExtract) {
		List<Integer> results = new ArrayList<Integer>();
		Connection connection = createConnection();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultsSet = statement.executeQuery(statementString);
			
			while (resultsSet.next()) {
				results.add(resultsSet.getInt(toExtract));
			}
			
			resultsSet.close();
			statement.close();
			connection.close();
		} catch(SQLException se) {
			System.out.println("Error attempting to get list of list of results");
			se.printStackTrace();
		}
		return results;
	}
	
	public String[] login(String username) {
		String toPrint = "Welcome back " + username + ".\r\n";
		
		boolean userExists = getNumber("SELECT COUNT(id) as count FROM user WHERE username = '" + username + "';") > 0;
		if (!userExists) {
			toPrint = username + " was not found, new user created. Welcome " + username + ".\r\n";
			executeUpdate("INSERT INTO user(username, joined, lastActive) VALUES('" + username + "', NOW(), NOW());");
		}
		
		return new String[]{toPrint, getNumber("SELECT id FROM user WHERE username = '" + username + "';") + ""};
	}
	
	public void updateLastActive(int id) {
		executeUpdate("UPDATE user SET lastActive = NOW() WHERE id = " + id);
	}

	public String checkUpdates(int id) {
		String toPrint = "";
		
		// Check if new users have joined since last time active
		String statement = "SELECT user.username, user.id, `group`.name, `group`.id FROM user, `group`, userInGroup WHERE userInGroup.joined "
				+ ">= (SELECT lastActive FROM user WHERE id = " + id + ") AND userInGroup.`left` != null AND `group`.id = "
				+ "userInGroup.`group` AND `group`.id in (SELECT `group` FROM userInGroup WHERE user = " + id + ");";
		List<Map<String, String>> updates = getListOfResults(statement, "user.username", "user.id", "group.name", "group.id");
		
		if (updates.size() > 0) {
			toPrint += "The following updates have occurred since you were last active: ";
			for (Map<String, String> row : updates) {
				toPrint += row.get("user.username") + " (" + row.get("user.id") + " joined " + row.get("group.name") + " (" + row.get("group.id") + ")\r\n";
			}
		}
		
		return toPrint;
	}

	public String listGroups(int id) {		
		TableHelper table = new TableHelper();
		table.addRow("ID", "Name", "Joined");
		
		List<Map<String, String>> groups = getListOfResults("SELECT * FROM `group`;", "id", "name");
		List<Integer> currentlyJoined = getListOfIntegerResults("SELECT `group` FROM userInGroup WHERE user = " + id + " AND `left` = 0;", "group");
		
		for (Map<String, String> group : groups) {
			if (currentlyJoined.contains(Integer.parseInt(group.get("id")))) {
				table.addRow(group.get("id"), group.get("name"), "Yes");
			} else {
				table.addRow(group.get("id"), group.get("name"));
			}
		}
		
		return table.toString() + "\r\n";
	}

	public String joinGroup(int userID, int groupID) {
		if (getNumber("SELECT count(*) from userInGroup WHERE user = " + userID + " and `group` = " + groupID) > 0) {
			executeUpdate("UPDATE userInGroup SET joined = NOW() AND left is null");
		} else {
			executeUpdate("INSERT INTO userInGroup (user, `group`, joined) VALUES (" + userID + ", " + groupID + ", NOW())");
		}
		
		String groupName = getString("SELECT name FROM `group` WHERE id = " + groupID);	
		return "You have successfully joined " + groupName + "\r\n";
	}

	public void leaveGroup(int id, int groupID) {
		// TODO Auto-generated method stub
		
	}
	
	public Model() {
		/*try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (HibernateException e) {
			System.out.println("Encountered error attempting to create factory: " + e.getMessage());
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}*/
	}
}
