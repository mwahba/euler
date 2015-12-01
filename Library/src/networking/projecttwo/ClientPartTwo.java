package networking.projecttwo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

/**
 * CSE 5461 - Mark Wahba (wahba.2@osu.edu) - Programming Assignment 2
 * ClientPartTwo - client implementation.
 * Please read README for more information.
 * @author Mark Wahba (wahba.2@osu.edu)
 */
public class ClientPartTwo {
	static String host = "172.31.15.162";
	static int port = 6789;
	static String errorResponse = "There was an issue with your request, please try again or see help for further assistance.";
	
	static BufferedReader response;
	
	private static void outputHelpContent() {
		try {
			File file = new File("help-parttwo");
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
	
	private static void validateResponse(BufferedOutputStream request, String serverResponse, String expected, String value) throws IOException {
		if (serverResponse.equals(expected)) {
			writeToServer(request, value);
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
	
	private static void writeToServer(BufferedOutputStream request, String toWrite) throws IOException {		
		request.write((toWrite + "\r\n").getBytes());
		request.flush();
	}
	
	private static String prep(String toSend) {
		return toSend.replace("'", "\'");
	}
	
	public static void main(String[] args) {
		String userInput;
		BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
		
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
				if (tokens.hasMoreTokens()) {
					port = Integer.parseInt(tokens.nextToken());
				} else {
					System.out.println("You did not specify a port, please enter a port number: ");
					userInput = sysIn.readLine();
					port = Integer.parseInt(userInput);
				}
			} else {
				System.out.println("Will connect using the default host: " + host + " and port: " + port);
			}
			
			try {
				Socket socket = new Socket(host, port);
				BufferedOutputStream request = new BufferedOutputStream(socket.getOutputStream());
				response = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				// login
				if ((readFromServer()).startsWith("username")) {
					System.out.print("\r\nEnter username: ");
					while((userInput = sysIn.readLine()) == null || userInput.length() < 1) {
						System.out.print("Username was empty, please enter a valid username: ");
					}
					// send username to server
					writeToServer(request, userInput);
					
					// get login success message
					print(readFromServer());
				} else {
					System.out.println("There was an error with the server response. Please contact the administrator.");
					request.close();
					response.close();
					socket.close();
					return;
				}
				
				writeToServer(request, "groups");
				print("Some groups on this server:\r\n" + readFromServer());
				
				String[] userInputArray;
				
				do {
					System.out.print("Enter command or help: ");
					userInput = sysIn.readLine();
					userInputArray = userInput.split(" ", 3);
					
					switch(userInputArray[0].toLowerCase()) {
					case "groupjoin":
					case "join":
						writeToServer(request, "join");
						validateResponse(request, readFromServer(), "groupID", userInputArray[1]);
						break;
						
					case "groups":
					case "listgroups":
						writeToServer(request, "groups");
						break;
						
					case "grouppost":
					case "post":
						writeToServer(request, "post");
						validateResponse(request, readFromServer(), "groupID", userInputArray[1]);
						validateResponse(request, readFromServer(), "subject", prep(userInputArray[2].split("\\|")[0]));
						validateResponse(request, readFromServer(), "content", prep(userInputArray[2].split("\\|", 2)[1]));
						break;
						
					case "groupusers":
					case "users":
						writeToServer(request, "users");
						validateResponse(request, readFromServer(), "groupID", userInputArray[1]);
						break;
						
					case "groupleave":
					case "leave":
						writeToServer(request, "leave");
						validateResponse(request, readFromServer(), "groupID", userInputArray[1]);
						break;
						
					case "groupmessage":
					case "message":
						writeToServer(request, "message");
						validateResponse(request, readFromServer(), "messageID", userInputArray[1]);
						break;
						
					case "grouplistmessages":
					case "listmessages":
						writeToServer(request, "listmessages");
						validateResponse(request, readFromServer(), "groupID", userInputArray[1]);
						break;
						
					case "exit":
						writeToServer(request, "exit");
						print(readFromServer());
						break;
						
					case "help":
						outputHelpContent();
						break;
					
					default:
						System.out.println("Unknown command was entered. Please use 'help' for further assistance.\r\n");
						userInputArray[0] = "unknown";
						break;
					}
					
					// if the user's command input was not help or something unknown
					if (!userInputArray[0].toLowerCase().startsWith("help") && !userInputArray[0].equals("unknown") 
							&& !userInputArray[0].toLowerCase().startsWith("exit")) {
						// print out the server response
						print(readFromServer());
						
						// print out updates and update last active date
						writeToServer(request, "updates");
						print(readFromServer());
					}
				} while (!userInput.startsWith("exit"));/**/
				
				writeToServer(request, "exit");
				
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
