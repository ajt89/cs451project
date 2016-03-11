package coolchess.game;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestPawnDetails {
	
	private Pawn p;
	
	@Test
	public void testHasMovedDefault() {
		p = new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(0, 0));
		assertFalse(p.hasMoved());
	}

	@Test
	public void testMoved() {
		p = new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(0, 0));
		p.setMoved(true);
		assertTrue(p.hasMoved());
	}
	
	@Test
	public void testHasAdvancedDefault() {
		p = new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(0, 0));
		assertFalse(p.hasAdvanced());
	}

	@Test
	public void testAdvanced() {
		p = new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(0, 0));
		p.setAdvanced(true);
		assertTrue(p.hasAdvanced());
	}
}
