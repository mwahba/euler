/**
 * AU15 - CSE 5461 - Networking - Programming Assignment 1 - Web Server
 * @author Mark Wahba, built on skeleton provided by Dr. Giovani
 * @since September 29, 2015
 */
package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class WebServer {
	private static int PORT = 6789;
	
	public static void main(String argv[]) throws Exception {
		// Establish the listen socket
		final ServerSocket socket = new ServerSocket(PORT);
		
		// Close the server properly when server is shut down
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		// Process HTTP service requests in an infinite loop
		while (true) {
			// Listen for a TCP connection Request
			// blocks until finds an incoming connection to accept
			Socket clientSocket = socket.accept();
			
			// Construct an object to process the HTTP request message.
			HttpRequest request = new HttpRequest(clientSocket);
			
			// Create a new thread to process the request
			Thread thread = new Thread(request);
			
			// Start the thread
			thread.start();
		}
	}
}

final class HttpRequest implements Runnable {
	final static String CRLF = "\r\n", USERNAME = "user", PASSWORD = "password";
	Socket socket;
	
	//Constructor
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}
	
	/**
	 * Private method to return the file MIME type.
	 * @param fileName The file whose content type is in question.
	 * @return The file's MIME type to be returned.
	 */
	private String findContentType(String fileName) {
		String lcFileName = fileName.toLowerCase();
		
		if (lcFileName.endsWith(".htm") || lcFileName.endsWith(".html")) {
			return "text/html";
		} 
		
		if (lcFileName.endsWith(".gif")) {
			return "image/gif";
		}
		
		if (lcFileName.endsWith(".jpg") || lcFileName.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		
		if (lcFileName.endsWith(".txt")) {
			return "text/plain";
		}
		
		return "application/octet-stream";
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
	
	private void processRequest() throws Exception {
		// Get a reference to the socket's input and output streams
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		
		// Set up input stream filters.
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
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
		tokens.nextToken(); // skip over "GET"
		String fileName = "." + tokens.nextToken(); // file name with prepended "."
		if (fileName.equals("./")) {
			fileName = "./index.html";
		}
		
		// Construct the response message
		String statusLine = "", contentTypeLine = "", entityBody = "", fileType = findContentType(fileName);
		
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
		} else if (fileType.equals("text/plain")) { 
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