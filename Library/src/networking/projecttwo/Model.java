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
	
	private boolean isUserInGroup(int userID, int groupID) {
		return getNumber("SELECT count(*) FROM  usersInGroups WHERE userID = " + userID + " AND groupID = " + groupID 
				+ " AND leftDate = 0") > 0;
	}
	
	private String getGroupName(int groupID) {
		return getString("SELECT name FROM groups WHERE id = " + groupID);
	}
	
	private String getUserName(int userID) {
		return getString("SELECT username FROM users WHERE id = " + userID);
	}
	
	public String[] login(String username) {
		String toPrint = "Welcome back " + username + ".\r\n";
		
		boolean userExists = getNumber("SELECT COUNT(id) as count FROM users WHERE username = '" + username + "';") > 0;
		if (!userExists) {
			toPrint = username + " was not found, new user created. Welcome " + username + ".";
			executeUpdate("INSERT INTO users (username, joined, lastActive) VALUES('" + username + "', NOW(), NOW());");
		}
		
		return new String[]{toPrint, getNumber("SELECT id FROM users WHERE username = '" + username + "';") + ""};
	}
	
	public void updateLastActive(int id) {
		executeUpdate("UPDATE users SET lastActive = NOW() WHERE id = " + id);
	}

	public String checkUpdates(int id) {
		String toPrint = "";
		
		// Check if new users have joined since last time active
		String statement = " (SELECT lastActive FROM users WHERE id = " + id + ") AND usersInGroups.leftDate != null AND groups.id = "
				+ "usersInGroups.groupID AND groups.id in (SELECT groupID FROM usersInGroups WHERE userID = " + id + ");";
		List<Map<String, String>> joined = getListOfResults("SELECT users.username, users.id, groups.name, groups.id FROM users, groups, "
				+ "usersInGroups WHERE usersInGroups.joined >=" + statement, "users.username", "users.id", "groups.name", "groups.id");
		
		List<Map<String, String>> left = getListOfResults("SELECT users.username, groups.name FROM users, groupID, "
				+ "usersInGroups WHERE usersInGroups.left >= " + statement, "users.username", "groups.name");
		
		List<Map<String, String>> posted = getListOfResults("SELECT * FROM messages WHERE groupID in (SELECT groupID FROM usersInGroups "
				+ "WHERE userID = " + id + ") and posted > (SELECT lastActive FROM users WHERE id = " + id + ") AND author in (SELECT "
						+ "userID FROM usersInGroups where userID = " + id + " AND leftDate = 0)", 
						"users.username", "groups.name", "messages.id", "messages.title");
		
		if (joined.size() > 0 || left.size() > 0 || posted.size() > 0) {
			toPrint += "The following updates have occurred since you were last active: ";
			if (joined.size() > 0) {
				for (Map<String, String> event : joined) {
					toPrint += event.get("users.username") + " joined " + event.get("groups.name") + "\r\n";
				}
			}
			
			if (left.size() > 0) {
				for (Map<String, String> event : left) {
					toPrint += event.get("users.username") + " left " + event.get("groups.name") + "\r\n";
				}
			}
			
			if (posted.size() > 0) {
				for (Map<String, String> message : posted) {
					String username = getUserName(Integer.parseInt(message.get("author"))), 
							groupName = getGroupName(Integer.parseInt(message.get("groupID")));
					toPrint += username + "(" + message.get("author") + " posted a new message in " + groupName + "(" + message.get("group") 
							+ ": " + message.get("subject") + " (" + message.get("id") + ")\r\n";
				}
			}
		} else {
			toPrint = "No new updates have occurred since you were last active.\r\n";
		}
		
		return toPrint;
	}

	

	public String listGroups(int id) {		
		TableHelper table = new TableHelper();
		table.addRow("ID", "Name", "Joined");
		
		List<Map<String, String>> groups = getListOfResults("SELECT * FROM groups LIMIT 0,5", "id", "name");
		List<Integer> currentlyJoined = getListOfIntegerResults("SELECT groupID FROM usersInGroups WHERE userID = " + id 
				+ " AND leftDate = 0;", "group");
		
		for (Map<String, String> group : groups) {
			if (currentlyJoined.contains(Integer.parseInt(group.get("id")))) {
				table.addRow(group.get("id"), group.get("name"), "Yes");
			} else {
				table.addRow(group.get("id"), group.get("name"));
			}
		}
		
		return table.toString();
	}

	public String joinGroup(int userID, int groupID) {
		String groupName = getGroupName(groupID);
		// if user is not a part of the group
		if (!isUserInGroup(userID, groupID)) {
			if (getNumber("SELECT count(*) from usersInGroups WHERE userID = " + userID + " and groupID = " + groupID) > 0) {
				executeUpdate("UPDATE usersInGroups SET joined = NOW(), leftDate = date('0000-00-00') WHERE userID = " + userID 
						+ " AND groupID = " + groupID);
			} else {
				executeUpdate("INSERT INTO usersInGroups (userID, groupID, joined) VALUES (" + userID + ", " + groupID + ", NOW())");
			}
			return "You have successfully joined " + groupName + "\r\n" + getUsersForGroup(userID, groupID);
		} else {
			return "You are already part of the group " + groupName + "\r\n" + getUsersForGroup(userID, groupID);
		}
	}

	public String leaveGroup(int userID, int groupID) {
		// if user is part of the group
		if (isUserInGroup(userID, groupID)) {
			executeUpdate("UPDATE usersInGroups SET leftDate = NOW() WHERE userID = " + userID + " AND groupID = " + groupID);
			return "You have successfully left the group " + getGroupName(groupID) + ".";
		} else {
			return "You are not a member of the group.";
		}
	}
	
	public String getUsersForGroup(int userID, int groupID) {
		String result;
		
		// if user is part of the group
		if (isUserInGroup(userID, groupID)) {
			TableHelper table = new TableHelper();
			table.addRow("id", "username");
			
			List<Map<String, String>> users = getListOfResults("SELECT id, username FROM users WHERE id in (SELECT userID FROM usersInGroups "
					+ "WHERE groupID = " + groupID + ")", "id", "username");
			for (Map<String, String> user : users) {
				table.addRow(user.get("id"), user.get("username"));
			}
			
			result = table.toString();
		} else {
			result = "You are not part of the group " + getGroupName(groupID) 
				+ " you need to be a member of the group if you would like to see the list of users.";
		}
		
		return result;
	}
	
	public String getListOfMessages(int userID, int groupID) {
		String result;
		
		if (isUserInGroup(userID, groupID)) {
			TableHelper table = new TableHelper();
			table.addRow("Message ID", "Sender", "Post Date", "Subject");
			
			List<Map<String, String>> messages = getListOfResults("SELECT id, author, posted, subject FROM messages WHERE posted >= "
					+ "(SELECT joined FROM usersInGroups WHERE userID = " + userID + " and groupID = " + groupID 
					+ ") ORDER by posted DESC;", "id", "author", "posted", "subject");
			
			messages.addAll(getListOfResults("SELECT id, author, posted, subject FROM messages WHERE posted < (SELECT joined FROM "
					+ "usersInGroups WHERE userID = " + userID + " and groupID = " + groupID + ") ORDER by posted DESC LIMIT 0,2;",
					"id", "author", "posted", "subject"));
			
			if (messages.size() > 0) {
				for (Map<String, String> message : messages) {
					table.addRow(message.get("id"), message.get("author"), message.get("posted"), message.get("subject"));
				}
				result = table.toString();
			} else {
				result = "No messages exist in " + getGroupName(groupID);
			}
		} else {
			result = "You are not part of the group " + getGroupName(groupID)
				+ " you need to be a member of the group if you would like to see a list of messages in the group.";
		}
		
		return result;
	}
	
	public String postMessage(int userID, int groupID, String subject, String content) {
		String result;
		
		if (isUserInGroup(userID, groupID)) {
			if (executeUpdate("INSERT INTO messages(author, groupID, subject, content, posted) VALUES (" + userID + ", " 
					+ groupID + ", '" + subject + "', '" + content + "', NOW())")) {
				result = "Your message has been posted successfully to " + getGroupName(groupID) + "\r\n" 
						+ getListOfMessages(userID, groupID);
			} else {
				result = "There was an issue posting your message, please try again.";
			}
		} else {
			result = "You are not part of " + getGroupName(groupID) 
				+ ". You need to be a member of the group if you would like to post to it.";
		}
		
		return result;
	}
	
	public String getPost(int userID, int messageID) {
		String toPrint = "";
		
		// get group ID for message
		int groupID = getNumber("SELECT groupID FROM messages WHERE id = " + messageID);
		
		// check proper permissions to read message
		if (isUserInGroup(userID, groupID)) {
			List<Map<String, String>> message = getListOfResults("SELECT author, subject, content, posted FROM messages WHERE id = " 
					+ messageID, "author", "subject", "content", "posted");
			
			if (message.size() > 0) {
				toPrint += "Subject: " + message.get(0).get("subject") + "\r\n"
						+ "Author: " + message.get(0).get("author") + "\r\n"
						+ "Posted: " + message.get(0).get("posted") + "\r\n"
						+ "Content: " + message.get(0).get("content");
			} else {
				toPrint = "There was an error retrieving message with ID: " + messageID;
			}
		} else {
			toPrint = "You are not a member of the group that contains this message. You must be a member to see the message.";
		}
		
		return toPrint;
	}
	
	public String listUsers() {
		TableHelper table = new TableHelper();
		table.addRow("ID", "Username");
		
		List<Map<String, String>> users = getListOfResults("SELECT * FROM users", "id", "username");
		
		for (Map<String, String> user : users) {
			table.addRow(user.get("id"), user.get("username"));
		}
		
		return table.toString();
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
