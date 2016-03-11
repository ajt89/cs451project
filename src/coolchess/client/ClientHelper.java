package coolchess.client;
import java.io.*;
import java.net.*;
import java.util.*;

import coolchess.game.Move;

public class ClientHelper {
	private String serverHost;
	private int portNumber;
	private String response;
	private String message;
	private String username;
	private Socket socketString;
	private Socket socketObject;
	private BufferedWriter bw;
	private BufferedReader br;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private ArrayList<String> playerlist = new ArrayList<String>();;
	private Move obj;

	// Server Host and port number are passed in
	public ClientHelper(String _serverHost, int _portNumber){
		serverHost = _serverHost;
		portNumber = _portNumber;
	}
	
	//Getters and setters
	public String getServerHost(){
		return serverHost;
	}
	
	public void setServerHost(String serverHost){
		this.serverHost = serverHost;
	}
	
	public int getPortNumber(){
		return portNumber;
	}
	
	public void setPortNumber(int portNumber){
		this.portNumber = portNumber;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setMessage(String message){
		this.message = message;	
	}
	
	//get response from server
	public void setResponse() throws Exception{
		response = br.readLine();
    }
	
	public String getResponse(){
		return response;
	}
	
	public Socket getSocketString(){
		return socketString;
	}
	
	public Socket getSocketObject(){
		return socketObject;
	}
	
	//used for testing
	public void raw(String input) throws Exception{
		bw.write(input + "\n");
		bw.flush();

		/*setResponse();
		System.out.println(response);
		*/
	}	
	
	public void sendMove(Move m) throws Exception{
		objectOut.writeObject(m);
	}
	
	public Move getMove() throws Exception{
		Move obj = (Move)objectIn.readObject();
		return obj;
	}
	
	public String getUser(){
		return username;
	}
	
	public void clearPlayerlist (){
		playerlist = new ArrayList<String>();
	}
	
	public void setPlayerlist(String input){
		if (input.equals(getUser())){
			return;
		}
		else if(!playerlist.contains(input)){
			playerlist.add(input);
		}
	}
	
	public ArrayList<String> getPlayerlist(){
		return playerlist;
	}
	
	public void printPlayerlist(){
		for (int i = 0; i < playerlist.size(); i++){
			System.out.println(playerlist.get(i));
		}
	}
	
	public void user(String input) throws Exception{
		username = input;
		bw.write(input + "\n");
		bw.flush();
	}
	//connect to server via socket, sets up buffered writers and readers
	public void connect() throws Exception{
		socketString = new Socket(serverHost, portNumber);
		OutputStream os = socketString.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        bw = new BufferedWriter(osw);

        InputStream is = socketString.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        
        socketObject = new Socket(serverHost, portNumber);
        objectIn = new ObjectInputStream(socketObject.getInputStream());
		objectOut = new ObjectOutputStream(socketObject.getOutputStream());
	}
		
	//sends QUIT to server, closing the connection
	public void QUIT() throws Exception{
		bw.write("QUIT\n");
		bw.flush();
	}
	
	public void startGame(){
	//	new Thread(ChessGame).start();
	//	new Thread(SurrenderListen).start();
	}
}
