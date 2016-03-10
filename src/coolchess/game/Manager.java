package coolchess.game;

import java.util.ArrayList;

public class Manager {
	private Board board;
	
	public Manager(BoardState bs){
		this.board = new Board(bs);
	}
	
	public void doMove(Move m){
		
	}
	
	public ArrayList<Cell> viableLocations(Cell c){
		Piece p = board.getPiece(c);
		
		if(p == null){
			return new ArrayList<Cell>();
		}
		else{
			switch(p.type){
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
