package coolchess.game;

import java.io.Serializable;

public class Move implements Serializable {
	private static final long serialVersionUID = 6068364453892314431L;
	
	private Piece piece;
	private Cell cell;
	
	public Move(Piece piece, Cell cell){
		this.piece = piece;
		this.cell = cell;
	}

	public Move(Move m){
		this.piece = m.getPiece();
		this.cell = m.getCell();
	}

	public Piece getPiece() {
		return piece;
	}
	
	public Cell getCell() {
		return cell;
	}
}
