import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class CoolChessServer implements Runnable {
	Socket csocket;
	static PrintWriter pw;
	static String username;
	String password;

	//Server constructor, passing in client socket and a bufferedwriter to the thread
	CoolChessServer(Socket csocket, PrintWriter pw, String username){
		this.csocket = csocket;
		this.username = username;
	}

	public static void main(String args[]) throws Exception{
	
		//set up socket to listen
		ServerSocket ssock = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("Listening on " + args[0]);
		System.out.println("Hostname: " + InetAddress.getLocalHost().getHostName());
		while (true){
			//accept connections and spawn a new thread for them
			Socket sock = ssock.accept();
			new Thread(new CoolChessServer(sock, pw, username)).start();
		}
	}
	
	//thread for a connection
	public void run(){
		try{
		//Setting up BufferedWriters, BufferedReaders, and PrintWriter
		//BufferedWriter bw = new BufferedWriter(log);
		PrintWriter out = new PrintWriter(csocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
        System.out.println("Connected to: " + csocket.getRemoteSocketAddress().toString());
		out.println("Welcome to Cool Chess Server");
		//boolean determines if connection should be open
		boolean connected = true;

		//states booleans
		boolean userNeed = true;
		

		//main loop
		while (connected == true){
			//reading one line at a time
			String input = in.readLine();
			System.out.println(input);
			out.println(input);
		}
		//close the connection
		out.close();
		in.close();
		csocket.close();
		System.out.println("Disconnected from: " + csocket.getRemoteSocketAddress().toString());

		//catch any exceptions and record them to the log
		} catch(IOException e){
			System.out.println(e);
		}
	}
}
