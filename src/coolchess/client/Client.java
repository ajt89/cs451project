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
			System.out.println("Enter 2 to request playerlist.");
			System.out.println("Enter 3 to send a challenge.");
			System.out.println("Enter 4 to accept a challenge.");
			System.out.println("Enter 5 to deny a challenge.");
			
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
				
			case 2: ch.raw("PLAYERLIST");
				break;
				
			case 3: System.out.println("Enter user to send a challenge to: ");
				input = in.nextLine();
				ch.raw("challenge " + input);
				break;
				
			case 4: System.out.println("Enter user to accept: ");
				input = in.nextLine();
				ch.raw(input + " accept");
				break;
				
			case 5: System.out.println("Enter user to deny: ");
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
		String me = ch.getUser();
		try{
			while (ch.getSocket().isConnected()){
				ch.setResponse();
				String response = ch.getResponse();
				if (response != null){
					String[] responseSplit = response.split(" ");
					if (response.length() == 3 && response.contains(me)){
						String userMessage = responseSplit[0].substring(0,responseSplit[0].length()-1);
						if(responseSplit[1].equals("challenge") && responseSplit[2].equals(me)){
							System.out.println("Challenge sent from " + userMessage);
							System.out.println("Do you accept?");
						}
						else if(responseSplit[1].equals(me) && responseSplit[2].equals("accept")){
							System.out.println("Challenge accepted by " + userMessage);
						}
						else if(responseSplit[1].equals(me) && responseSplit[2].equals("denied")){
							System.out.println("Challenge denied by " + userMessage);
						}
					}
					else{
						System.out.println("Server: " + response);
					}
				}
			}
		}catch(Exception e){
			System.out.println(e);
			try{
				System.out.println("Attempt reconnect in 30 seconds");
				Thread.sleep(30000);
				ch.connect();
			} catch (Exception e1) {
				System.out.println(e);
				System.out.println("Reconnect fail");
				System.exit(0);
			}
		}
		
	}
}
