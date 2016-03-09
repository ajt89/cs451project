package coolchess.server;

import java.net.*;

import coolchess.game.Move;

import java.io.*;

public class ServerHelper {
	Socket user1Socket;
	Socket user2Socket;
	
	public ServerHelper(Socket user1Socket, Socket user2Socket){
		this.user1Socket = user1Socket;
		this.user2Socket = user2Socket;
		//new Thread(SurrenderListen1).start();
		//new Thread(SurrenderListen2).start();
		//new Thread(ChessGame).start();
	}
	Runnable ChessGame = new Runnable(){
		public void run(){
			System.out.println("Server ChessGame running");
			try {
				ObjectInputStream user1In = new ObjectInputStream(user1Socket.getInputStream());
				ObjectOutputStream user1Out = new ObjectOutputStream(user1Socket.getOutputStream());
				ObjectInputStream user2In = new ObjectInputStream(user2Socket.getInputStream());
				ObjectOutputStream user2Out = new ObjectOutputStream(user1Socket.getOutputStream());
				while(true){
					Move obj = (Move)user1In.readObject();
					user2Out.writeObject(obj);
					obj = (Move)user2In.readObject();
					user1Out.writeObject(obj);
				}	
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	};
	
	Runnable SurrenderListen1 = new Runnable(){
		public void run(){
			boolean inProgress = true;
			System.out.println("SurrenderListen1 running");
			try{
				BufferedReader in1 = new BufferedReader(new InputStreamReader(user1Socket.getInputStream()));
				//PrintWriter out1 = new PrintWriter(user1Socket.getOutputStream(), true);
				//BufferedReader in2 = new BufferedReader(new InputStreamReader(user2Socket.getInputStream()));
				PrintWriter out2 = new PrintWriter(user2Socket.getOutputStream(), true);
				while(inProgress){
					String input = in1.readLine();
					System.out.println(input + "ONE");
					if (input.contains("SURRENDER")){
						out2.println("VICTORY");
						System.out.println("Print vicotry? ONE");
						inProgress = false;
						break;
					}
				}
			} catch (Exception e){
				System.out.println(e);
			}
		}
	};
	
	Runnable SurrenderListen2 = new Runnable(){
		public void run(){
			System.out.println("SurrenderListen2 running");
			boolean inProgress = true;
			try{
				//BufferedReader in1 = new BufferedReader(new InputStreamReader(user1Socket.getInputStream()));
				PrintWriter out1 = new PrintWriter(user1Socket.getOutputStream(), true);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(user2Socket.getInputStream()));
				//PrintWriter out2 = new PrintWriter(user2Socket.getOutputStream(), true);
				while(inProgress){
					String input = in2.readLine();
					System.out.println(input + "TWO");
					if (input.contains("SURRENDER")){
						out1.println("VICTORY");
						System.out.println("Print vicotry? TWO");
						inProgress = false;
						break;
					}
				}
			} catch (Exception e){
				System.out.println(e);
			}
		}
	};
}
