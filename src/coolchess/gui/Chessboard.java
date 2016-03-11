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

	private boolean white = true;
	private String playerName;
	private static JFrame frame = new JFrame();
	private static Container contentPane = frame.getContentPane();
	private static CardLayout cardLayout = new CardLayout();
	private static ArrayList<String> names = new ArrayList<String>();
	private JPanel gui = new JPanel(new BorderLayout(3, 3));
	private JButton[][] squares = new JButton[8][8];
	private JPanel board;
	public static final int QUEEN = 1, KING = 0, ROOK = 2, KNIGHT = 3, BISHOP = 4, PAWN = 5;
	private int[] starting = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK};
	private Image[][] pieces = new Image[2][6];
	//private Board b = new Board(BoardState.WHITE_TURN);
	private Manager man = new Manager(BoardState.WHITE_TURN);
	private ArrayList<Cell> old;
	private boolean active = true;
	private boolean player;
	private boolean isViable;
	private int activex;
	private int activey;
	//ClientHelper ch;
	
	public Chessboard(ClientHelper ch, CardLayout cl, Container cp) {
		//this.ch=ch;
		initialize(ch, cl, cp);
	}
	
	public final void initialize(ClientHelper ch, CardLayout cl, Container cp) {
		createImages();
		
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JToolBar options = new JToolBar();
		gui.add(options, BorderLayout.PAGE_START);
		//add all the options
		JButton surrender = new JButton("Surrender");
		surrender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surrender(ch, cl, cp);
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
		//uncomment for testing
		//player = true;
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
								if(white) {
									Cell c = new Cell(xcord, ycord);
									if(man.getBoard().getPiece(c).getColor() == PieceTypes.Color.WHITE) {
										showViableMoves(xcord, ycord);
									}
								}
								else {
									Cell c = new Cell(7-xcord, 7-ycord);
									if(man.getBoard().getPiece(c).getColor() == PieceTypes.Color.BLACK) {
										showViableMoves(xcord, ycord);
									}
								}
							}
							else {
								//check if current location is within the viable list
								//if(is in viable list) {
								//isViable = true;
								//}
								for(int k = 0; k < old.size(); k++) {
									Cell c = old.get(k);
									if(xcord == c.getNum() && ycord == c.getLet() && white) {
										isViable = true;
									}
									else if(xcord == 7-c.getNum() && ycord == 7-c.getLet() && !white){
										isViable = true;
									}
								}
								if(isViable) {
									movePiece(activex, activey, xcord, ycord, ch);
									//comment out for testing
									player = false;
								}
								active = true;
								isViable = false;
								color();
							}
						}
						else {
							//squares[xcord][ycord].setEnabled(false);
							//receiveMove(ch);
							//player = true;
						}
					}	
				});
				board.add(squares[i][j]);
			}
		}
		
		setupBoard();
	}
	
	public void surrender(ClientHelper ch, CardLayout cl, Container cp) {
		//Object[] options = {"Yes", "No"};
		int n = JOptionPane.showConfirmDialog(frame, "Would you like to surrender?", "Surrender",
			    JOptionPane.YES_NO_OPTION);
		if(n == 0) {
			JOptionPane.showMessageDialog(frame, "You have lost.", "You lose",JOptionPane.PLAIN_MESSAGE);
			//send win message to opponent
			try {
				ch.raw(ch.getUser() + " SURRENDER");
			} catch (Exception e) {
				e.printStackTrace();
			}
			cl.previous(cp);
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
		ArrayList<Cell> viable = new ArrayList<Cell>();
		System.out.println(white);
		if(white) {
			activex = i;
			activey = j;	
			viable = man.viableLocations(i, j);
		}
		else {
			activex = 7 - i;
			activey = 7 - j;
			viable = man.viableLocations(7-i, 7-j);
		}
		System.out.println(activex + " " + activey);
		//System.out.println(viable.size());
		//viable.add(new Cell(3, 5));
		old = viable;
		for(int k = 0; k < viable.size(); k++) {
			Cell c = viable.get(k);
			if(white) {
				squares[c.getNum()][c.getLet()].setBackground(Color.RED);
			}
			else {
				squares[7-c.getNum()][7-c.getLet()].setBackground(Color.RED);
			}
		}
		active = false;
	}
	
	private void color() {
		Insets margins = new Insets(0,0,0,0);
		for(int i = 0; i < old.size(); i++) {
			JButton button = new JButton();
			button.setMargin(margins);
			ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
			button.setIcon(icon);
			
			int x;
			int y;
			
			if(!white) {
				x = old.get(i).getNum();
				y = old.get(i).getLet();
			}
			else {
				x = 7 - old.get(i).getNum();
				y = 7 - old.get(i).getLet();
			}
			System.out.println("Should recolor:" + x + " " + y);
			if((x % 2 == 0 && y % 2 == 0) || (x % 2 == 1 && y % 2 == 1)) {
				//button.setBackground(Color.WHITE);
				squares[x][y].setBackground(Color.WHITE);
			}
			else {
				//button.setBackground(Color.BLACK);
				squares[x][y].setBackground(Color.BLACK);
			}
			//squares[x][y] = button;
		}
	}
	
	private void receiveMove(ClientHelper ch) {
		boolean listening = true;
		Move m = null;
		while(listening) {
			try {
				m = ch.getMove();
				if(m != null) {
					listening = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		man.doMove(m);
		white = !white;
		update();
	}
	
	private void movePiece(int oldi, int oldj, int i, int j, ClientHelper ch) {
		//get image/text from old space
		//String temp = squares[oldi][oldj].getText();
		//adjust based on white or !white
		//boolean listening = false;
		Move m = null;
		if(white) {
			Icon ic = squares[oldi][oldj].getIcon();
			squares[oldi][oldj].setIcon(null);
			squares[i][j].setIcon(ic);
			Piece p = man.getBoard().getPiece(new Cell(oldi, oldj));
			/*(if(listening) {
				while(listening) {
					try {
						m = ch.getMove();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else {*/
				m = new Move(p, new Cell(i, j));
			//}
			man.doMove(m);
		}
		else {
			Icon ic = squares[7-oldi][7-oldj].getIcon();
			squares[7-oldi][7-oldj].setIcon(null);
			squares[i][j].setIcon(ic);
			Piece p = man.getBoard().getPiece(new Cell(oldi, oldj));
			/*if(!listening) {
				try {
					m = ch.getMove();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {*/
				m = new Move(p, new Cell(7-i, 7-j));
			//}
			man.doMove(new Move(p, new Cell(7-i, 7-j)));
		}
		white = !white;
		//man.doMove();
	}
	
	private void update() {
		ArrayList<Piece> p = man.getBoard().getPieces();
		
		for(int i = 0; i < squares.length; i++) {
			for(int j = 0; j < squares.length; j++) {
				squares[i][j].setIcon(null);
			}
		}
		if(white) {
			for(int i = 0; i < p.size(); i++) {
				Piece pi = p.get(i);
				Cell c = pi.getLoc();
				int x = c.getNum();
				int y = c.getLet();
				switch (pi.getType()) {
				case BISHOP:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][BISHOP]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][BISHOP]));
					}
					break;
				case KING:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][KING]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][KING]));
					}
					break;
				case KNIGHT:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][KNIGHT]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][KNIGHT]));
					}
					break;
				case PAWN:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][PAWN]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][PAWN]));
					}
					break;
				case QUEEN:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][QUEEN]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][QUEEN]));
					}
					break;
				case ROOK:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][ROOK]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][ROOK]));
					}
					break;
				default:
					break;
				
				}
			}
		}
		else {
			for(int i = 0; i < p.size(); i++) {
				Piece pi = p.get(i);
				Cell c = pi.getLoc();
				int x = 7 - c.getNum();
				int y = 7 - c.getLet();
				switch (pi.getType()) {
				case BISHOP:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][BISHOP]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][BISHOP]));
					}
					break;
				case KING:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][KING]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][KING]));
					}
					break;
				case KNIGHT:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][KNIGHT]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][KNIGHT]));
					}
					break;
				case PAWN:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][PAWN]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][PAWN]));
					}
					break;
				case QUEEN:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][QUEEN]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][QUEEN]));
					}
					break;
				case ROOK:
					if(pi.getColor() == PieceTypes.Color.BLACK) {
						squares[x][y].setIcon(new ImageIcon(pieces[0][ROOK]));
					}
					else {
						squares[x][y].setIcon(new ImageIcon(pieces[1][ROOK]));
					}
					break;
				default:
					break;
				
				}
			}
		}
		white = !white;
	}
	
	public void setPlayer(boolean b) {
		player = b;
	}
	
	public void flipBoard() {
		/*if(white) {
			white = false;
		}
		else {
			white = true;
		}*/
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
	
	public void setPlayerName(String s) {
		playerName = s;
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
				Chessboard cb = new Chessboard(ch, cardLayout, contentPane);
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
