package coolchess.game;

import java.util.ArrayList;

public class Manager {
	private Board board;
	
	public Manager(BoardState bs){
		this.board = new Board(bs);
	}
	
	public void doMove(Move m){
		
	}
	
	public Board getBoard(){
		return board;
	}
	
	public ArrayList<Piece> getViablePieces(){
		if(board.getState() == BoardState.BLACK_TURN){
			ArrayList<Piece> ret = new ArrayList<Piece>();
			for(Piece p : board.getPieces()){
				if(p.getColor() == PieceTypes.Color.BLACK){
					ret.add(p);
				}
			}
			return ret;
		}
		else if(board.getState() == BoardState.WHITE_TURN){
			ArrayList<Piece> ret = new ArrayList<Piece>();
			for(Piece p : board.getPieces()){
				if(p.getColor() == PieceTypes.Color.WHITE){
					ret.add(p);
				}
			}
			return ret;
		}
		else{
			return new ArrayList<Piece>();
		}
	}
	
	public ArrayList<Cell> viableLocations(int num, int let){
		Piece p = board.getPiece(new Cell(num, let));
		
		if(p == null){
			return new ArrayList<Cell>();
		}
		else{
			switch(p.getType()){
			case BISHOP:    
				return Validator.bishopMoves(board, p, p.getColor() == PieceTypes.Color.BLACK);
			case KING:
				return Validator.kingMoves(board, p, p.getColor() == PieceTypes.Color.BLACK);
			case KNIGHT:
				return Validator.knightMoves(board, p, p.getColor() == PieceTypes.Color.BLACK);
			case PAWN:
				return Validator.pawnMoves(board, p, p.getColor() == PieceTypes.Color.BLACK);
			case QUEEN:
				return Validator.queenMoves(board, p, p.getColor() == PieceTypes.Color.BLACK);
			case ROOK:
				return Validator.rookMoves(board, p, p.getColor() == PieceTypes.Color.BLACK);
			default:
				return null;
			}
		}
	}
}
