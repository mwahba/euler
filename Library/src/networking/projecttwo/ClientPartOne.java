package networking.projecttwo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class ClientPartOne {
	static String host = "Mark-PC";
	static int port = 6789;
	static Socket socket;
	static DataOutputStream os;
	static BufferedReader in;
	static int groupID = 1;

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
	
	public static void main(String[] args) {
		String userInput, serverResponse;
		BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.println("Command: ");
			userInput = sysIn.readLine();
			
			while (!userInput.startsWith("connect") || userInput.startsWith("help")) {
				if (userInput.equalsIgnoreCase("help")) {
					outputHelpContent();
				}
				
				if (userInput.equalsIgnoreCase("exit")) {
					sysIn.close();
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
			host = tokens.nextToken();
			port = Integer.parseInt(tokens.nextToken());
			
			try {
				socket = new Socket(host, port);
				os = new DataOutputStream(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				do {
					System.out.print("Enter command or help: ");
					userInput = sysIn.readLine();
					
					tokens = new StringTokenizer(userInput);
					
					switch(tokens.nextToken().toLowerCase()) {
					case "":
						
					case "exit":
						break;
					}
					// serverResponse = os.readLine();
				} while (userInput.equalsIgnoreCase("exit"));
				
				sysIn.close();
				os.close();
				in.close();
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
