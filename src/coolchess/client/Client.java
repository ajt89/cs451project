package coolchess.client;

import java.util.*;

public class Client implements Runnable{
	ClientHelper ch;
	
	Client(ClientHelper ch){
		this.ch = ch;
	}
	
	public static void main(String[] args)throws Exception{
		Scanner in = new Scanner(System.in);
		
		System.out.println("Please enter server host:");
        String serverHost = in.nextLine();
        System.out.println("Please enter port number:");
		int portNumber = Integer.parseInt(in.nextLine());
		
		ClientHelper ch = new ClientHelper(serverHost,portNumber);	
		//Connect with server and setup Buffered readers and writers	
		ch.connect();
		new Thread(new Client(ch)).start();
		
		//Setting up while loop, only exit upon sending QUIT to FTP server
		boolean exit = false;
		while(exit != true){
			String input;
			
			//Setting up switch statements, simple numbering system to choose FTP commands
			System.out.println("Enter 0 to send the QUIT command to server and exit program.");
			System.out.println("Enter 1 to send a message.");
			System.out.println("Enter 2 to send username.");
			
			input = in.nextLine();

			switch (Integer.parseInt(input)){
			//exit and send QUIT
			case 0: ch.QUIT();
				exit = true;
				break;
				
			//send raw input
			case 1: System.out.println("Enter input: ");
				 input = in.nextLine();
				 ch.raw(input);
				 break;
			
			case 2: System.out.println("Enter username: ");
				input = in.nextLine();
				 ch.user(input);
				 break;
				 
			//to get any unwanted answers
			default: System.out.println("Invalid input");
				break;
			}
		}
		
		in.close();
	}

	@Override
	public void run() {
		try{
			boolean connected = true;
			while (connected == true){
				ch.setResponse();
				System.out.println("Message from the server: " + ch.getResponse());
			} 
		}catch(Exception e){
			System.out.println(e);
		}
		
	}
}
