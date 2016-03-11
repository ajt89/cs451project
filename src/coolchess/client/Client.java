package coolchess.client;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import coolchess.gui.Chessboard;

public class Client implements Runnable{
	static ClientHelper ch;
	static boolean gameChallenge = false;
	static boolean challengeAccept = false;
	static boolean challengeDenied = false;
	static boolean victory = false;
	static String challengeUser;
	static CardLayout cardLayout = new CardLayout();
	static Container contentPane;
	static Chessboard cb;
	
	Client(ClientHelper ch){
		this.ch = ch;
	}
	
	public static void main(String[] args)throws Exception{
		if (args.length != 2){
			System.out.println("Correct usage: java Client <hostname> <server>");
			System.exit(0);
		}
		String serverHost = args[0];
		int portNumber = Integer.parseInt(args[1]);
		ClientHelper ch = new ClientHelper(serverHost,portNumber);	
		
		Runnable r = new Runnable() {
			public void run() {
				
				JFrame frame = new JFrame();
				
				contentPane = frame.getContentPane();
				contentPane.setLayout(cardLayout);
				cb = new Chessboard(ch, cardLayout, contentPane);
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
								String[] responseIn = ch.getResponse().split(" ");
								int listSize = Integer.parseInt(responseIn[1]);
								for (int i = 0; i < listSize; i++){
									ch.setResponse();
									String response = ch.getResponse();
									if (response.contains("PLAYERS")){
										String[] responseSplit = response.split(" ");
										ch.setPlayerlist(responseSplit[1]);
									}
								}
				    		} catch (Exception e1) {
								System.out.println(e1);
							}
				    		list.clear();
				    		ArrayList<String> listPlayer = new ArrayList<String>();
				    		listPlayer = ch.getPlayerlist();
				    		for(int i = 0; i < listPlayer.size(); i++) {
				    			list.addElement(listPlayer.get(i));
				    		}
				      }
			    });
				menu.add(play);
				contentPane.add(menu, "CoolChess Menu");
				
				JPanel lobby = new JPanel();
				
				ArrayList<String> players = new ArrayList<String>();
				players = ch.getPlayerlist();
				
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
								}
							}
			    		} catch (Exception e1) {
							System.out.println(e1);
						}
			    		list.clear();
			    		ArrayList<String> listPlayer = new ArrayList<String>();
			    		listPlayer = ch.getPlayerlist();
			    		for(int i = 0; i < listPlayer.size(); i++) {
			    			list.addElement(listPlayer.get(i));
			    		}
			    	}
			    });
			    
			    //challenge
				challenge.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String opponent = (String) people.getSelectedValue();
						try {
							ch.raw(ch.getUser() + " challenge " + opponent);
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
			    
			    JButton quit = new JButton("Quit");
			    quit.addActionListener(new ActionListener() {
			    	public void actionPerformed(ActionEvent e) {
			    		ready.setEnabled(false);
			    		try {
							ch.QUIT();
							System.exit(0);
						} catch (Exception e1) {
							System.out.println(e1);
						}
			    		
			    	}
			    });
				JScrollPane listScroller = new JScrollPane(people);
				listScroller.setPreferredSize(new Dimension(250, 400));
				
				lobby.add(listScroller);
				lobby.add(challenge);
				lobby.add(refresh);
				lobby.add(ready);
				lobby.add(quit);
				
				contentPane.add(lobby, "Player Lobby");
				
				contentPane.add(cb.getGui(), "CoolChess");
				
	            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            frame.setLocationByPlatform(true);
	            
	            frame.pack();
	            
	            frame.setMinimumSize(frame.getSize());
	            frame.setVisible(true);
	            
			}
		};
		
		ch.connect();
		ch.setResponse();
		System.out.println(ch.getResponse());
		SwingUtilities.invokeLater(r);
		ch.setResponse();
		System.out.println(ch.getResponse());
	}

	@Override
	public void run() {
		String me = ch.getUser();
		try{
			while (ch.getSocketString().isConnected()){
				ch.setResponse();
				String response = ch.getResponse();
				if (response != null){
					System.out.println(response);
					String[] responseSplit = response.split(" ");
					if (responseSplit.length == 3 && response.contains(me)){
						String userMessage = responseSplit[0];
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
						}
						else if(responseSplit[1].equals(me) && responseSplit[2].equals("denied")){
							challengeDenied = true;
							challengeUser = userMessage;
							System.out.println("Challenge denied by " + userMessage);
						}
					}
					else if (response.contains("GAME") && response.contains(me)){
						ch.setupGame();
						new Thread(MoveListen).start();
					}
					else if (response.contains("COUNTER")){
						int counter = Integer.parseInt(responseSplit[1]);
						ch.setCounter(counter);
					}
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
							ch.raw("GAME " + challengeUser + " " + me);
							//ch.setupGame();
							cb.flipBoard();
							cb.setWhite(false);
							cb.setPlayer(false);
							cardLayout.next(contentPane);
							new Thread(SurrenderListen).start();
							new Thread(LossListen).start();
							new Thread(TieListen).start();
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
					cb.setWhite(true);
					cb.setPlayer(true);
					cardLayout.next(contentPane);
					new Thread(SurrenderListen).start();
					new Thread(LossListen).start();
					new Thread(TieListen).start();
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
	
	static Runnable SurrenderListen = new Runnable(){
		public void run() {
			try{
				System.out.println("SurrenderListen running");
				boolean inProgress = true;
				String input;
				while(inProgress){
					ch.setResponse();
					input = ch.getResponse();
					System.out.println(input);
					System.out.println("Client: " + input);
					if (input.contains("VICTORY") && input.contains(ch.getUser())){
						inProgress = false;
						System.out.println("VICTORY");
						JOptionPane.showMessageDialog(new JFrame(), "You win.", "Victory",JOptionPane.PLAIN_MESSAGE);
						cardLayout.previous(contentPane);
						break;
					}
				}
			} catch(Exception e){
				System.out.println(e);
			}
			
		}
		
	};
	
	static Runnable LossListen = new Runnable(){
		public void run() {
			try{
				System.out.println("SurrenderListen running");
				boolean inProgress = true;
				String input;
				while(inProgress){
					ch.setResponse();
					input = ch.getResponse();
					System.out.println(input);
					System.out.println("Client: " + input);
					if (input.contains("LOSS") && input.contains(ch.getUser())){
						inProgress = false;
						System.out.println("LOSS");
						JOptionPane.showMessageDialog(new JFrame(), "You lost.", "Defeat",JOptionPane.PLAIN_MESSAGE);
						cardLayout.previous(contentPane);
						break;
					}
				}
			} catch(Exception e){
				System.out.println(e);
			}
			
		}
		
	};
	
	static Runnable TieListen = new Runnable(){
		public void run() {
			try{
				System.out.println("SurrenderListen running");
				boolean inProgress = true;
				String input;
				while(inProgress){
					ch.setResponse();
					input = ch.getResponse();
					System.out.println(input);
					System.out.println("Client: " + input);
					if (input.contains("TIE") && input.contains(ch.getUser())){
						inProgress = false;
						System.out.println("TIE");
						JOptionPane.showMessageDialog(new JFrame(), "Tie.", "Tie?",JOptionPane.PLAIN_MESSAGE);
						cardLayout.previous(contentPane);
						break;
					}
				}
			} catch(Exception e){
				System.out.println(e);
			}
			
		}
		
	};
	
	static Runnable MoveListen = new Runnable(){
		public void run(){
			try{
				System.out.println("MoveListen running");
				while(true){
					ch.setBoard();
					System.out.println("Recieved move");
					cb.recieveBoard(ch);
					System.out.println("Sent move to chessboard");
				}
			} catch (Exception e){
				System.out.println(e);
			}
		}
	};
	
}
