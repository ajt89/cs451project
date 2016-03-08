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
		new Thread(ChessGame).start();
	}
	Runnable ChessGame = new Runnable(){
		public void run(){
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
}
