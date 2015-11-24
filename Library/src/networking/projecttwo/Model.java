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

/**
 * CSE 5461 - Mark Wahba (wahba.2@osu.edu) - Programming Assignment 2
 * Model - implementation for database interface. Acting as business object.
 * Please read README for more information.
 * @author Mark Wahba (wahba.2@osu.edu)
 */
public class Model {
	
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver",
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
		String toPrint = "Welcome back " + username + ".";
		
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
		
		// get the date the user was last active
		String lastActive = getString("SELECT DATE_FORMAT(lastActive, '%Y-%c-%d %H:%i:%s') FROM users WHERE id = " + id);
		
		// get a list of ids for the groups that the user is a part of and has not yet left
		List<Integer> userGroups = getListOfIntegerResults("SELECT groupID FROM usersInGroups WHERE userID = " + id + " AND leftDate = 0", 
				"groupID");
		
		// Create new array to hold the users recently joined, left, and new messages posted
		List<Map<String, String>> joined = new ArrayList<Map<String, String>>();
		List<Map<String, String>> left = new ArrayList<Map<String, String>>();
		List<Map<String, String>> posted = new ArrayList<Map<String, String>>();
		
		// for each group that the user is in
		for (int userGroup : userGroups) {
			// find any users who have joined the group
			joined.addAll(getListOfResults("SELECT userID, groupID FROM usersInGroups WHERE joined > STR_TO_DATE('" + lastActive 
					+ "', '%Y-%c-%d %H:%i:%s') AND groupID = " + userGroup + " AND userID != " + id, "userID", "groupID"));
			
			// find any users who have left the group
			left.addAll(getListOfResults("SELECT userID, groupID FROM usersInGroups WHERE leftDate > STR_TO_DATE('" + lastActive 
					+ "', '%Y-%c-%d %H:%i:%s') AND groupID = " + userGroup + " AND userID != " + id, "userID", "groupID"));
			
			// find the messages posted following the last active date, add them to 'posted'
			posted.addAll(getListOfResults("SELECT id, userID, groupID, subject FROM messages WHERE posted > STR_TO_DATE('" + lastActive 
					+ "', '%Y-%c-%d %H:%i:%s') AND groupID = " + userGroup + " AND userID != " + id, "id", "userID", "groupID", "subject"));
		}
		
		// If there are any updates
		if (joined.size() > 0 || left.size() > 0 || posted.size() > 0) {
			// notify the user that updates have occurred
			toPrint += "The following updates have occurred since you were last active: ";
			
			// if a user has joined a group
			if (joined.size() > 0) {
				for (Map<String, String> event : joined) {
					toPrint += "\r\n" + getUserName(Integer.parseInt(event.get("userID"))) + " joined " + getGroupName(Integer.parseInt(event.get("groupID")));
				}
			}
			
			// if a user has left a group
			if (left.size() > 0) {
				for (Map<String, String> event : left) {
					toPrint += "\r\n" + getUserName(Integer.parseInt(event.get("userID"))) + " left " + getGroupName(Integer.parseInt(event.get("groupID")));
				}
			}
			
			// if a message was posted
			if (posted.size() > 0) {
				for (Map<String, String> message : posted) {
					String username = getUserName(Integer.parseInt(message.get("userID"))), 
							groupName = getGroupName(Integer.parseInt(message.get("groupID")));
					toPrint += "\r\n" + username + " (" + message.get("userID") + ") posted a new message in " + groupName + " (" + message.get("groupID") 
							+ "): " + message.get("subject") + " (" + message.get("id") + ")";
				}
			}
		}
		
		return toPrint;
	}

	public String listGroups(int id) {		
		TableHelper table = new TableHelper();
		table.addRow("ID", "Name", "Joined");
		
		List<Map<String, String>> groups = getListOfResults("SELECT * FROM groups LIMIT 0,5", "id", "name");
		List<Integer> currentlyJoined = getListOfIntegerResults("SELECT groupID FROM usersInGroups WHERE userID = " + id 
				+ " AND leftDate = 0;", "groupID");
		
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
			return "You have successfully joined " + groupName + "\r\n\r\nUsers:\r\n" + getUsersForGroup(userID, groupID);
		} else {
			return "You are already part of the group " + groupName + "\r\n\r\nUsers:\r\n" + getUsersForGroup(userID, groupID);
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
			
			List<Map<String, String>> messages = getListOfResults("SELECT id, userID, posted, subject FROM messages WHERE posted >= "
					+ "(SELECT joined FROM usersInGroups WHERE userID = " + userID + " and groupID = " + groupID 
					+ ") AND groupID = " + groupID + " ORDER by posted DESC", "id", "userID", "posted", "subject");
			
			messages.addAll(getListOfResults("SELECT id, userID, posted, subject FROM messages WHERE posted < (SELECT joined FROM "
					+ "usersInGroups WHERE userID = " + userID + " and groupID = " + groupID + ") AND groupID = " + groupID 
					+ " ORDER by posted DESC LIMIT 0,2", "id", "userID", "posted", "subject"));
			
			if (messages.size() > 0) {
				for (Map<String, String> message : messages) {
					table.addRow(message.get("id"), message.get("userID"), message.get("posted"), message.get("subject"));
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
			if (executeUpdate("INSERT INTO messages(userID, groupID, subject, content, posted) VALUES (" + userID + ", " 
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
			List<Map<String, String>> message = getListOfResults("SELECT userID, subject, content, posted FROM messages WHERE id = " 
					+ messageID, "userID", "subject", "content", "posted");
			
			if (message.size() > 0) {
				toPrint += "Subject: " + message.get(0).get("subject") + "\r\n"
						+ "Author: " + message.get(0).get("userID") + "\r\n"
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
	
	public String listUsers(int groupID) {
		TableHelper table = new TableHelper();
		table.addRow("ID", "Username");
		
		List<Map<String, String>> users = getListOfResults("SELECT * FROM users WHERE id in (SELECT userID FROM usersInGroups "
				+ "WHERE groupID = " + groupID + ")", "id", "username");
		
		for (Map<String, String> user : users) {
			table.addRow(user.get("id"), user.get("username"));
		}
		
		return table.toString();
	}
	
	public String getGroupID(String groupIdentifier) {
		if (groupIdentifier.matches("\\d+")) {
			// to verify that group exists, return empty string otherwise
			return getString("SELECT id FROM groups WHERE id = " + groupIdentifier);
		} else {
			List<Integer> idList = getListOfIntegerResults("SELECT id FROM groups WHERE name = '" + groupIdentifier + "'", "id");
			if (idList.size() != 1) {
				return "";
			} else {
				return idList.get(0) + "";
			}
		}
	}
	
	public Model() {}
	
}
