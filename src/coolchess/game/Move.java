package coolchess.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Move implements Serializable {
	private static final long serialVersionUID = 6068364453892314431L;
	
	private ArrayList<Piece> pieces;
	private ArrayList<Cell> cells;
	
	// needs to be an arraylist of pieces and arraylist of cells that those pieces are going to go to
	// add constructor for single piece
	// contract implies that these are the same length
	public Move(ArrayList<Piece> pieces, ArrayList<Cell> cells) throws InvalidMoveException{
		if(pieces.size() != cells.size()){
			throw new InvalidMoveException("Number of pieces and cells aren't equal");
		}
		
		this.pieces = pieces;
		this.setCells(cells);
	}
	
	public Move(Piece piece, Cell cell){
		pieces = new ArrayList<Piece>();
		pieces.add(piece);
		cells = new ArrayList<Cell>();
		cells.add(cell);
	}

	public Move(Move m){
		this.pieces = m.getPieces();
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}
	
	public ArrayList<Cell> getCells() {
		return cells;
	}

	public void setCells(ArrayList<Cell> cells) {
		this.cells = cells;
	}

	public static class InvalidMoveException extends Exception{
		private static final long serialVersionUID = -2862426984521252427L;

		public InvalidMoveException(){
			super();
		}

		public InvalidMoveException(String message){
			super(message);
		}
		
		public InvalidMoveException(String message, Throwable cause){
			super(message, cause);
		}
		
		public InvalidMoveException(Throwable cause){
			super(cause);
		}
	}
}
