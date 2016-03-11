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

public class Chessboard2 {

	private String pieceText;
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
	
	public Chessboard2(ClientHelper ch, CardLayout cl, Container cp) {
		//this.ch=ch;
		initialize(ch, cl, cp);
		//new Thread ObjectListener
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
								if(white) {
									Cell c = new Cell(xcord, ycord);
									if(man.getBoard().getPiece(c) != null) {
										if(man.getBoard().getPiece(c).getColor() == PieceTypes.Color.WHITE) {
											showViableMoves(xcord, ycord);
										}
									}
								}
								else {
									Cell c = new Cell(7-xcord, 7-ycord);
									if(man.getBoard().getPiece(c) != null) {
										if(man.getBoard().getPiece(c).getColor() == PieceTypes.Color.BLACK) {
											showViableMoves(xcord, ycord);
										}
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
									//player = false;
								}
								active = true;
								isViable = false;
								color();
							}
						}
						else {
							//squares[xcord][ycord].setEnabled(false);
							//System.out.println("Does it get here on black?");
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
			viable = man.viableLocations(activex, activey);
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
		for(int i = 0; i < squares.length; i++) {
			for(int j = 0; j < squares.length; j++) {
				JButton button = new JButton();
				button.setMargin(margins);
				ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
				button.setIcon(icon);
				
				if((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					squares[i][j].setBackground(Color.WHITE);
				}
				else {
					squares[i][j].setBackground(Color.BLACK);
				}
			}
		}
	}
	
	public void receiveMove(ClientHelper ch) {
		boolean listening = true;
		System.out.println("Inside receive move");
		Board b = null;
		while(listening) {
			try {
				b = ch.getBoard();
				if(b != null) {
					listening = false;
				}
				man = new Manager(b);
				System.out.println("Should've done move");
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		white = !white;
		update();
		player = !player;
	}
	
	private void movePiece(int oldi, int oldj, int i, int j, ClientHelper ch) {
		Move m = null;
		if(white) {
			Icon ic = squares[oldi][oldj].getIcon();
			squares[oldi][oldj].setIcon(null);
			squares[i][j].setIcon(ic);
			Piece p = man.getBoard().getPiece(new Cell(oldi, oldj));
			m = new Move(p, new Cell(i, j));
			man.doMove(m);
			checkPromote();
			/*try {
				System.out.println("send board white 1");
				ch.sendBoard(man.getBoard());
				System.out.println("send board 2");
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}*/
		}
		else {
			Icon ic = squares[7-oldi][7-oldj].getIcon();
			squares[7-oldi][7-oldj].setIcon(null);
			squares[i][j].setIcon(ic);
			Piece p = man.getBoard().getPiece(new Cell(oldi, oldj));
			m = new Move(p, new Cell(7-i, 7-j));
			man.doMove(new Move(p, new Cell(7-i, 7-j)));
			checkPromote();
			/*try {
				System.out.println("send move black 1");
				ch.sendBoard(man.getBoard());
				//System.out.println(m + " 3");
				System.out.println("send move black 2");
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
			System.out.println(m + " 4");*/
		}
		update();
		switch(man.getBoard().getBoardState()) {
		case BLACK_WIN:
			try {
				ch.raw(ch.getUser() + " WIN");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//cl.previous(cp);
			break;
		case TIE:
			try {
				ch.raw(ch.getUser() + " TIE");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//cl.previous(cp);
			break;
		case WHITE_WIN:
			try {
				ch.raw(ch.getUser() + " WIN");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//cl.previous(cp);
			break;
		default:
			break;
		}
		white = !white;
	}
	
	private void checkPromote() {
		ArrayList<Piece> p = new ArrayList<Piece>();
		ButtonGroup bg = new ButtonGroup();
		JRadioButton q = new JRadioButton("Queen");
		JRadioButton r = new JRadioButton("Rook");
		JRadioButton b = new JRadioButton("Bishop");
		JRadioButton k = new JRadioButton("Knight");
		bg.add(q);
		bg.add(r);
		bg.add(b);
		bg.add(k);
		
		frame = new JFrame();
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        Container cont = frame.getContentPane();
        cont.setLayout(new GridLayout(5, 1));
        cont.add(new JLabel("Please choose the piece you would like to promote your pawn into"));
        cont.add(q);
        cont.add(r);
        cont.add(b);
        cont.add(k);
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int x;
				int y;
				ArrayList<Piece> p;
				if(q.isSelected()) {
        			white = !white;
        			if(white) {
        				p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN);
        				for(int i = 0; i < p.size(); i++) {
	        				x = p.get(i).getLoc().getNum();
	        				y = p.get(i).getLoc().getLet();
	        				if(p.get(i).getLoc().getNum() == 0) {
	        					squares[x][y].setIcon(new ImageIcon(pieces[1][QUEEN]));
	        					man.promote(new Cell(x, y), PieceTypes.Type.QUEEN);
	        				}
	       				}
           			}
        			else {
        				p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN);
        				for(int i = 0; i < p.size(); i++) {
        					x = p.get(i).getLoc().getNum();
        					y = p.get(i).getLoc().getLet();
        					if(p.get(i).getLoc().getNum() == 7) {
        						squares[7-x][7-y].setIcon(new ImageIcon(pieces[0][QUEEN]));
        						man.promote(new Cell(x, y), PieceTypes.Type.QUEEN);
        					}
        				}
        			}
        			white = !white;
        		}
        		else if(r.isSelected()) {
        			white = !white;
        			if(white) {
        				p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN);
        				for(int i = 0; i < p.size(); i++) {
        					x = p.get(i).getLoc().getNum();
        					y = p.get(i).getLoc().getLet();
        					if(p.get(i).getLoc().getNum() == 0) {
        						squares[x][y].setIcon(new ImageIcon(pieces[1][ROOK]));
        						man.promote(new Cell(x, y), PieceTypes.Type.ROOK);
        					}
        				}
        			}
        			else {
        				p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN);
        				for(int i = 0; i < p.size(); i++) {
        					x = p.get(i).getLoc().getNum();
        					y = p.get(i).getLoc().getLet();
        					if(p.get(i).getLoc().getNum() == 7) {
        						squares[7-x][7-y].setIcon(new ImageIcon(pieces[0][ROOK]));
        						man.promote(new Cell(x, y), PieceTypes.Type.ROOK);
        					}
        				}
        			}
        			white = !white;
        		}
        		else if(b.isSelected()) {
        			white = !white;
        			if(white) {
        				p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN);
        				for(int i = 0; i < p.size(); i++) {
        					x = p.get(i).getLoc().getNum();
        					y = p.get(i).getLoc().getLet();
        					if(p.get(i).getLoc().getNum() == 0) {
        						squares[x][y].setIcon(new ImageIcon(pieces[1][BISHOP]));
        						man.promote(new Cell(x, y), PieceTypes.Type.BISHOP);
        					}
        				}
        			}
        			else {
        				p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN);
        				for(int i = 0; i < p.size(); i++) {
        					x = p.get(i).getLoc().getNum();
        					y = p.get(i).getLoc().getLet();
        					if(p.get(i).getLoc().getNum() == 7) {
        						squares[7-x][7-y].setIcon(new ImageIcon(pieces[0][BISHOP]));
        						man.promote(new Cell(x, y), PieceTypes.Type.BISHOP);
        					}
        				}
        			}
        			white = !white;
        		}
        		else if(k.isSelected()) {
        			white = !white;
        			if(white) {
        				p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN);
        				for(int i = 0; i < p.size(); i++) {
        					x = p.get(i).getLoc().getNum();
        					y = p.get(i).getLoc().getLet();
        					if(p.get(i).getLoc().getNum() == 0) {
        						squares[x][y].setIcon(new ImageIcon(pieces[1][KNIGHT]));
        						man.promote(new Cell(x, y), PieceTypes.Type.KNIGHT);
        					}
        				}
        			}
        			else {
        				p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN);
        				for(int i = 0; i < p.size(); i++) {
        					x = p.get(i).getLoc().getNum();
        					y = p.get(i).getLoc().getLet();
        					if(p.get(i).getLoc().getNum() == 7) {
        						squares[7-x][7-y].setIcon(new ImageIcon(pieces[0][KNIGHT]));
        						man.promote(new Cell(x, y), PieceTypes.Type.KNIGHT);
        					}
        				}
        			}
        			white = !white;
        		}
        	}
        });
        cont.add(submit);
        int x;
        int y;
		if (white) {
			p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN);
			for(int i = 0; i < p.size(); i++) {
				if(p.get(i).getLoc().getNum() == 0) {
					frame.setVisible(true);
				}
			}
		}
		else {
			p = man.getBoard().getPiecesOfTypes(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN);
			for(int i = 0; i < p.size(); i++) {
				if(p.get(i).getLoc().getNum() == 7) {
					frame.setVisible(true);
				}
			}
		}
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
	}
	
	public void setPlayer(boolean b) {
		player = b;
	}
	
	public void flipBoard() {
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
		}
		for(int j = 0; j < squares.length; j++) {
			squares[6][j].setIcon(new ImageIcon(pieces[1][PAWN]));;
		}
		for(int j = 0; j < squares.length; j++) {
			squares[0][j].setIcon(new ImageIcon(pieces[0][starting[j]]));
		}
		for(int j = 0; j < squares.length; j++) {
			squares[7][j].setIcon(new ImageIcon(pieces[1][starting[j]]));
		}
	}
	
	public void setPlayerName(String s) {
		playerName = s;
	}
	
	public String getPlayerName() {
		return playerName;
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
				Chessboard2 cb = new Chessboard2(ch, cardLayout, contentPane);
				
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
				
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationByPlatform(true);
                
                frame.pack();
                
                frame.setMinimumSize(frame.getSize());
                frame.setVisible(true);
                
                
			}
		};

		SwingUtilities.invokeLater(r);
	}
	/*static Runnable ObjectListener = new Runnable(){
		public void run(){
			receiveMove(ch);
		}
	};*/

}

