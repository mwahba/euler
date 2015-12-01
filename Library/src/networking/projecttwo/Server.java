package networking.projecttwo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * CSE 5461 - Mark Wahba (wahba.2@osu.edu) - Programming Assignment 2
 * Server - always listening for new connections coming in. Based on implementation from Programming Assignment 1.
 * Please read README for more information.
 * @author Mark Wahba (wahba.2@osu.edu)
 */
public class Server {
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
			Request request = new Request(clientSocket);
			//WebSocketRequest request = new WebSocketRequest(clientSocket);
			
			// Create a new thread to process the request
			Thread thread = new Thread(request);
			
			// Start the thread
			thread.start();
		}
	}
}