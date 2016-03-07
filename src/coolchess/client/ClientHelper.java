package coolchess.client;
import java.io.*;
import java.net.*;

public class ClientHelper {
	private String serverHost;
	private int portNumber;
	private String response;
	private String message;
	private String username;
	private Socket socket;
	private BufferedWriter bw;
	private BufferedReader br;

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
	
	public Socket getSocket(){
		return socket;
	}
	
	//used for testing
	public void raw(String input) throws Exception{
		bw.write(input + "\n");
		bw.flush();

		/*setResponse();
		System.out.println(response);
		*/
	}	
	
	public String getUser(){
		return username;
	}
	
	public void user(String input) throws Exception{
		username = input;
		bw.write(input + "\n");
		bw.flush();
		new Thread(connectionCheck).start();
	}
	//connect to server via socket, sets up buffered writers and readers
	public void connect() throws Exception{
		socket = new Socket(serverHost, portNumber);
		OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        bw = new BufferedWriter(osw);

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
	}
	
	//sends QUIT to server, closing the connection
	public void QUIT() throws Exception{
		bw.write("QUIT\n");
		bw.flush();
	}
	
	Runnable connectionCheck = new Runnable(){
		public void run(){
			try {
				while(true){
					raw("PING");
					setResponse();
					Thread.sleep(15000);
				}	
			} catch (Exception e) {
				System.out.println(e);
				try {
					System.out.println("Attempt reconnect in 30 seconds");
					Thread.sleep(30000);
					connect();
				} catch (Exception e1) {
					System.out.println(e);
					System.out.println("Reconnect fail");
					System.exit(0);
				}
			}
		}
	};
}
