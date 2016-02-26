package coolchess.client;

import java.util.*;

public class Client implements Runnable{
	public static void main(String[] args)throws Exception{
		Scanner in = new Scanner(System.in);
		
		//command arguments to variables
		System.out.println("Please enter server host:");
        String serverHost = in.nextLine();
        System.out.println("Please enter port number:");
		int portNumber = Integer.parseInt(in.nextLine());

		//Construct FTPClient with inputs from user
		ClientHelper ch = new ClientHelper(serverHost,portNumber);	
		//Connect with server and setup Buffered readers and writers	
		ch.connect();
		
		//Setting up while loop, only exit upon sending QUIT to server
		boolean exit = false;
		while(exit != true){
			String input;
			
			//Setting up switch statements, simple numbering system to choose commands
			System.out.println("Enter 0 to send the QUIT command to server and exit program.");
			System.out.println("Enter 1 for raw input.");
			
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
				 
			//to get any unwanted answers
			default: System.out.println("Invalid input");
				break;
			}
		}
		
		in.close();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
