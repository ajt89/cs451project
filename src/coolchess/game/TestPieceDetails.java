package coolchess.game;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestPieceDetails {
	
	private Piece p;

	@Test
	public void testBishop() {
		p = new Bishop(PieceTypes.Color.BLACK, PieceTypes.Type.BISHOP, new Cell(0, 0));
		assertTrue(PieceTypes.Color.BLACK == p.getColor());
		assertTrue(PieceTypes.Type.BISHOP == p.getType());
		assertTrue((new Cell(0, 0)).equals(p.getLoc()));
		assertTrue(("[BLACK, BISHOP, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertTrue(p.equals(new Bishop(PieceTypes.Color.BLACK, PieceTypes.Type.BISHOP, new Cell(0, 0))));
		assertFalse(PieceTypes.Color.WHITE == p.getColor());
		assertFalse(PieceTypes.Type.KNIGHT == p.getType());
		assertFalse((new Cell(1, 1)).equals(p.getLoc()));
		assertFalse(("[WHITE, BISHOP, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertFalse(p.equals(new Bishop(PieceTypes.Color.WHITE, PieceTypes.Type.BISHOP, new Cell(5, 5))));
	}
	
	@Test
	public void testKing() {
		p = new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 0));
		assertTrue(PieceTypes.Color.BLACK == p.getColor());
		assertTrue(PieceTypes.Type.KING == p.getType());
		assertTrue((new Cell(0, 0)).equals(p.getLoc()));
		assertTrue(("[BLACK, KING, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertTrue(p.equals(new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 0))));
		assertFalse(PieceTypes.Color.WHITE == p.getColor());
		assertFalse(PieceTypes.Type.BISHOP == p.getType());
		assertFalse((new Cell(1, 1)).equals(p.getLoc()));
		assertFalse(("[WHITE, BISHOP, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertFalse(p.equals(new Bishop(PieceTypes.Color.WHITE, PieceTypes.Type.BISHOP, new Cell(5, 5))));
	}
	
	@Test
	public void testKnight() {
		p = new Knight(PieceTypes.Color.BLACK, PieceTypes.Type.KNIGHT, new Cell(0, 0));
		assertTrue(PieceTypes.Color.BLACK == p.getColor());
		assertTrue(PieceTypes.Type.KNIGHT == p.getType());
		assertTrue((new Cell(0, 0)).equals(p.getLoc()));
		assertTrue(("[BLACK, KNIGHT, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertTrue(p.equals(new Knight(PieceTypes.Color.BLACK, PieceTypes.Type.KNIGHT, new Cell(0, 0))));
		assertFalse(PieceTypes.Color.WHITE == p.getColor());
		assertFalse(PieceTypes.Type.BISHOP == p.getType());
		assertFalse((new Cell(1, 1)).equals(p.getLoc()));
		assertFalse(("[WHITE, BISHOP, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertFalse(p.equals(new Bishop(PieceTypes.Color.WHITE, PieceTypes.Type.BISHOP, new Cell(5, 5))));
	}
	
	@Test
	public void testPawn() {
		p = new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(0, 0));
		assertTrue(PieceTypes.Color.BLACK == p.getColor());
		assertTrue(PieceTypes.Type.PAWN == p.getType());
		assertTrue((new Cell(0, 0)).equals(p.getLoc()));
		assertTrue(("[BLACK, PAWN, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertTrue(p.equals(new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(0, 0))));
		assertFalse(PieceTypes.Color.WHITE == p.getColor());
		assertFalse(PieceTypes.Type.BISHOP == p.getType());
		assertFalse((new Cell(1, 1)).equals(p.getLoc()));
		assertFalse(("[WHITE, BISHOP, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertFalse(p.equals(new Bishop(PieceTypes.Color.WHITE, PieceTypes.Type.BISHOP, new Cell(5, 5))));
	}
	
	@Test
	public void testQueen() {
		p = new Queen(PieceTypes.Color.BLACK, PieceTypes.Type.QUEEN, new Cell(0, 0));
		assertTrue(PieceTypes.Color.BLACK == p.getColor());
		assertTrue(PieceTypes.Type.QUEEN == p.getType());
		assertTrue((new Cell(0, 0)).equals(p.getLoc()));
		assertTrue(("[BLACK, QUEEN, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertTrue(p.equals(new Queen(PieceTypes.Color.BLACK, PieceTypes.Type.QUEEN, new Cell(0, 0))));
		assertFalse(PieceTypes.Color.WHITE == p.getColor());
		assertFalse(PieceTypes.Type.BISHOP == p.getType());
		assertFalse((new Cell(1, 1)).equals(p.getLoc()));
		assertFalse(("[WHITE, BISHOP, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertFalse(p.equals(new Bishop(PieceTypes.Color.WHITE, PieceTypes.Type.BISHOP, new Cell(5, 5))));
	}
	
	@Test
	public void testRook() {
		p = new Rook(PieceTypes.Color.BLACK, PieceTypes.Type.ROOK, new Cell(0, 0));
		assertTrue(PieceTypes.Color.BLACK == p.getColor());
		assertTrue(PieceTypes.Type.ROOK == p.getType());
		assertTrue((new Cell(0, 0)).equals(p.getLoc()));
		assertTrue(("[BLACK, ROOK, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertTrue(p.equals(new Rook(PieceTypes.Color.BLACK, PieceTypes.Type.ROOK, new Cell(0, 0))));
		assertFalse(PieceTypes.Color.WHITE == p.getColor());
		assertFalse(PieceTypes.Type.BISHOP == p.getType());
		assertFalse((new Cell(1, 1)).equals(p.getLoc()));
		assertFalse(("[WHITE, BISHOP, " + (new Cell(0, 0).toString()) + "]").equals(p.toString()));
		assertFalse(p.equals(new Bishop(PieceTypes.Color.WHITE, PieceTypes.Type.BISHOP, new Cell(5, 5))));
	}
}
