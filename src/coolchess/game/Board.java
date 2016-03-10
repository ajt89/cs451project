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
		this.pieces.add(new Rook(PieceType.WHITE_ROOK, new Cell(boardSize-1, 0)));
		this.pieces.add(new Rook(PieceType.WHITE_ROOK, new Cell(boardSize-1, boardSize-1)));
		this.pieces.add(new Knight(PieceType.WHITE_KNIGHT, new Cell(boardSize-1, 1)));
		this.pieces.add(new Knight(PieceType.WHITE_KNIGHT, new Cell(boardSize-1, boardSize-2)));
		this.pieces.add(new Bishop(PieceType.WHITE_BISHOP, new Cell(boardSize-1, 2)));
		this.pieces.add(new Bishop(PieceType.WHITE_BISHOP, new Cell(boardSize-1, boardSize-3)));
		this.pieces.add(new Queen(PieceType.WHITE_QUEEN, new Cell(boardSize-1, 3)));
		this.pieces.add(new King(PieceType.WHITE_KING, new Cell(boardSize-1, 4)));
		
		//black backline
		this.pieces.add(new Rook(PieceType.BLACK_ROOK, new Cell(0, 0)));
		this.pieces.add(new Rook(PieceType.BLACK_ROOK, new Cell(0, boardSize-1)));
		this.pieces.add(new Knight(PieceType.BLACK_KNIGHT, new Cell(0, 1)));
		this.pieces.add(new Knight(PieceType.BLACK_KNIGHT, new Cell(0, boardSize-2)));
		this.pieces.add(new Bishop(PieceType.BLACK_BISHOP, new Cell(0, 2)));
		this.pieces.add(new Bishop(PieceType.BLACK_BISHOP, new Cell(0, boardSize-3)));
		this.pieces.add(new Queen(PieceType.BLACK_QUEEN, new Cell(0, 3)));
		this.pieces.add(new King(PieceType.BLACK_KING, new Cell(0, 4)));
		
		// pawns
		for(int i = 16; i < 24; i++){
			this.pieces.add(new Pawn(PieceType.WHITE_PAWN, new Cell(boardSize-2, i-16)));
			this.pieces.add(new Pawn(PieceType.BLACK_PAWN, new Cell(1, i-16)));
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
	
	public ArrayList<Piece> getViablePieces(){
		if(state == BoardState.BLACK_TURN){
			ArrayList<Piece> ret = new ArrayList<Piece>();
			for(Piece p : pieces){
				switch(p.getType()){
				case BLACK_BISHOP:
				case BLACK_KING:
				case BLACK_KNIGHT:
				case BLACK_PAWN:
				case BLACK_QUEEN:
				case BLACK_ROOK:
					ret.add(p);
					break;
				default:
					break;
				}
			}
			return ret;
		}
		else if(state == BoardState.WHITE_TURN){
			ArrayList<Piece> ret = new ArrayList<Piece>();
			for(Piece p : pieces){
				switch(p.getType()){
				case WHITE_BISHOP:
				case WHITE_KING:
				case WHITE_KNIGHT:
				case WHITE_PAWN:
				case WHITE_QUEEN:
				case WHITE_ROOK:
					ret.add(p);
					break;
				default:
					break;
				}
			}
			return ret;
		}
		else{
			return new ArrayList<Piece>();
		}
	}
	
	public ArrayList<Piece> getPiecesOfType(PieceType pt){
		ArrayList<Piece> ret = new ArrayList<Piece>();
		for(Piece p : pieces){
			if(p.getType() == pt){
				ret.add(p);
			}
		}
		return ret;
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
