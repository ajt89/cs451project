package coolchess.server;

import java.io.*;
import java.net.*;

public class Server implements Runnable {
	private ServerThread clients[] = new ServerThread[6];
	private ServerSocket server = null;
	private Thread thread = null;
	private int clientCount = 0;

	//Server constructor, passing in client socket and a bufferedwriter to the thread
	Server(int port){
		try{
			System.out.println("Binding to port " + port);
			server = new ServerSocket(port);
			System.out.println("Server started: " + server);
			start();
		}
		catch (IOException ioe){
			System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
		}
	}

	public static void main(String args[]){
		Server server = null;
		if(args.length != 1)
			System.out.println("usage: java Server <port>");
		else{
			server = new Server(Integer.parseInt(args[0]));
		}
	}
	
	//thread for a connection
	public void run(){
		while (thread != null){
			try{
				System.out.println("Waiting for client... Standby.");
				addThread(server.accept());
			}
			catch(IOException ioe){
				System.out.println("Server accept error: " + ioe);
			}
		}
	}
	
	public void start(){
		if (thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stop(){
		if (thread != null){
			thread = null;
		}
	}
	
	private int findClient(int ID){
		for(int i = 0; i < clientCount; i++){
			if (clients[i].getID() == ID)
				return i;
		}
		return -1;
	}
	
	public synchronized void handle(int ID, String input){
		if (input.equals("QUIT")){
			clients[findClient(ID)].send("QUIT");
			remove(ID);
		}
		else{
			for (int i = 0; i < clientCount; i++){
				clients[i].send(ID + ": " + input);
			}
		}
	}
	
	public synchronized void remove(int ID){
		int pos = findClient(ID);
		if (pos >= 0){
			ServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);
			if (pos < clientCount-1)
				for (int i = pos+1; i < clientCount; i++){
					clients[i-1] = clients[i];
				}
			clientCount--;
			try{
				toTerminate.close();
			}
			catch (IOException ioe){
				System.out.println("Error closing thread: " + ioe);
			}
		}
	}
	
	private void addThread(Socket socket){
		if (clientCount < clients.length){
			System.out.println("Client accepted: " + socket);
			clients[clientCount] = new ServerThread(this,socket);
			try{
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			}
			catch(IOException ioe){
				System.out.println("Error opening thread: " + ioe);
			}
		}
		else{
			System.out.println("Clieng refused: maximum " + clients.length + " reached.");
		}
	}
}
