import java.util.*;
import java.io.*;
import java.net.*;

public class CoolChessClientHelper {
	private String serverHost;
	private int portNumber;
	private String username;
	private String response;
	private String message;
	private Socket socket;
	private BufferedWriter bw;
	private BufferedReader br;

	//contructor Server Host, port number, username, and password are passed in
	public CoolChessClientHelper(String _serverHost, int _portNumber, String _username){
		serverHost = _serverHost;
		portNumber = _portNumber;
		username = _username;
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
	
	public String getUsername(){
                return username;
        }

        public void setUsername(String username){
                this.username = username;
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
	
	//used for testing, getting familiar with FTP
	public void raw(String input) throws Exception{
		bw.write(input + "\n");
		bw.flush();

		setResponse();
		System.out.println(response);
	}	
	//connect to FTP server via socket, sets up buffered writers and readers
	public void connect() throws Exception{
		socket = new Socket(serverHost, portNumber);
		OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                bw = new BufferedWriter(osw);

                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                br = new BufferedReader(isr);	
	
		setResponse();
		System.out.println(response);		

	}
	//sends QUIT to FTP server, closing the connection
	public void QUIT() throws Exception{
		bw.write("QUIT\n");
		bw.flush();
		setMessage("QUIT\n");
		setResponse();
                System.out.println(response);
	}
	//sends USER <username> to FTP server, completing first half of login
	public void USER() throws Exception{
		bw.write("USER " + username + "\n");
		bw.flush();
		setMessage("USER " + username + "\n");
		setResponse();                
		System.out.println(response);
	}
}
