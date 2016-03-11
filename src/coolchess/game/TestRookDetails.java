package coolchess.game;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRookDetails {

	private Rook r;
	
	@Test
	public void testHasMovedDefault() {
		r = new Rook(PieceTypes.Color.BLACK, PieceTypes.Type.ROOK, new Cell(0, 0));
		assertFalse(r.hasMoved());
	}

	@Test
	public void testMoved() {
		r = new Rook(PieceTypes.Color.BLACK, PieceTypes.Type.ROOK, new Cell(0, 0));
		r.setMoved(true);
		assertTrue(r.hasMoved());
	}
}
