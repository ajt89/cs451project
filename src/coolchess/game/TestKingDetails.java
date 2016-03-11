package coolchess.game;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestKingDetails {

	private King k;
	
	@Test
	public void testHasMovedDefault() {
		k = new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 0));
		assertFalse(k.hasMoved());
	}

	@Test
	public void testMoved() {
		k = new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 0));
		k.setMoved(true);
		assertTrue(k.hasMoved());
	}
}
