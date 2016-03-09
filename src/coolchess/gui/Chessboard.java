package coolchess.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import coolchess.client.ClientHelper;
import coolchess.game.*;

public class Chessboard {

	private boolean white;
	private static JFrame frame = new JFrame();
	private static Container contentPane = frame.getContentPane();
	private static CardLayout cardLayout = new CardLayout();
	private static ArrayList<String> names = new ArrayList<String>();
	private JPanel gui = new JPanel(new BorderLayout(3, 3));
	private JButton[][] squares = new JButton[8][8];
	private JPanel board;
	public static final int QUEEN = 0, KING = 1, ROOK = 2, KNIGHT = 3, BISHOP = 4, PAWN = 5;
	private int[] starting = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK};
	private Image[][] pieces = new Image[2][6];
	private Board b = new Board(BoardState.WHITE_TURN);
	private boolean active = true;
	private boolean player;
	private boolean isViable;
	private int activex;
	private int activey;
	//ClientHelper ch;
	
	public Chessboard(ClientHelper ch, CardLayout cl) {
		//this.ch=ch;
		initialize(ch, cl);
	}
	
	public final void initialize(ClientHelper ch, CardLayout cl) {
		createImages();
		
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JToolBar options = new JToolBar();
		gui.add(options, BorderLayout.PAGE_START);
		//add all the options
		JButton surrender = new JButton("Surrender");
		surrender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surrender(ch, cl);
			}
		});
		options.add(surrender);
		JButton flip = new JButton("Flip");
		flip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flipBoard();
			}
		});
		options.add(flip);
		board = new JPanel(new GridLayout(0,8));
		board.setBorder(new LineBorder(Color.BLACK));
		gui.add(board);
		
		Insets margins = new Insets(0,0,0,0);
		for(int i = 0; i < squares.length; i++) {
			for(int j = 0; j < squares[i].length; j++) {
				JButton button = new JButton();
				button.setMargin(margins);
				ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
				button.setIcon(icon);
				
				if((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					button.setBackground(Color.WHITE);
				}
				else {
					button.setBackground(Color.BLACK);
				}
				squares[i][j] = button;
				//Rectangle rect = squares[i][j].getBounds();
			}
		}
		player = true;
		for(int i = 0; i < squares.length; i++) {
			for(int j = 0; j < squares[i].length; j++) {
				board.setLocation(i, j);
				//System.out.println(board.getBounds().x);
				int xcord = board.getBounds().x;
				int ycord = board.getBounds().y;
				squares[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(player) {
							if(active) {
								showViableMoves(xcord, ycord);
							}
							else {
								//check if current location is within the viable list
								//if(is in viable list) {
								isViable = true;
								//}
								if(isViable) {
									movePiece(activex, activey, xcord, ycord);
									active = true;
								}
								else {
									active = true;
									for(int k = 0; k < 10; k++) {
										//change colors back to black and white through math
									}
								}
							}
						}
					}	
				});
				board.add(squares[i][j]);
			}
		}
		
		setupBoard();
	}
	
	public void surrender(ClientHelper ch, CardLayout cl) {
		Object[] options = {"Yes", "No"};
		int n = JOptionPane.showConfirmDialog(frame, "Would you like to surrender?", "Surrender",
			    JOptionPane.YES_NO_OPTION);
		if(n == 0) {
			JOptionPane.showMessageDialog(frame, "You have lost.", "You lose",JOptionPane.PLAIN_MESSAGE);
			//send win message to opponent
			cl.previous(contentPane);
		}
	}
	
	public void createImages() {
		try {
			File image = new File("pieces.png");
            BufferedImage bi = ImageIO.read(image);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++) {
                    pieces[i][j] = bi.getSubimage(
                            j * 64, i * 64, 64, 64);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
	}
	
	public void setWhite(boolean b) {
		white = b;
	}
	
	private void showViableMoves(int i, int j) {
		//System.out.println(i);
		//System.out.println(j);
		//adjust based on white or !white
		activex = i;
		activey = j;
		//squares[i][j].setBackground(Color.gray);
		active = false;
	}
	
	private void movePiece(int oldi, int oldj, int i, int j) {
		//get image/text from old space
		//String temp = squares[oldi][oldj].getText();
		//adjust based on white or !white
		Icon ic = squares[oldi][oldj].getIcon();
		squares[oldi][oldj].setIcon(null);
		squares[i][j].setIcon(ic);
		//move in board as well
	}
	
	private void update(Board b) {
		//for(int i = 0; i < b.getSize(); i++) {
		//for(int j = 0; j < b.getSize(); j++) {
		Cell[][] cells = b.getCells();
		for(int i = 0; i < b.boardSize; i++) {
			for(int j = 0; j < b.boardSize; j++) {
				switch(cells[i][j].getCellState()) {
					case EMPTY:
						break;
					case WHITE_KING:
						squares[i][j].setText("King");
						squares[i][j].setForeground(Color.RED);
						break;
					case WHITE_QUEEN:
						squares[i][j].setText("Queen");
						squares[i][j].setForeground(Color.RED);
						break;
					case WHITE_ROOK:
						squares[i][j].setText("Rook");
						squares[i][j].setForeground(Color.RED);
						break;
					case WHITE_BISHOP:
						squares[i][j].setText("Bishop");
						squares[i][j].setForeground(Color.RED);
						break;
					case WHITE_KNIGHT:
						squares[i][j].setText("Knight");
						squares[i][j].setForeground(Color.RED);
						break;
					case WHITE_PAWN:
						squares[i][j].setText("Pawn");
						squares[i][j].setForeground(Color.RED);
						break;
					case BLACK_KING:
						squares[i][j].setText("King");
						squares[i][j].setForeground(Color.BLUE);
						break;
					case BLACK_QUEEN:
						squares[i][j].setText("Queen");
						squares[i][j].setForeground(Color.BLUE);
						break;
					case BLACK_ROOK:
						squares[i][j].setText("Rook");
						squares[i][j].setForeground(Color.BLUE);
						break;
					case BLACK_BISHOP:
						squares[i][j].setText("Bishop");
						squares[i][j].setForeground(Color.BLUE);
						break;
					case BLACK_KNIGHT:
						squares[i][j].setText("Knight");
						squares[i][j].setForeground(Color.BLUE);
						break;
					case BLACK_PAWN:
						squares[i][j].setText("Pawn");
						squares[i][j].setForeground(Color.BLUE);
						break;
				}
			}
		}
	}
	
	private void flipBoard() {
		if(white) {
			white = false;
		}
		else {
			white = true;
		}
		JButton[][] temp = new JButton[8][8];
		Insets margins = new Insets(0,0,0,0);
		for(int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {
				JButton button = new JButton();
				button.setMargin(margins);
				ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
				button.setIcon(icon);
				
				if((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					button.setBackground(Color.WHITE);
				}
				else {
					button.setBackground(Color.BLACK);
				}
				
				temp[7-i][7-j] = button;
				
				temp[7-i][7-j].setIcon(squares[i][j].getIcon());
			}
		}
		for(int i = 0; i < temp.length; i++) {
			for(int j = 0; j < temp.length; j++) {
				squares[i][j].setIcon(temp[i][j].getIcon());
			}
		}
	}
	
	private void setupBoard() {
		for(int j = 0; j < squares.length; j++) {
			squares[1][j].setIcon(new ImageIcon(pieces[0][PAWN]));;
			//squares[1][j].setForeground(Color.BLUE);
		}
		for(int j = 0; j < squares.length; j++) {
			squares[6][j].setIcon(new ImageIcon(pieces[1][PAWN]));;
			//squares[6][j].setForeground(Color.RED);
		}
		for(int j = 0; j < squares.length; j++) {
			squares[0][j].setIcon(new ImageIcon(pieces[0][starting[j]]));
			//squares[0][j].setForeground(Color.BLUE);
		}
		for(int j = 0; j < squares.length; j++) {
			squares[7][j].setIcon(new ImageIcon(pieces[1][starting[j]]));
			//squares[7][j].setForeground(Color.RED);
		}
	}
	
	public JComponent getBoard() {
		return board;
	}
	
	public JComponent getGui() {
		return gui;
	}
	
	public static void main(String[] args) {
		ClientHelper ch = new ClientHelper("AJ-PC", 6969);
		Runnable r = new Runnable() {
			public void run() {
				Chessboard cb = new Chessboard(ch, cardLayout);
				//JFrame frame = new JFrame();
				
				//Container contentPane = frame.getContentPane();
				//CardLayout cardLayout = new CardLayout();
				
				contentPane.setLayout(cardLayout);
				JPanel menu = new JPanel();
				JButton play = new JButton("Play");
				play.addActionListener(new ActionListener()
			    {
				      public void actionPerformed(ActionEvent e)
				      {
				    	  String alias = (String)JOptionPane.showInputDialog(frame,"Please enter an alias.");
				    	  //Send string in here
				    	  cardLayout.next(contentPane);
				      }
			    });
				menu.add(play);
				contentPane.add(menu, "CoolChess Menu");
				
				JPanel lobby = new JPanel();
				
				DefaultListModel list = new DefaultListModel();
				/*for(int i = 0; i < 10; i++) {
					list.addElement("hi" + i);
				}*/
				JButton add = new JButton("Add Name");
				add.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String n = (String)JOptionPane.showInputDialog(frame,"Please enter an alias.");
						names.add(n);
						list.clear();
						for(int i = 0; i < names.size(); i++) {
							list.addElement(names.get(i));
						}
					}
				});
				
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
				challenge.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String opponent = (String) people.getSelectedValue();
						//
						cardLayout.next(contentPane);
						System.out.println(opponent);
					}
				});
				JScrollPane listScroller = new JScrollPane(people);
				listScroller.setPreferredSize(new Dimension(250, 400));
				
				lobby.add(listScroller);
				lobby.add(challenge);
				lobby.add(add);
				
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

		SwingUtilities.invokeLater(r);
	}

}
