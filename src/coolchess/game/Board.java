package coolchess.game;

import java.util.ArrayList;

public class Board {
	public static final int boardSize = 8;

	private BoardState state;
	private ArrayList<Piece> pieces;
	
	public Board(BoardState state, ArrayList<Piece> pieces){
		this.state = state;
		this.pieces = pieces;
	}
	
	public Board(BoardState state){		
		this(state, null); 
		
		this.pieces = new ArrayList<Piece>();
		
		//white backline
		this.pieces.add(new Rook(PieceType.WHITE_ROOK, 0, new Cell(boardSize-1, 0)));
		this.pieces.add(new Rook(PieceType.WHITE_ROOK, 1, new Cell(boardSize-1, boardSize-1)));
		this.pieces.add(new Knight(PieceType.WHITE_KNIGHT, 2, new Cell(boardSize-1, 1)));
		this.pieces.add(new Knight(PieceType.WHITE_KNIGHT, 3, new Cell(boardSize-1, boardSize-2)));
		this.pieces.add(new Bishop(PieceType.WHITE_BISHOP, 4, new Cell(boardSize-1, 2)));
		this.pieces.add(new Bishop(PieceType.WHITE_BISHOP, 5, new Cell(boardSize-1, boardSize-3)));
		this.pieces.add(new Queen(PieceType.WHITE_QUEEN, 6, new Cell(boardSize-1, 3)));
		this.pieces.add(new King(PieceType.WHITE_KING, 7, new Cell(boardSize-1, 4)));
		
		//black backline
		this.pieces.add(new Rook(PieceType.BLACK_ROOK, 8, new Cell(0, 0)));
		this.pieces.add(new Rook(PieceType.BLACK_ROOK, 9, new Cell(0, boardSize-1)));
		this.pieces.add(new Knight(PieceType.BLACK_KNIGHT, 10, new Cell(0, 1)));
		this.pieces.add(new Knight(PieceType.BLACK_KNIGHT, 11, new Cell(0, boardSize-2)));
		this.pieces.add(new Bishop(PieceType.BLACK_BISHOP, 12, new Cell(0, 2)));
		this.pieces.add(new Bishop(PieceType.BLACK_BISHOP, 13, new Cell(0, boardSize-3)));
		this.pieces.add(new Queen(PieceType.BLACK_QUEEN, 14, new Cell(0, 3)));
		this.pieces.add(new King(PieceType.BLACK_KING, 15, new Cell(0, 4)));
		
		// pawns
		for(int i = 16; i < 24; i++){
			this.pieces.add(new Pawn(PieceType.WHITE_PAWN, i, new Cell(boardSize-2, i-16)));
			this.pieces.add(new Pawn(PieceType.BLACK_PAWN, i+8, new Cell(1, i-16)));
		}
	}
	
	public Board(Board b){
		this(b.getState(), b.getPieces());
	}

	public BoardState getState(){
		return this.state;
	}
	
	public void setBoardState(BoardState state){
		this.state = state;
	}	
	
	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}
	
	// possibly null result
	public Piece getPiece(Cell c){
		for(Piece p : pieces){
			if(p.getLoc().equals(c)){
				return p;
			}
		}
		return null;
	}
}
