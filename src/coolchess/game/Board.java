package coolchess.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
	private static final long serialVersionUID = -4342807129775409263L;

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
		this.pieces.add(new Rook(PieceTypes.Color.WHITE, PieceTypes.Type.ROOK, new Cell(boardSize-1, 0)));
		this.pieces.add(new Rook(PieceTypes.Color.WHITE, PieceTypes.Type.ROOK, new Cell(boardSize-1, boardSize-1)));
		this.pieces.add(new Knight(PieceTypes.Color.WHITE, PieceTypes.Type.KNIGHT, new Cell(boardSize-1, 1)));
		this.pieces.add(new Knight(PieceTypes.Color.WHITE, PieceTypes.Type.KNIGHT, new Cell(boardSize-1, boardSize-2)));
		this.pieces.add(new Bishop(PieceTypes.Color.WHITE, PieceTypes.Type.BISHOP, new Cell(boardSize-1, 2)));
		this.pieces.add(new Bishop(PieceTypes.Color.WHITE, PieceTypes.Type.BISHOP, new Cell(boardSize-1, boardSize-3)));
		this.pieces.add(new Queen(PieceTypes.Color.WHITE, PieceTypes.Type.QUEEN, new Cell(boardSize-1, 3)));
		this.pieces.add(new King(PieceTypes.Color.WHITE, PieceTypes.Type.KING, new Cell(boardSize-1, 4)));
		
		//black backline
		this.pieces.add(new Rook(PieceTypes.Color.BLACK, PieceTypes.Type.ROOK, new Cell(0, 0)));
		this.pieces.add(new Rook(PieceTypes.Color.BLACK, PieceTypes.Type.ROOK, new Cell(0, boardSize-1)));
		this.pieces.add(new Knight(PieceTypes.Color.BLACK, PieceTypes.Type.KNIGHT, new Cell(0, 1)));
		this.pieces.add(new Knight(PieceTypes.Color.BLACK, PieceTypes.Type.KNIGHT, new Cell(0, boardSize-2)));
		this.pieces.add(new Bishop(PieceTypes.Color.BLACK, PieceTypes.Type.BISHOP, new Cell(0, 2)));
		this.pieces.add(new Bishop(PieceTypes.Color.BLACK, PieceTypes.Type.BISHOP, new Cell(0, boardSize-3)));
		this.pieces.add(new Queen(PieceTypes.Color.BLACK, PieceTypes.Type.QUEEN, new Cell(0, 3)));
		this.pieces.add(new King(PieceTypes.Color.BLACK, PieceTypes.Type.KING, new Cell(0, 4)));
		
		// pawns
		for(int i = 16; i < 24; i++){
			this.pieces.add(new Pawn(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN, new Cell(boardSize-2, i-16)));
			this.pieces.add(new Pawn(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN, new Cell(1, i-16)));
		}
	}
	
	public Board(Board b){
		this(b.getBoardState(), b.getPieces());
	}

	public BoardState getBoardState(){
		return this.state;
	}
	
	public void setBoardState(BoardState state){
		this.state = state;
	}	
	
	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	
	public ArrayList<Piece> getPiecesOfTypes(PieceTypes.Color color, PieceTypes.Type type){
		ArrayList<Piece> ret = new ArrayList<Piece>();
		for(Piece p : pieces){
			if(p.getColor() == color && p.getType() == type){
				ret.add(p);
			}
		}
		return ret;
	}
	
	public ArrayList<Piece> getPiecesOfType(PieceTypes.Color color){
		ArrayList<Piece> ret = new ArrayList<Piece>();
		for(Piece p : pieces){
			if(p.getColor() == color){
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
			if(p.getLoc() != null && p.getLoc().equals(c)){
				return p;
			}
		}
		return null;
	}
}
