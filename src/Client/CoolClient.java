package Client;

import java.util.*;
import java.io.*;
import java.net.*;
import java.text.*;

public class CoolClient {
	public static void main(String[] args)throws Exception{
		Scanner in = new Scanner(System.in);
		
		//command arguments to variables
		System.out.println("Please enter server host:");
        String serverHost = in.nextLine();
        System.out.println("Please enter port number:");
		int portNumber = Integer.parseInt(in.nextLine());
		
		//get username and password
		System.out.println("Please enter username:");
		String username = in.nextLine();
		
		//Construct FTPClient with inputs from user
		CoolClientHelper cch = new CoolClientHelper(serverHost,portNumber,username);	
		//Connect with FTP server and setup Buffered readers and writers	
		cch.connect();
		
		//Setting up while loop, only exit upon sending QUIT to FTP server
		boolean exit = false;
		while(exit != true){
			String input;
			
			//Setting up switch statements, simple numbering system to choose FTP commands
			System.out.println("Enter 0 to send the QUIT command to server and exit program.");
			System.out.println("Enter 1 for raw input.");
			
			input = in.nextLine();

			switch (Integer.parseInt(input)){
			//exit and send QUIT
			case 0: cch.QUIT();
				exit = true;
				break;
				
			//send raw input
			case 1: System.out.println("Enter input: ");
				 input = in.nextLine();
				 cch.raw(input);
				 break;
				 
			//to get any unwanted answers
			default: System.out.println("Invalid input");
				break;
			}
		}
	}
}
