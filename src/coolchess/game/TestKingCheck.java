package coolchess.game;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestKingCheck {
	
	private Board b;
	private King k;

	@Test
	public void testNotCheck() {
		b = new Board(BoardState.BLACK_TURN);
		k = (King)(b.getPiecesOfTypes(PieceTypes.Color.BLACK, PieceTypes.Type.KING).get(0));
		assertFalse(k.inCheck(b));
	}
	
	@Test
	public void testCheck() {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 0)));
		pieces.add(new Rook(PieceTypes.Color.WHITE, PieceTypes.Type.ROOK, new Cell(1, 0)));
		pieces.add(new Rook(PieceTypes.Color.WHITE, PieceTypes.Type.ROOK, new Cell(0, 1)));
		b = new Board(BoardState.BLACK_TURN, pieces);
		k = (King)(b.getPiecesOfTypes(PieceTypes.Color.BLACK, PieceTypes.Type.KING).get(0));
		assertTrue(k.inCheck(b));
	}

}
