package coolchess.game;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestMoveDetails {
	
	private Move m;

	@Test
	public void test() {
		m = new Move(new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(0, 0)), new Cell(0, 1));
		assertTrue((new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(0, 0))).equals(m.getPiece()));
		assertTrue((new Cell(0, 1).equals(m.getCell())));
		assertTrue(("<" + (new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(0, 0))).toString() + ", " + (new Cell(0, 1).toString()) + ">").equals(m.toString()));
		assertFalse((new Pawn(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN, new Cell(0, 0))).equals(m.getPiece()));
		assertFalse((new Cell(5, 0).equals(m.getCell())));
		assertFalse(("<" + (new Pawn(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN, new Cell(0, 0))).toString() + ", " + (new Cell(0, 1).toString()) + ">").equals(m.toString()));
	}
}
