package coolchess.client;
import java.util.*;

public class Client implements Runnable{
	ClientHelper ch;
	
	Client(ClientHelper ch){
		this.ch = ch;
	}
	
	public static void main(String[] args)throws Exception{
		Scanner in = new Scanner(System.in);
		String input;
		
		System.out.println("Please enter server host:");
        String serverHost = in.nextLine();
        System.out.println("Please enter port number:");
		int portNumber = Integer.parseInt(in.nextLine());
		
		ClientHelper ch = new ClientHelper(serverHost,portNumber);	
		//Connect with server and setup Buffered readers and writers	
		ch.connect();
		
		boolean userEntry = true;
		while(userEntry){
			ch.setResponse();
			System.out.println("Server: " + ch.getResponse());
			input = in.nextLine();
			
			ch.user(input);
			ch.setResponse();
			
			if (ch.getResponse().equals("Username accepted")){
				System.out.println("Server: " + ch.getResponse());
				userEntry = false;
				break;
			}
		}
		Thread t = new Thread(new Client(ch)); 
		t.start();
		
		//Setting up while loop, only exit upon sending QUIT to FTP server
		boolean exit = false;
		while(exit != true){
			//Setting up switch statements, simple numbering system to choose FTP commands
			System.out.println("Enter 0 to send the QUIT command to server and exit program.");
			System.out.println("Enter 1 to send a message.");
			System.out.println("Enter 2 to send a challenge.");
			System.out.println("Enter 3 to accept a challenge.");
			System.out.println("Enter 4 to deny a challenge.");
			
			input = in.nextLine();
			int choice = -1;
			try{
				choice = Integer.parseInt(input);
			}catch(NumberFormatException e){
				System.out.println("Please enter an integer");
			}
			switch (choice){
			//exit and send QUIT
			case 0: ch.QUIT();
				exit = true;
				break;
				
			//send raw input
			case 1: System.out.println("Enter input: ");
				input = in.nextLine();
				ch.raw(input);
				break;
			
			case 2: System.out.println("Enter user to send a challenge to: ");
				input = in.nextLine();
				ch.raw("challenge " + input);
				break;
				
			case 3: System.out.println("Enter user to accept: ");
				input = in.nextLine();
				ch.raw(input + " accept");
				break;
				
			case 4: System.out.println("Enter user to deny: ");
				input = in.nextLine();
				ch.raw(input + " denied");
				break;
				 
			//to get any unwanted answers
			default: System.out.println("Invalid input");
				break;
			}
		}
		in.close();
		t.interrupt();
		System.exit(0);
	}

	@Override
	public void run() {
		try{
			while (ch.getSocket().isConnected()){
				ch.setResponse();
				String response = ch.getResponse();
				if (response != null){
					String[] responseSplit = response.split(" ");
					/*for (int i = 0; i < responseSplit.length; i++){
						System.out.println(responseSplit[i]);
					}*/
					if (response.contains(ch.getUser())){
						String userChallenge = responseSplit[2].substring(0,responseSplit[2].length()-1);
						if (responseSplit[2].contains(ch.getUser())){
							return;
						}
						else if (responseSplit[3].equals("challenge") && responseSplit[4].equals(ch.getUser())){
							System.out.println("Challenge sent from " + userChallenge);
							System.out.println("Do you accept?(y/n):");
						}
						else if (responseSplit[3].equals(ch.getUser()) && responseSplit[4].equals("accept")){
							System.out.println("Challenge accepted by " + userChallenge);
						}
						else if (responseSplit[3].equals(ch.getUser()) && responseSplit[4].equals("denied")){
							System.out.println("Challenge denied by " + userChallenge);
						}
					}
					else if(response.equals("PONG")){
						return;
					}
					else{
						System.out.println("Server: " + response);
					}
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
	}
}
