package coolchess.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Move implements Serializable {
	private static final long serialVersionUID = 6068364453892314431L;
	
	private ArrayList<Piece> pieces;
	
	public Move(Piece... pieces){
		this.setPieces(new ArrayList<Piece>(Arrays.asList(pieces)));
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
}
