package coolchess.server;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import coolchess.game.Board;

public class Server {
	private static HashSet<String> usernames = new HashSet<String>();
	private static HashSet<PrintWriter> pw = new HashSet<PrintWriter>();
	private static HashMap<String, Socket> userSocket = new HashMap<String, Socket>();
	private static HashMap<String,String> opponents = new HashMap<String,String>();
	private static int port;
	private static int counter = 1;
	//private static Move m;
	//private static boolean whiteMove;
	//private static boolean blackMove;
	
	public static void main(String[] args) throws Exception{
		if (args.length != 1){
			System.out.println("Correct usage: java Server <port number>");
			System.exit(0);
		}
		port = Integer.parseInt(args[0]);
		counter = 1;
		System.out.println("Server running... ");
		System.out.println("Hostname: " + InetAddress.getLocalHost().getHostName());
		ServerSocket listener = new ServerSocket(port, 6);
		System.out.println("Listening on: " + port);
		
		try {
			while (true){
				new Handler(listener.accept()).start();
			}
		} finally{
			listener.close();
		}
	}
	
	private static class Handler extends Thread {
		private String username;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		//private String opponent;
		
		public Handler(Socket socket){
			this.socket = socket;
		}
		
		public void run(){
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				PrintWriter individual = new PrintWriter(socket.getOutputStream(), true);
				while(true){
					out.println("Enter username: ");
					username = in.readLine();
					if (username == null){
						return;
					}
					synchronized(usernames){
						if (!usernames.contains(username)){
							usernames.add(username);
							break;
						}
					}
				}
				userSocket.put(username,socket);
				out.println("Username accepted");
				
				System.out.println(username + " added");
				/*for (String s : usernames){
					out.println("PLAYERS: " + s);
					System.out.println("Players: " + s);
				}*/
				pw.add(out);
				
				boolean status = true;
				while(status){
					Thread t = null;
					String input = in.readLine();
					if (input == null){
						return;
					}
					else if(input.equals("PLAYERLIST")){
						int numberUsers = 0;
						for (String s : usernames){
							numberUsers = numberUsers + 1;
						}
						String numUsers = Integer.toString(numberUsers);
						individual.println("NUMBERPLAYERS: " + numUsers);
						System.out.println("NUMBERPLAYERS: " + numUsers);
						for (String s : usernames){
							individual.println("PLAYERS: " + s);
							System.out.println("Players: " + s);
						}
					}
					else if (input.equals("QUIT")){
						status = false;
					}
					else if(input.contains(username + " challenge")){
						t = new Thread(GameStart);
						for (PrintWriter writer : pw){
							writer.println("COUNTER: " + counter);
						}	
						String[] inputArray = input.split(" ");
						String user1 = inputArray[0];
						String user2 = inputArray[2];
						
						opponents.put(user1,user2);
						opponents.put(user2,user1);
						System.out.println(user1 + ": opponent = " + opponents.get(user1) + " ");
						System.out.println(user2 + ": opponent = " + opponents.get(user2) + " ");
						
						t.start();
						for (PrintWriter writer : pw){
							writer.println(input);
						}
					}
					else if (input.contains("accept") || (input.contains("denied"))){
						for (PrintWriter writer : pw){
							writer.println(username + " " + input);
						}
						if (input.contains("denied")){
							t.interrupt();
						}
					}
					else if (input.contains(username) && input.contains("SURRENDER")){
						t.interrupt();
						//individual.println("VICTORY");
						for (PrintWriter writer : pw){
							writer.println(opponents.get(username) + " VICTORY");
						}
						System.out.println("VICTORY sent to " + opponents.get(username));
					}
					else if (input.contains(username) && input.contains("WIN")){
						t.interrupt();
						for (PrintWriter writer : pw){
							writer.println(opponents.get(username) + " LOSS");
						}
						System.out.println("LOSS sent to " + opponents.get(username));
					}
					else if (input.contains(username) && input.contains("TIE")){
						t.interrupt();
						for (PrintWriter writer : pw){
							writer.println(opponents.get(username) + " TIE");
						}
						System.out.println("TIE sent to " + opponents.get(username));
					}
					else{
						for (PrintWriter writer : pw){
							writer.println(username + ": " + input);
						}	
					}
					System.out.println(username + ": " + input);
				}
			} catch(IOException e){
				System.out.println(e);
			} finally{
				if (username != null){
					usernames.remove(username);
					System.out.println(username + " removed");
				}
				try{
					socket.close();
				} catch(IOException e){
					System.out.println(e);
				}
			}
		}
	}
	
	static Runnable GameStart = new Runnable(){
		public void run() {
			System.out.println("Thread: " + counter + " running");
			try {
				ServerSocket listener = new ServerSocket(7878+counter, 4);
				Socket black = listener.accept();
				System.out.println(black);
				Socket white = listener.accept();
				counter++;
				System.out.println(white);
				listener.close();
				ObjectOutputStream outBlack = new ObjectOutputStream(black.getOutputStream());
				ObjectInputStream inBlack = new ObjectInputStream(black.getInputStream());
				ObjectOutputStream outWhite = new ObjectOutputStream(white.getOutputStream());
				ObjectInputStream inWhite = new ObjectInputStream(white.getInputStream());
				Board obj = null;
				while(true){
					obj = (Board)inWhite.readObject();
					outBlack.writeObject(obj);
					outBlack.flush();
					obj = (Board)inBlack.readObject();
					outWhite.writeObject(obj);
					outWhite.flush();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
