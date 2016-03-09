package coolchess.client;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import coolchess.gui.Chessboard;

public class Client implements Runnable{
	ClientHelper ch;
	static boolean gameChallenge = false;
	static boolean challengeAccept = false;
	static boolean challengeDenied = false;
	static String challengeUser;
	static CardLayout cardLayout = new CardLayout();
	static Container contentPane;
	
	Client(ClientHelper ch){
		this.ch = ch;
	}
	
	public static void main(String[] args)throws Exception{
		//Scanner in = new Scanner(System.in);
		//String input;
		
		if (args.length != 2){
			System.out.println("Correct usage: java Client <hostname> <server>");
			System.exit(0);
		}
		String serverHost = args[0];
		int portNumber = Integer.parseInt(args[1]);
		ClientHelper ch = new ClientHelper(serverHost,portNumber);	
		
		Runnable r = new Runnable() {
			public void run() {
				Chessboard cb = new Chessboard();
				JFrame frame = new JFrame();
				
				contentPane = frame.getContentPane();
				cardLayout = new CardLayout();
				
				contentPane.setLayout(cardLayout);
				JPanel menu = new JPanel();
				JButton play = new JButton("Play");
				DefaultListModel list = new DefaultListModel();
				play.addActionListener(new ActionListener()
			    {
				      public void actionPerformed(ActionEvent e)
				      {
				    	  String alias = (String)JOptionPane.showInputDialog(frame,"Please enter an alias.");
				    	  try {
							ch.user(alias);
						} catch (Exception e1) {
							System.out.println(e1);
						}
				    	  cardLayout.next(contentPane);
				    	  try {
								ch.raw("PLAYERLIST");
								ch.setResponse();
								//System.out.println(ch.getResponse());
								String[] responseIn = ch.getResponse().split(" ");
								int listSize = Integer.parseInt(responseIn[1]);
								for (int i = 0; i < listSize; i++){
									ch.setResponse();
									String response = ch.getResponse();
									if (response.contains("PLAYERS")){
										String[] responseSplit = response.split(" ");
										ch.setPlayerlist(responseSplit[1]);
										//list.addElement(responseSplit[1]);
										//System.out.println(responseSplit[1] + " added");
									}
								}
				    		} catch (Exception e1) {
								System.out.println(e1);
							}
				    		//people = new JList(list)
				    		list.clear();
				    		ArrayList<String> listPlayer = new ArrayList<String>();
				    		listPlayer = ch.getPlayerlist();
				    		for(int i = 0; i < listPlayer.size(); i++) {
				    			//System.out.println(listPlayer.get(i));
				    			list.addElement(listPlayer.get(i));
				    		}
				      }
			    });
				menu.add(play);
				contentPane.add(menu, "CoolChess Menu");
				
				JPanel lobby = new JPanel();
				
				ArrayList<String> players = new ArrayList<String>();
				players = ch.getPlayerlist();
				/*for(int i = 0; i < players.size(); i++) {
					list.addElement(players.get(i));
				}*/
				
				JButton	challenge = new JButton("Send Challenge");
				JList people = new JList(list);
				people.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				people.setLayoutOrientation(JList.VERTICAL);
				people.setVisibleRowCount(-1);
				people.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting() == false) {
							if(people.getSelectedIndex() == -1) {
								challenge.setEnabled(false);
							}
							else {
								challenge.setEnabled(true);
							}
						}
					}	
				});
				
				JButton refresh = new JButton("Refresh");
			    refresh.addActionListener(new ActionListener() {
			    	public void actionPerformed(ActionEvent e) {
			    		ch.clearPlayerlist();
			    		try {
							ch.raw("PLAYERLIST");
							ch.setResponse();
							System.out.println(ch.getResponse());
							String[] responseIn = ch.getResponse().split(" ");
							int listSize = Integer.parseInt(responseIn[1]);
							for (int i = 0; i < listSize; i++){
								ch.setResponse();
								String response = ch.getResponse();
								if (response.contains("PLAYERS")){
									String[] responseSplit = response.split(" ");
									ch.setPlayerlist(responseSplit[1]);
									//list.addElement(responseSplit[1]);
									//System.out.println(responseSplit[1] + " added");
								}
							}
			    		} catch (Exception e1) {
							System.out.println(e1);
						}
			    		//people = new JList(list)
			    		list.clear();
			    		ArrayList<String> listPlayer = new ArrayList<String>();
			    		listPlayer = ch.getPlayerlist();
			    		for(int i = 0; i < listPlayer.size(); i++) {
			    			//System.out.println(listPlayer.get(i));
			    			list.addElement(listPlayer.get(i));
			    		}
			    	}
			    });
			    
			    //challenge
				challenge.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String opponent = (String) people.getSelectedValue();
						try {
							ch.raw("challenge " + opponent);
						} catch (Exception e1) {
							System.out.println(e1);
						}
						
					}
				});
				
				JButton ready = new JButton("Ready");
			    ready.addActionListener(new ActionListener() {
			    	public void actionPerformed(ActionEvent e) {
			    		ready.setEnabled(false);
			    		Thread t = new Thread(new Client(ch)); 
						t.start();
			    	}
			    });
				JScrollPane listScroller = new JScrollPane(people);
				listScroller.setPreferredSize(new Dimension(250, 400));
				
				if (gameChallenge){
					Object[] options = {"Accpet","Decline"};
					int n = JOptionPane.showOptionDialog(new JFrame(),
							"You have received a challenge from " + challengeUser,
							"Challenge Recieved",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							options,
							options[1]);
					try {
						if (n == 0){		
							ch.raw(challengeUser + " accept");
						}
						else{
							ch.raw(challengeUser + " denied");
						}
					} catch (Exception e1) {
						System.out.println(e1);
					}
				}
				
				if (challengeAccept){
					JOptionPane.showMessageDialog(new JFrame(),
							"Your challenge has been accepted",
							"Challenge Accepted",
							JOptionPane.PLAIN_MESSAGE);
					challengeUser = null;
					gameChallenge = false;
					challengeAccept = false;
					cardLayout.next(contentPane);
				}
				
				if (challengeDenied){
					JOptionPane.showMessageDialog(new JFrame(),
							"Your challenge has been declined",
							"Challenge declined",
							JOptionPane.PLAIN_MESSAGE);
					challengeUser = null;
					gameChallenge = false;
					challengeDenied = false;
				}
				
				lobby.add(listScroller);
				lobby.add(challenge);
				lobby.add(refresh);
				lobby.add(ready);
				
				contentPane.add(lobby, "Player Lobby");
				
				contentPane.add(cb.getGui(), "CoolChess");
				
				//frame.add(cb.getGui());
	            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            frame.setLocationByPlatform(true);
	            
	            frame.pack();
	            
	            frame.setMinimumSize(frame.getSize());
	            frame.setVisible(true);
	            
			}
		};
		
		//Connect with server and setup Buffered readers and writers	
		ch.connect();
		ch.setResponse();
		System.out.println(ch.getResponse());
		SwingUtilities.invokeLater(r);
		ch.setResponse();
		System.out.println(ch.getResponse());
		
		/*
		boolean userEntry = true;
		while(userEntry){
			ch.setResponse();
			System.out.println("Server: " + ch.getResponse());
			//input = in.nextLine();
			
			SwingUtilities.invokeLater(r);
			//ch.user(input);
			ch.setResponse();
			
			if (ch.getResponse().equals("Username accepted")){
				System.out.println("Server: " + ch.getResponse());
				userEntry = false;
				break;
			}
		}*/
		
		/*boolean getPlayerlist = true;
		while (getPlayerlist){
			ch.setResponse();
			String response = ch.getResponse();
			if (response.contains("PLAYERS")){
				String[] responseSplit = response.split(" ");
				System.out.println(responseSplit[1]);
				ch.setPlayerlist(responseSplit[1]);
			}
			else{
				getPlayerlist = false;
				break;
			}
		}*/
		//Thread t = new Thread(new Client(ch)); 
		//t.start();
		
		//Setting up while loop, only exit upon sending QUIT to FTP server
		/*boolean exit = false;
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
				
			case 6: ch.printPlayerlist();
				break;
				 
			//to get any unwanted answers
			default: System.out.println("Invalid input");
				break;
			}
		}
		in.close();*/
		//t.interrupt();
		//System.exit(0);
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
					if (responseSplit.length == 3 && response.contains(me)){
						String userMessage = responseSplit[0].substring(0,responseSplit[0].length()-1);
						if(responseSplit[1].equals("challenge") && responseSplit[2].equals(me)){
							gameChallenge = true;
							challengeUser = userMessage;
							System.out.println("Challenge sent from " + userMessage);
							System.out.println("Do you accept?");
						}
						else if(responseSplit[1].equals(me) && responseSplit[2].equals("accept")){
							challengeAccept = true;
							challengeUser = userMessage;
							System.out.println("Challenge accepted by " + userMessage);
							ch.raw("GAME " + me + " " + userMessage);
						}
						else if(responseSplit[1].equals(me) && responseSplit[2].equals("denied")){
							challengeDenied = true;
							challengeUser = userMessage;
							System.out.println("Challenge denied by " + userMessage);
						}
					}
					/*else if (response.contains("PLAYERS")){
						return;
						ch.setPlayerlist(responseSplit[1]);
						list.addElement(responseSplit[1]);
					}
					else{
						System.out.println("Server: " + response);
					}*/
				}
				if (gameChallenge){
					Object[] options = {"Accept","Decline"};
					int n = JOptionPane.showOptionDialog(new JFrame(),
							"You have received a challenge from " + challengeUser,
							"Challenge Recieved",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							options,
							options[1]);
					try {
						if (n == 0){		
							ch.raw(challengeUser + " accept");
							cardLayout.next(contentPane);
						}
						else{
							ch.raw(challengeUser + " denied");
						}
					} catch (Exception e1) {
						System.out.println(e1);
					}
					gameChallenge = false;
				}
				
				if (challengeAccept){
					JOptionPane.showMessageDialog(new JFrame(),
							"Your challenge has been accepted",
							"Challenge Accepted",
							JOptionPane.PLAIN_MESSAGE);
					challengeUser = null;
					gameChallenge = false;
					challengeAccept = false;
					cardLayout.next(contentPane);
				}
				
				if (challengeDenied){
					JOptionPane.showMessageDialog(new JFrame(),
							"Your challenge has been declined",
							"Challenge declined",
							JOptionPane.PLAIN_MESSAGE);
					challengeUser = null;
					gameChallenge = false;
					challengeDenied = false;
				}
			}
		}catch(Exception e){
			System.out.println(e);
			try{
				System.out.println("Attempt reconnect in 30 seconds");
				Thread.sleep(30000);
				ch.connect();
				ch.user(ch.getUser());
			} catch (Exception e1) {
				System.out.println(e);
				System.out.println("Reconnect fail");
				System.exit(0);
			}
		}
		
	}
	
}
