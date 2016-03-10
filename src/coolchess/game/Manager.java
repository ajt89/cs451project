package coolchess.game;

import java.util.ArrayList;
import java.util.Iterator;

public class Manager {
	private Board board;
	
	public Manager(BoardState bs){
		this.board = new Board(bs);
	}
	
	public void doMove(Move m){
		for(Cell c : m.getCells()){
			Piece p = board.getPiece(c);
			if(p != null){
				p.setLoc(null);
			}
		}
		for(Iterator<Piece> iter = board.getPieces().iterator(); iter.hasNext();){
			if(iter.next().getLoc() == null){
				iter.remove();
			}
		}
		for(int i = 0; i < m.getPieces().size(); i++){
			m.getPieces().get(i).setLoc(m.getCells().get(i));
		}
		
		if(board.getBoardState() == BoardState.BLACK_TURN){
			board.setBoardState(BoardState.WHITE_TURN);
		}
		else{
			board.setBoardState(BoardState.BLACK_TURN);
		}
	}
	
	public Board getBoard(){
		return board;
	}
	
	public ArrayList<Piece> getViablePieces(){
		if(board.getBoardState() == BoardState.BLACK_TURN){
			return board.getPiecesOfType(PieceTypes.Color.BLACK);
		}
		else if(board.getBoardState() == BoardState.WHITE_TURN){
			return board.getPiecesOfType(PieceTypes.Color.WHITE);
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
				return Validator.bishopMoves(board, (Bishop)p, p.getColor() == PieceTypes.Color.BLACK);
			case KING:
				return Validator.kingMoves(board, (King)p, p.getColor() == PieceTypes.Color.BLACK);
			case KNIGHT:
				return Validator.knightMoves(board, (Knight)p, p.getColor() == PieceTypes.Color.BLACK);
			case PAWN:
				return Validator.pawnMoves(board, (Pawn)p, p.getColor() == PieceTypes.Color.BLACK);
			case QUEEN:
				return Validator.queenMoves(board, (Queen)p, p.getColor() == PieceTypes.Color.BLACK);
			case ROOK:
				return Validator.rookMoves(board, (Rook)p, p.getColor() == PieceTypes.Color.BLACK);
			default:
				return null;
			}
		}
	}
}
