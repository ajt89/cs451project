package coolchess.game;

import java.util.ArrayList;

public class Manager {
	private Board board;
	
	public Manager(BoardState bs){
		this.board = new Board(bs);
	}
	
	public void doMove(Move m){
		
	}
	
	public ArrayList<Piece> getViablePieces(){
		if(board.getState() == BoardState.BLACK_TURN){
			ArrayList<Piece> ret = new ArrayList<Piece>();
			for(Piece p : board.getPieces()){
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
		else if(board.getState() == BoardState.WHITE_TURN){
			ArrayList<Piece> ret = new ArrayList<Piece>();
			for(Piece p : board.getPieces()){
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
	
	public ArrayList<Cell> viableLocations(int num, int let){
		Piece p = board.getPiece(new Cell(num, let));
		
		if(p == null){
			return new ArrayList<Cell>();
		}
		else{
			switch(p.getType()){
			case BLACK_BISHOP:    
				return Validator.bishopMoves(board, p, true);
			case BLACK_KING:
				return Validator.kingMoves(board, p, true);
			case BLACK_KNIGHT:
				return Validator.knightMoves(board, p, true);
			case BLACK_PAWN:
				return Validator.pawnMoves(board, p, true);
			case BLACK_QUEEN:
				return Validator.queenMoves(board, p, true);
			case BLACK_ROOK:
				return Validator.rookMoves(board, p, true);
			case WHITE_BISHOP:
				return Validator.bishopMoves(board, p, false);
			case WHITE_KING:
				return Validator.kingMoves(board, p, false);
			case WHITE_KNIGHT:
				return Validator.knightMoves(board, p, false);
			case WHITE_PAWN:
				return Validator.pawnMoves(board, p, false);
			case WHITE_QUEEN:
				return Validator.queenMoves(board, p, false);
			case WHITE_ROOK:
				return Validator.rookMoves(board, p, false);
			default:
				return null;
			}
		}
	}
}
