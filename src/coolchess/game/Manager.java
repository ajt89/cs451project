package coolchess.game;

import java.util.ArrayList;
import java.util.Iterator;

public class Manager {
	private Board board;
	
	public Manager(BoardState bs){
		this.board = new Board(bs);
	}
	
	public void doMove(Move m){
		// get rid of taken pieces
		Piece p = board.getPiece(m.getCell());
		if(p != null){
			p.setLoc(null);
		}
		
		for(Iterator<Piece> iter = board.getPieces().iterator(); iter.hasNext();){
			if(iter.next().getLoc() == null){
				iter.remove();
			}
		}
		
		// actually move piece
		p = m.getPiece();
		if(p.getType() == PieceTypes.Type.KING){
			// castling right / king
			if(p.getLoc().getLet() - m.getCell().getLet() == -2){
				board.getPiece(new Cell(p.getLoc().getNum(), 7)).setLoc(new Cell(p.getLoc().getNum(), 5));
			} // castling left / queen
			else if(p.getLoc().getLet() - m.getCell().getLet() == 2){
				board.getPiece(new Cell(p.getLoc().getNum(), 0)).setLoc(new Cell(p.getLoc().getNum(), 3));
			}
		}
		// most pieces don't require special treatment
		p.setLoc(m.getCell());

		// switch turn - also needs to check if the game is over or not
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
				return Validator.bishopMoves(board, (Bishop)p);
			case KING:
				return Validator.kingMoves(board, (King)p);
			case KNIGHT:
				return Validator.knightMoves(board, (Knight)p);
			case PAWN:
				return Validator.pawnMoves(board, (Pawn)p);
			case QUEEN:
				return Validator.queenMoves(board, (Queen)p);
			case ROOK:
				return Validator.rookMoves(board, (Rook)p);
			default:
				return null;
			}
		}
	}
}
