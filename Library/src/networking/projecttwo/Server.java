package networking.projecttwo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
			
			// Create a new thread to process the request
			Thread thread = new Thread(request);
			
			// Start the thread
			thread.start();
		}
	}
}