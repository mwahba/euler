package networking.projecttwo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import webserver.FtpClient;

final class Request implements Runnable {
	final static String CRLF = "\r\n", USERNAME = "user", PASSWORD = "password",
			LOGIN = "login";
	
	Socket socket;
	
	Model model;
	
	//Constructor
	public Request(Socket socket) throws Exception {
		this.socket = socket;
		this.model = new Model();
	}
	
	private static void sendBytes(FileInputStream fis, DataOutputStream os) throws Exception {
		// Construct a 1K buffer to hold bytes on their way to the socket.
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		// Copy requested file into the socket's output stream.
		while ((bytes = fis.read(buffer)) != -1) {
			os.write(buffer, 0, bytes);
		}
	}
	
	private String trim(String string) {
		return string.replaceAll("[^A-Za-z0-9]", "");
	}
	
	private void processRequest() throws Exception {
		// Get a reference to the socket's input and output streams
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		os.writeBytes("Enter a username: ");
		
		String username = trim(br.readLine());
		
		int id = model.login(username);
		
		os.writeBytes(model.checkUpdates(id));
		
		String command;
		
		do {
			os.writeBytes("Enter command: ");
			command = br.readLine();
			
			switch (command) {
			case "list":
				model.listGroups(id);
				break;
			case "join":
				os.writeBytes("\tGroup ID to join: ");
				model.joinGroup(id, Integer.parseInt(trim(br.readLine())));
				break;
			case "leave":
				os.writeBytes("\tGroup ID to leave: ");
				model.leaveGroup(id, Integer.parseInt(trim(br.readLine())));
			case "exit":
				model.updateLastActive(id);
				break;
			}
		} while (!command.equalsIgnoreCase("exit"));
		// Get the request line of the HTTP request message
		String requestLine = br.readLine();
		
		// Display the request line.
		System.out.println("\nRequest:\n" + requestLine);
		
		// Get and display the header lines.
		String headerLine = null;
		while ((headerLine = br.readLine()).length() > 0) {
			System.out.println(headerLine);
		}
		
		// Extract the filename from the request line.
		StringTokenizer tokens = new StringTokenizer(requestLine);
		String currentCommand = tokens.nextToken();
		if (currentCommand.equals(LOGIN)) {
			
		}
		String fileName = "." + tokens.nextToken(); // file name with prepended "."
		if (fileName.equals("./")) {
			fileName = "./index.html";
		}
		
		// Construct the response message
		String statusLine = "", contentTypeLine = "", entityBody = "", fileType = "";
		
		// Open the requested file
		FileInputStream fis = null;
		boolean fileExists = true;
		
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			fileExists = false;
		}
		
		if (fileExists) {
			statusLine = "HTTP/1.1 200 OK" + CRLF;
			contentTypeLine = "Content-Type: " + fileType + CRLF;
		} else {
			if (fileType.equals("text/plain")) {
				// if the file requested is any type other than a text (.txt) file, report error to the web client
				// retrieve the text (.txt) file from the local FTP server
				FtpClient ftp = new FtpClient();
				
				ftp.connect(USERNAME, PASSWORD);
				
				// Retrieve the file from the FTP server
				try {
					ftp.getFile(fileName);
					fileExists = true;
				} catch (IOException e) {
					fileExists = false;
				}
				
				// disconnect from the FTP server
				ftp.disconnect();
				
				if (fileExists) {
					statusLine = "HTTP/1.1 200 OK" + CRLF;
					contentTypeLine = "Content-Type: text/plain" + CRLF;
					
					// assign input stream to read the recently ftp-downloaded file
					try {
						fis = new FileInputStream(fileName);
					} catch (FileNotFoundException e) {
						fileExists = false;
					}
				}
			}
			
			if (!fileExists) {
				statusLine = "HTTP/1.1 404 Not Found" + CRLF;
				contentTypeLine = "Content-Type: text/html" + CRLF;
				entityBody = "<HTML><HEAD><TITLE>Not Found</TITLE></HEAD><BODY>Not Found</BODY></HTML>";
			}
			
		}
		
		// Set the statusLine, the contentTypeLine, and the empty line to the output stream
		os.writeBytes(statusLine);
		os.writeBytes(contentTypeLine);
		os.writeBytes(CRLF);
		
		// Send the entity Body
		if (fileExists) {
			sendBytes(fis, os);
			fis.close();
		} else {
			os.writeBytes(entityBody);
		}
		
		System.out.println("\nResponse:\n" + statusLine + contentTypeLine + entityBody);
		
		//fis.close();
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
