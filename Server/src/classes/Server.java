package classes;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 *
 * The Server class represents the daemon for the server side.
 * It keeps waiting for new connections to happen.
 */
public class Server {
	
	/**
	 * Launcher method of the server
	 * @param args unused parameter
	 * @throws IOException exception
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		//creazione ServerSocket
		ServerSocket serverSocket = new ServerSocket(ApplicationParameters.getPort());
		System.out.println("Started: "+serverSocket);
		
		while(true){
			System.out.println("Waiting a connection...");
			Socket socket = serverSocket.accept();
			
			new Thread(new ServerSkeleton(socket)).start();
		}
	}

}