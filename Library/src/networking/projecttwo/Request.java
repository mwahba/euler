package networking.projecttwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
	
	PrintWriter out;
	static BufferedReader br;
	
	//Constructor
	public Request(Socket socket) throws Exception {
		this.socket = socket;
		this.model = new Model();
	}
	
	private String trim(String string) {
		return string.replaceAll("[^A-Za-z0-9]", "");
	}
	
	private int processInput() {
		int result = -1;
		
		String input;
		try {
			input = br.readLine().replaceAll("[^0-9]", "");
			
			while (input.length() < 1) {
				out.println("The input was invalid, please try again: ");
				input = br.readLine().replaceAll("[^0-9]", "");
			}
			
			result = Integer.parseInt(input);
		} catch (IOException e) {
			System.out.println("Unable to get proper input String.");
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static String readFromClient() throws IOException {
		boolean wait = true;
		String clientRequest = "";
		while (wait) {
			if ((clientRequest = br.readLine()).length() > 0) {
				wait = false;
			}
		}
		
		return clientRequest;
	}
	
	private String response(String toSendBack) {
		return toSendBack.replace("\r\n", "|") + "|";
	}
	
	private void processRequest() throws Exception {
		// Get a reference to the socket's input and output streams
		InputStream is = socket.getInputStream();
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		System.out.println("Client connected: " + socket.getRemoteSocketAddress().toString());
		
		out.println("username");
		String username = trim(br.readLine());
		System.out.println(username + " logging in.");
		String[] userResult = model.login(username);
		int userID = Integer.parseInt(userResult[1]);
		out.println(response(userResult[0] + model.checkUpdates(userID)));
		model.updateLastActive(userID);
		
		String command;
		
		do {
			command = br.readLine();
			System.out.println("User " + username + " (" + userID + ") processing command: " + command);
			switch (command.split(" ")[0].toLowerCase()) {
			case "groups":
				out.println(response(model.listGroups(userID)));
				break;
				
			case "updates":
				out.println(response(model.checkUpdates(userID)));
				model.updateLastActive(userID);
				break;
			
			case "users":
				out.println("groupID");
				out.println(response(model.listUsers(processInput())));
				break;
				
			case "join":
				out.println("groupID");
				out.println(response(model.joinGroup(userID, processInput())));
				break;
				
			case "leave":
				out.println("groupID");
				out.println(response(model.leaveGroup(userID, processInput())));
				break;
			
			case "listmessages":
				out.println("groupID");
				out.println(response(model.getListOfMessages(userID, processInput())));
				break;
				
			case "post":
				out.println("groupID");
				int groupID = processInput();
				out.println("subject");
				String subject = readFromClient();
				out.println("content");
				String content = readFromClient();
				out.println(response(model.postMessage(userID, groupID, subject, content)));				
				break;
			
			case "message":
				out.println("messageID");
				int messageID = Integer.parseInt(trim(br.readLine()));
				out.println(response(model.getPost(userID, messageID)));
				break;
				
			case "groupid":
				String groupName;
				if (command.split(" ").length == 1) {
					out.println("groupName");
					groupName = trim(readFromClient());
				} else {
					groupName = command.split(" ")[1];
				}
				out.println(response(model.getGroupID(groupName)));
				break;
				
			case "exit":
				out.println("Server disconnecting...");
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
