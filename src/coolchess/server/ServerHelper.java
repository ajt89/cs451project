package coolchess.server;

import java.net.*;
import java.util.HashMap;

import coolchess.client.Client;
import coolchess.game.Move;

import java.io.*;

public class ServerHelper {
	private int portNumber;
	//private static Move m;
	private static boolean whiteRecieved;
	private static boolean blackRecieved;
	private static HashMap<String, Socket> userSocket;
	private static String white;
	private static String black;
	
	public ServerHelper(int portNumber, HashMap<String, Socket> userSocket, String white, String black){
		this.portNumber = portNumber;
		whiteRecieved = false;
		blackRecieved = false;
		ServerHelper.userSocket = userSocket;
		ServerHelper.white = white;
		ServerHelper.black = black;
	}
	
	public void startGameServer() throws IOException{
		ServerSocket listener = new ServerSocket(portNumber, 2);
		try {
			Socket white = listener.accept();
			Socket black = listener.accept();
			ObjectInputStream inWhite = new ObjectInputStream(white.getInputStream());
			ObjectOutputStream outWhite = new ObjectOutputStream(white.getOutputStream());
			ObjectInputStream inBlack = new ObjectInputStream(black.getInputStream());
			ObjectOutputStream outBlack = new ObjectOutputStream(black.getOutputStream());
			while(true){
				Move m = (Move)inWhite.readObject();
				//System.out.println("Object recieved from black");
				outBlack.writeObject(m);
				//System.out.println("Object sent to black");
				m = (Move)inBlack.readObject();
				//System.out.println("Object recieved from black");
				outWhite.writeObject(m);
				//System.out.println("Object sent to white");
			}
		} catch (Exception e){
			System.out.println(e);
		}
		finally{
			listener.close();
		}
		
	}
}