package networking.projecttwo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

/**
 * CSE 5461 - Mark Wahba (wahba.2@osu.edu) - Programming Assignment 2
 * Request - Implementation for request established from listening server.
 * Please read README for more information.
 * @author Mark Wahba (wahba.2@osu.edu)
 */
final class Request implements Runnable {
	final static String CRLF = "\r\n", USERNAME = "user", PASSWORD = "password",
			LOGIN = "login";
	
	Socket socket;
	Model model;
	
	DataOutputStream out;
	static BufferedReader br;
	
	Map<String, Integer> groupIDs;
	
	//Constructor
	public Request(Socket socket) throws Exception {
		this.socket = socket;
		this.model = new Model();
	}
	
	private String trim(String string) {
		return string.replaceAll("[^A-Za-z0-9]", "");
	}
	
	private static String readFromClient() throws IOException {
		boolean wait = true;
		String clientRequest = "";
		while (wait) {
			if ((clientRequest = br.readLine()).length() > 0) {
				wait = false;
			}
		}
		
		return clientRequest.trim().replace("'", "\'");
	}
	
	private String response(String toSendBack) {
		return toSendBack.replace("\r\n", "|") + "|";
	}
	
	private void reply(String toSendBack) throws IOException {
		out.writeBytes(toSendBack + "\r\n");
	}
	
	private int getGroupID(String groupName) {
		if (groupIDs.containsKey(groupName)) {
			return groupIDs.get(groupName);
		} else if (groupIDs.containsValue(groupName)) {
			return Integer.parseInt(groupName);
		}
		return 0;
	}
	private void processRequest() throws Exception {
		// Get a reference to the socket's input and output streams
		InputStream is = socket.getInputStream();
		out = new DataOutputStream(socket.getOutputStream());
		br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		System.out.println("Client connected: " + socket.getRemoteSocketAddress().toString());
		
		reply("username");
		String username = trim(br.readLine());
		System.out.println(username + " logging in.");
		String[] userResult = model.login(username);
		int userID = Integer.parseInt(userResult[1]);
		reply(response(userResult[0] + model.checkUpdates(userID)));
		model.updateLastActive(userID);
		
		String command;
		
		groupIDs = model.getGroupIDs();
		
		do {
			command = br.readLine();
			System.out.println("User " + username + " (" + userID + ") processing command: " + command);
			switch (command.split(" ")[0].toLowerCase()) {
			case "groups":
				reply(response(model.listGroups(userID)));
				break;
				
			case "updates":
				reply(response(model.checkUpdates(userID)));
				model.updateLastActive(userID);
				break;
			
			case "users":
				reply("groupID");
				reply(response(model.listUsers(getGroupID(readFromClient()))));
				break;
				
			case "join":
				reply("groupID");
				reply(response(model.joinGroup(userID, getGroupID(readFromClient()))));
				break;
				
			case "leave":
				reply("groupID");
				reply(response(model.leaveGroup(userID, getGroupID(readFromClient()))));
				break;
			
			case "listmessages":
				reply("groupID");
				reply(response(model.getListOfMessages(userID, getGroupID(readFromClient()))));
				break;
				
			case "post":
				reply("groupID");
				int groupID = getGroupID(readFromClient());
				reply("subject");
				String subject = readFromClient();
				reply("content");
				String content = readFromClient();
				reply(response(model.postMessage(userID, groupID, subject, content)));				
				break;
			
			case "message":
				reply("messageID");
				int messageID = Integer.parseInt(trim(br.readLine()));
				reply(response(model.getPost(userID, messageID)));
				break;
				
			case "groupid":
				String groupName = command.split(" ")[1];
				if (groupIDs.containsKey(groupName)) {
					reply(groupIDs.get(groupName) + "");
				} else if (groupIDs.containsValue(groupName)) {
					reply(groupName);
				}
				break;
				
			case "exit":
				reply("Server disconnecting...");
				break;
				
			default:
				break;
			}
		} while (!command.equalsIgnoreCase("exit"));
		
		System.out.println("Client disconnected: " + socket.getRemoteSocketAddress().toString());
		
		out.close();
		br.close();
		socket.close();
	}
	
	// Implement the run() method of the Runnable interface
	public void run() {
		try {
			processRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
