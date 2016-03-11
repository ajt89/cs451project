package coolchess.game;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestBoard {

	private ArrayList<Piece> pieces;
	private Board b;
	
	@Before
	public void setUp() throws Exception {
		pieces = new ArrayList<Piece>();
		pieces.add(new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 0)));
		pieces.add(new Rook(PieceTypes.Color.WHITE, PieceTypes.Type.ROOK, new Cell(1, 0)));
		pieces.add(new Rook(PieceTypes.Color.WHITE, PieceTypes.Type.ROOK, new Cell(0, 1)));
		pieces.add(new Pawn(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN, new Cell(1, 1)));
		
		b = new Board(BoardState.BLACK_TURN, pieces);
	}

	@Test
	public void testDefaultState() {
		assertTrue(BoardState.BLACK_TURN == b.getBoardState());
		assertFalse(BoardState.WHITE_TURN == b.getBoardState());
	}
	
	@Test
	public void testState() {
		b.setBoardState(BoardState.WHITE_TURN);
		assertTrue(BoardState.WHITE_TURN == b.getBoardState());
		assertFalse(BoardState.BLACK_TURN == b.getBoardState());
	}

	@Test
	public void testPieces(){
		ArrayList<Piece> pieces2 = new ArrayList<Piece>();
		pieces2.add(new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 0)));
		b.setPieces(pieces2);
		assertTrue(pieces2.size() == b.getPieces().size());
		for(int i = 0; i < pieces2.size(); i++){
			assertEquals(pieces2.get(i), b.getPieces().get(i));
		}
	}
	
	@Test
	public void testGetPiecesOfTypes(){
		ArrayList<Piece> pieces2 = new ArrayList<Piece>();
		pieces2.add(new Rook(PieceTypes.Color.WHITE, PieceTypes.Type.ROOK, new Cell(1, 0)));
		pieces2.add(new Rook(PieceTypes.Color.WHITE, PieceTypes.Type.ROOK, new Cell(0, 1)));
		ArrayList<Piece> pieces3 = b.getPiecesOfTypes(PieceTypes.Color.WHITE, PieceTypes.Type.ROOK);
		assertTrue(pieces2.size() == pieces3.size());
		for(int i = 0; i < pieces2.size(); i++){
			assertTrue(pieces2.get(i).equals(pieces3.get(i)));
		}
	}
	
	@Test
	public void testGetPiecesOfType(){
		ArrayList<Piece> pieces2 = new ArrayList<Piece>();
		pieces2.add(new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 0)));
		ArrayList<Piece> pieces3 = b.getPiecesOfType(PieceTypes.Color.BLACK);
		assertTrue(pieces2.size() == pieces3.size());
		for(int i = 0; i < pieces2.size(); i++){
			assertTrue(pieces2.get(i).equals(pieces3.get(i)));
		}
	}
	
	@Test
	public void testGetPiece(){
		Piece p = new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 0));
		assertTrue(p.equals(b.getPiece(new Cell(0, 0))));
	}
}
