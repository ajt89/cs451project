package coolchess.server;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	private static HashSet<String> usernames = new HashSet<String>();
	private static HashSet<PrintWriter> pw = new HashSet<PrintWriter>();
	private static HashMap<String, Socket> userSocket = new HashMap<String, Socket>();
	private static HashMap<String,String> opponents = new HashMap<String,String>();
	
	public static void main(String[] args) throws Exception{
		if (args.length != 1){
			System.out.println("Correct usage: java Server <port number>");
			System.exit(0);
		}
		int port = Integer.parseInt(args[0]);
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
					else if (input.contains("GAME") && input.contains(username)){
						String[] inputArray = input.split(" ");
						String user1 = inputArray[1];
						String user2 = inputArray[2];
						
						opponents.put(user1,user2);
						opponents.put(user2,user1);
						System.out.println(user1 + ": opponent = " + opponents.get(user1));
						System.out.println(user2 + ": opponent = " + opponents.get(user2));
						
						Socket user1Socket = userSocket.get(user1);
						Socket user2Socket = userSocket.get(user2);
						System.out.println("launching server helper");
						new ServerHelper(user1Socket,user2Socket);
					}
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
}
