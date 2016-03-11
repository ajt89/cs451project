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
	private static int counter;
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
					else if(input.equals("PING")){
						//individual.println("PONG");
						//System.out.println("PING from: " + username);
					}
					else if (input.equals("QUIT")){
						status = false;
					}
					else if(input.contains(username + " challenge")){
						String[] inputArray = input.split(" ");
						String user1 = inputArray[0];
						String user2 = inputArray[2];
						
						opponents.put(user1,user2);
						opponents.put(user2,user1);
						System.out.println(user1 + ": opponent = " + opponents.get(user1) + " ");
						System.out.println(user2 + ": opponent = " + opponents.get(user2) + " ");
						
						Socket user1Socket = userSocket.get(user1);
						Socket user2Socket = userSocket.get(user2);
						
						new Thread(GameStart).start();
						for (PrintWriter writer : pw){
							writer.println(input);
						}
					}
					else if (input.contains("accept") || (input.contains("denied"))){
						for (PrintWriter writer : pw){
							writer.println(username + " " + input);
						}
					}
					/*else if (input.contains("GAME") && input.contains(username)){
						String[] inputArray = input.split(" ");
						String user1 = inputArray[1];
						String user2 = inputArray[2];
						
						opponents.put(user1,user2);
						opponents.put(user2,user1);
						System.out.println(user1 + ": opponent = " + opponents.get(user1) + " ");
						System.out.println(user2 + ": opponent = " + opponents.get(user2) + " ");
						
						Socket user1Socket = userSocket.get(user1);
						Socket user2Socket = userSocket.get(user2);
						
						//new ServerHelper(7070,userSocket,user1,user2);
						//new Thread(whiteListener).start();
						new Thread(GameStart).start();
						//whiteMove = true;
						//blackMove = false;
						System.out.println("launching server helper");
						sleep(10000);
						
						//counter++;
						for (PrintWriter writer : pw){
							writer.println("GAMESTART: " + user1 + " " + user2);
						}
					}*/
					else if (input.contains(username) && input.contains("SURRENDER")){
						//individual.println("VICTORY");
						for (PrintWriter writer : pw){
							writer.println(opponents.get(username) + " VICTORY");
						}
						System.out.println("VICTORY sent to " + opponents.get(username));
					}
					else{
						//System.out.println(username + ": " + input);
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
			System.out.println("Hello!");
			try {
				ServerSocket listener = new ServerSocket(7878, 4);
				Socket black = listener.accept();
				System.out.println(black);
				Socket white = listener.accept();
				System.out.println(white);
				listener.close();
				System.out.println("listener closed dawg");
				ObjectOutputStream outBlack = new ObjectOutputStream(black.getOutputStream());
				System.out.println("Stream 1");
				ObjectInputStream inBlack = new ObjectInputStream(black.getInputStream());
				System.out.println("Stream 2");
				ObjectOutputStream outWhite = new ObjectOutputStream(white.getOutputStream());
				System.out.println("Stream 3");
				ObjectInputStream inWhite = new ObjectInputStream(white.getInputStream());
				System.out.println("Stream 4");
				System.out.println("Sockets all setup");
				Board obj = null;
				while(true){
					obj = (Board)inWhite.readObject();
					System.out.println("White in");
					outBlack.writeObject(obj);
					outBlack.flush();
					System.out.println("Black out");
					obj = (Board)inBlack.readObject();
					System.out.println("Black in");
					outWhite.writeObject(obj);
					outWhite.flush();
					System.out.println("Whit out");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
