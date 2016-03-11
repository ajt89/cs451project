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
			((King)p).setMoved(true);
			
			// castling right / king
			if(p.getLoc().getLet() - m.getCell().getLet() == -2){
				Rook r = (Rook)(board.getPiece(new Cell(p.getLoc().getNum(), 7)));
				r.setMoved(true);
				r.setLoc(new Cell(p.getLoc().getNum(), 5));
			} // castling left / queen
			else if(p.getLoc().getLet() - m.getCell().getLet() == 2){
				Rook r = (Rook)(board.getPiece(new Cell(p.getLoc().getNum(), 0)));
				r.setMoved(true);
				r.setLoc(new Cell(p.getLoc().getNum(), 3));
			}
		}
		else if(p.getType() == PieceTypes.Type.ROOK){
			((Rook)p).setMoved(true);
		}
		else if(p.getType() == PieceTypes.Type.PAWN){
			Pawn pp = (Pawn)p;
			
			if(!pp.hasMoved()){
				pp.setMoved(true);
				pp.setAdvanced(false);
				
				if(Math.abs(pp.getLoc().getNum() - m.getCell().getNum()) == 2){
					pp.setAdvanced(true);
				}
			}
			else{ // en passant
				Cell pc = pp.getLoc();
				Cell mc = m.getCell();
				System.out.println("piece length before possible enpassant deletion " + board.getPieces().size());
				if(board.getPiece(mc) == null && Math.abs(pc.getNum() - mc.getNum()) == 1 && Math.abs(pc.getLet() - mc.getLet()) == 1){
					board.getPieces().remove(board.getPiece(new Cell(pc.getNum(), 2 * pc.getLet() - mc.getLet())));
				}
				System.out.println("piece length after possible enpassant deletion " + board.getPieces().size());
			}
		}
		// most pieces don't require special treatment
		p.setLoc(m.getCell());

		// switch turn - also needs to check if the game is over or not
		// turn off possibility for en passant after you finish your turn
		if(board.getBoardState() == BoardState.BLACK_TURN){
			board.setBoardState(BoardState.WHITE_TURN);
			
			for(Piece pp : board.getPiecesOfTypes(PieceTypes.Color.WHITE, PieceTypes.Type.PAWN)){
				Pawn ppp = (Pawn)pp;
				ppp.setAdvanced(false);
			}
		}
		else{
			board.setBoardState(BoardState.BLACK_TURN);
			for(Piece pp : board.getPiecesOfTypes(PieceTypes.Color.BLACK, PieceTypes.Type.PAWN)){
				Pawn ppp = (Pawn)pp;
				ppp.setAdvanced(false);
			}
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
				return Validator.removeChecks(board, p, Validator.bishopMoves(board, (Bishop)p));
			case KING:
				return Validator.removeChecks(board, p, Validator.kingMoves(board, (King)p));
			case KNIGHT:
				return Validator.removeChecks(board, p, Validator.knightMoves(board, (Knight)p));
			case PAWN:
				return Validator.removeChecks(board, p, Validator.pawnMoves(board, (Pawn)p));
			case QUEEN:
				return Validator.removeChecks(board, p, Validator.queenMoves(board, (Queen)p));
			case ROOK:
				return Validator.removeChecks(board, p, Validator.rookMoves(board, (Rook)p));
			default:
				return null;
			}
		}
	}
}
