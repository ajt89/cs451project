package coolchess.game;

import java.awt.Point;

public class Cell {
	private Point position;
	private CellState state;
	
	public Cell(int num, int let, CellState state){
		this.position = new Point(let, num); // x => letter, y => number
		this.state = state;
	}
	
	public Cell(Cell c){
		this.position = c.getPosition();
		this.state = c.getCellState();
	}
	
	public Point getPosition(){
		return new Point(this.position);
	}
	
	public CellState getCellState(){
		return state;
	}
	
	public void setCellState(CellState state){
		this.state = state;
	}
	
	public String toIcon(){
		switch(this.state){
		case EMPTY:
			return " ";
		case WHITE_KING:
			return "♔";
		case WHITE_QUEEN:
			return "♕";
		case WHITE_ROOK:
			return "♖";
		case WHITE_BISHOP:
			return "♗";
		case WHITE_KNIGHT:
			return "♘";
		case WHITE_PAWN:
			return "♙";
		case BLACK_KING:
			return "♚";
		case BLACK_QUEEN:
			return "♛";
		case BLACK_ROOK:
			return "♜";
		case BLACK_BISHOP:
			return "♝";
		case BLACK_KNIGHT:
			return "♞";
		case BLACK_PAWN:
			return "♟";
		default:
			return "?";
		}
	}
}
