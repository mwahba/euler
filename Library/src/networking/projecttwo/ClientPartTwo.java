package networking.projecttwo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class ClientPartTwo {
	static String host = "localhost";
	static int port = 6789;
	static String errorResponse = "There was an issue with your request, please try again or see help for further assistance.";

	private static Socket socket;
	private static PrintWriter request;
	private static BufferedReader response;
	private static BufferedReader sysIn;
	
	private static void outputHelpContent() {
		try {
			File file = new File("help-partone");
	        BufferedReader buffer = new BufferedReader(new FileReader(file));
	        
	        while (buffer.ready()) {
	        	System.out.println(buffer.readLine());
	        }
	        
	        buffer.close();
		} catch (IOException ioe) {
			System.out.println("Error processing help file, verify file is readable.");
			ioe.printStackTrace();
		}
	}
	
	private static void validateResponse(String serverResponse, String expected, String value) {
		if (serverResponse.equals(expected)) {
			request.println(value);
		} else {
			System.out.println(errorResponse + "\r\nExpected: " + expected + ", received: " + serverResponse);
		}
	}
	
	private static void print(String toPrint) {
		System.out.println(toPrint.replace("|", "\r\n"));
	}
	
	private static String readFromServer() throws IOException {
		boolean wait = true;
		String serverResponse = "";
		while (wait) {
			if ((serverResponse = response.readLine()).length() > 0) {
				wait = false;
			}
		}
		
		return serverResponse;
	}
	
	private static String getGroupID(String groupName) throws IOException {
		int groupID = 0;
		boolean processGroupName = true;
		while (processGroupName) {
			request.println("groupID");
			validateResponse(readFromServer(), "groupName", groupName);
			String serverResponse = readFromServer().replace("|", "");
			if (serverResponse.length() > 0) {
				groupID = Integer.parseInt(serverResponse);
				processGroupName = false;
			} else {
				System.out.print("Invalid Group ID or Name entered, please input group ID or name: ");
				groupName = sysIn.readLine();
			}
		}
		
		return groupID + "";
	}
	
	public static void main(String[] args) {
		String userInput;
		sysIn = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print("Command ('connect host port', 'help', or 'exit'): ");
			userInput = sysIn.readLine();
			
			while (!userInput.startsWith("connect") || userInput.startsWith("help")) {
				if (userInput.equalsIgnoreCase("help")) {
					outputHelpContent();
				}
				
				if (userInput.equalsIgnoreCase("exit")) {
					sysIn.close();
					System.out.println("Goodbye");
					return;
				}
				
				if (!userInput.equalsIgnoreCase("help")) {
					System.out.print("You have not entered a proper command. ");
				}
				
				System.out.print("Please enter 'connect' command, 'help', or 'exit': ");
				userInput = sysIn.readLine();
			}
			
			StringTokenizer tokens = new StringTokenizer(userInput);
			tokens.nextToken(); // skipping over connect
			if (tokens.hasMoreTokens()) {
				host = tokens.nextToken();
				port = Integer.parseInt(tokens.nextToken());
			} else {
				System.out.println("Will connect using the default host: " + host + " and port: " + port);
			}
			
			try {
				socket = new Socket(host, port);
				request = new PrintWriter(socket.getOutputStream(), true);
				response = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				// login
				if ((readFromServer()).equals("username")) {
					System.out.print("\r\nEnter username: ");
					while((userInput = sysIn.readLine()) == null || userInput.length() < 1) {
						System.out.print("Username was empty, please enter a valid username.");
					}
					// send username to server
					request.println(userInput);
					
					// get login success message
					print(readFromServer());
				} else {
					System.out.println("There was an error with the server response. Please contact the administrator.");
					return;
				}
				
				request.println("groups");
				print("Some groups on this server:\r\n" + readFromServer());
				
				String[] userInputArray;
				
				do {
					System.out.print("Enter command or help: ");
					userInput = sysIn.readLine();
					userInputArray = userInput.split(" ", 3);
					
					switch(userInputArray[0].toLowerCase()) {
					case "groupjoin":
					case "join":
						userInputArray[1] = getGroupID(userInputArray[1]);
						request.println("join");
						validateResponse(readFromServer(), "groupID", userInputArray[1]);
						break;
						
					case "grouppost":
					case "post":
						userInputArray[1] = getGroupID(userInputArray[1]);
						request.println("post");
						validateResponse(readFromServer(), "groupID", userInputArray[1]);
						validateResponse(readFromServer(), "subject", userInputArray[2].split("\\|")[0]);
						validateResponse(readFromServer(), "content", userInputArray[2].split("\\|", 2)[1]);
						break;
						
					case "groupusers":
					case "users":
						userInputArray[1] = getGroupID(userInputArray[1]);
						request.println("users");
						validateResponse(readFromServer(), "groupID", userInputArray[1]);
						break;
						
					case "groupleave":
					case "leave":
						userInputArray[1] = getGroupID(userInputArray[1]);
						request.println("leave");
						validateResponse(readFromServer(), "groupID", userInputArray[1]);
						break;
						
					case "groupmessage":
					case "message":
						userInputArray[1] = getGroupID(userInputArray[1]);
						request.println("message");
						validateResponse(readFromServer(), "messageID", userInputArray[1]);
						break;
						
					case "grouplistmessages":
					case "listmessages":
						userInputArray[1] = getGroupID(userInputArray[1]);
						request.println("listmessages");
						validateResponse(readFromServer(), "groupID", userInputArray[1]);
						break;
						
					case "exit":
						request.println("exit");
						break;
						
					case "help":
						outputHelpContent();
						break;
					
					default:
						System.out.println("Unknown command was entered. Please use 'help' for further assistance.\r\n");
						userInputArray[0] = "unknown";
						break;
					}
					
					if (!userInputArray[0].toLowerCase().startsWith("help") && !userInputArray[0].equals("unknown")) {
						print(readFromServer());
					}
				} while (!userInput.startsWith("exit"));/**/
				
				request.println("exit");
				
				System.out.println("Goodbye.");
				
				sysIn.close();
				request.close();
				response.close();
				socket.close();
				
			} catch (UnknownHostException e) {
				System.err.println("Unable to find host " + host + ":" + port);
				e.printStackTrace();
			}
		} catch (IOException ioe) {
			System.err.println("Unable to obtain an I/O connection to host " + host);
			ioe.printStackTrace();
		}
	}
}
