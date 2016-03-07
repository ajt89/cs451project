package coolchess.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

import coolchess.game.*;

public class Chessboard {

	private JPanel gui = new JPanel(new BorderLayout(3, 3));
	private JButton[][] squares = new JButton[8][8];
	private JPanel board;
	private String[] starting = {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};
	private Board b;
	private boolean active = true;
	private boolean isViable;
	private int activex;
	private int activey;
	
	public Chessboard() {
		initialize();
	}
	
	public final void initialize() {
		
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JToolBar options = new JToolBar();
		gui.add(options, BorderLayout.PAGE_START);
		//add all the options
		options.add(new JButton("Surrender"));
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
		
		for(int i = 0; i < squares.length; i++) {
			for(int j = 0; j < squares[i].length; j++) {
				board.setLocation(i, j);
				//System.out.println(board.getBounds().x);
				int xcord = board.getBounds().x;
				int ycord = board.getBounds().y;
				squares[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(active) {
							showViableMoves(xcord, ycord);
						}
						else {
							//check if current location is within the viable list
							//if(is in viable list) {
								
							//}
							if(isViable) {
								movePiece(activex, activey, xcord, ycord);
							}
							else {
								active = false;
								for(int k = 0; k < 10; k++) {
									//change colors back to black and white through math
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
	
	private void showViableMoves(int i, int j) {
		//System.out.println(i);
		//System.out.println(j);
		activex = i;
		activey = j;
		squares[i][j].setBackground(Color.RED);
	}
	
	private void movePiece(int oldi, int oldj, int i, int j) {
		//get image/text from old space
		String temp = squares[oldi][oldj].getText();
		squares[oldi][oldj].setText("");
		squares[i][j].setText(temp);
	}
	
	private void setupBoard() {
		for(int j = 0; j < squares.length; j++) {
			squares[1][j].setText("Pawn");
			squares[1][j].setForeground(Color.BLUE);
		}
		for(int j = 0; j < squares.length; j++) {
			squares[6][j].setText("Pawn");
			squares[6][j].setForeground(Color.RED);
		}
		for(int j = 0; j < squares.length; j++) {
			squares[0][j].setText(starting[j]);
			squares[0][j].setForeground(Color.BLUE);
		}
		for(int j = 0; j < squares.length; j++) {
			squares[7][j].setText(starting[j]);
			squares[7][j].setForeground(Color.RED);
		}
	}
	
	public JComponent getBoard() {
		return board;
	}
	
	public JComponent getGui() {
		return gui;
	}
	
	public static void main(String[] args) {
		Runnable r = new Runnable() {
			public void run() {
				Chessboard cb = new Chessboard();
				JFrame frame = new JFrame("CoolChess");
				frame.add(cb.getGui());
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
