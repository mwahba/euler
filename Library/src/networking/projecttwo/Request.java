package networking.projecttwo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

final class Request implements Runnable {
	final static String CRLF = "\r\n", USERNAME = "user", PASSWORD = "password",
			LOGIN = "login";
	
	Socket socket;
	Model model;
	
	DataOutputStream os;
	BufferedReader br;
	
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
				os.writeBytes("The input was invalid, please try again: ");
				input = br.readLine().replaceAll("[^0-9]", "");
			}
			
			result = Integer.parseInt(input);
		} catch (IOException e) {
			System.out.println("Unable to get proper input String.");
			e.printStackTrace();
		}
		
		return result;
	}
	
	private void processRequest() throws Exception {
		// Get a reference to the socket's input and output streams
		InputStream is = socket.getInputStream();
		os = new DataOutputStream(socket.getOutputStream());
		br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		System.out.println("Client connected: " + socket.getRemoteSocketAddress().toString());
		
		os.writeBytes("Enter a username: ");
		
		String[] userResult = model.login(trim(br.readLine()));
		os.writeBytes(userResult[0]);
		int userID = Integer.parseInt(userResult[1]);
		
		os.writeBytes(model.checkUpdates(userID));
		
		String command;
		
		do {
			os.writeBytes(CRLF + CRLF + "Enter command: ");
			command = br.readLine();
			model.updateLastActive(userID);
			
			switch (command.toLowerCase()) {
			case "list":
			case "groups":
				os.writeBytes(model.listGroups(userID));
				break;
				
			case "updates":
				os.writeBytes(model.checkUpdates(userID));
				break;
			
			case "users":
				os.writeBytes(model.listUsers());
				break;
				
			case "join":
				os.writeBytes("Group ID to join: ");
				os.writeBytes(model.joinGroup(userID, processInput()));
				break;
				
			case "leave":
				os.writeBytes("Group ID to leave: ");
				os.writeBytes(model.leaveGroup(userID, processInput()));
				break;
			
			case "messages":
				os.writeBytes("Group ID: ");
				os.writeBytes(model.getListOfMessages(userID, processInput()));
				break;
				
			case "post":
				os.writeBytes("Enter Group ID to post to: ");
				int groupID = processInput();
				os.writeBytes("Subject: ");
				String subject = br.readLine();
				os.writeBytes("Content: ");
				String content = br.readLine();
				os.writeBytes(model.postMessage(userID, groupID, subject, content));				
				break;
			
			case "view":
				os.writeBytes("Enter Message ID: ");
				int messageID = Integer.parseInt(trim(br.readLine()));
				os.writeBytes(model.getPost(userID, messageID));
				break;
				
			case "exit":
				model.updateLastActive(userID);
				break;
			}
		} while (!command.equalsIgnoreCase("exit"));
		
		System.out.println("Client disconnected: " + socket.getRemoteSocketAddress().toString());
		
		os.close();
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
