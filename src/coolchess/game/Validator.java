package coolchess.game;

import java.util.ArrayList;
import java.util.Iterator;

public class Validator {
	private static void removeSameSide(Board board, Piece p, ArrayList<Cell> ret){
		for(Iterator<Cell> iter = ret.iterator(); iter.hasNext();){
			Piece pp = board.getPiece(iter.next());
			if(pp != null){
				if(p.getColor() == PieceTypes.Color.BLACK){
					if(pp.getColor() == PieceTypes.Color.BLACK){
						iter.remove();
					}
				}
				else{
					if(pp.getColor() == PieceTypes.Color.WHITE){
						iter.remove();
					}
				}
			}
		}
	}
	
	private static void removeChecks(Board board, Piece p, ArrayList<Cell> ret){
		Cell start = p.getLoc();
		for(Iterator<Cell> iter = ret.iterator(); iter.hasNext();){
			p.setLoc(iter.next());
			// 0 alright here since there's only 1 king per side ever
			if(((King)(board.getPiecesOfTypes(p.getColor(), PieceTypes.Type.KING).get(0))).inCheck(board)){
				iter.remove();
			}
		}
		p.setLoc(start);
	}
	
	public static ArrayList<Cell> bishopMoves(Board board, Bishop p) {
		ArrayList<Cell> ret = new ArrayList<Cell>();
		
		int n = p.getLoc().getNum();
		int l = p.getLoc().getLet();
		for(int i = 1; n + i < Board.boardSize && l + i < Board.boardSize; i++){
			Cell c = new Cell(n + i, l + i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; n + i < Board.boardSize && l - i >= 0; i++){
			Cell c = new Cell(n + i, l - i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; n - i >= 0 && l + i < Board.boardSize; i++){
			Cell c = new Cell(n - i, l + i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; n - i >= 0 && l - i >= 0; i++){
			Cell c = new Cell(n - i, l - i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		
		removeSameSide(board, p, ret);
		removeChecks(board, p, ret);
		
		return ret;
	}

	public static ArrayList<Cell> kingMoves(Board board, King p) {
		ArrayList<Cell> ret = new ArrayList<Cell>();
		
		int n = p.getLoc().getNum();
		int l = p.getLoc().getLet();
		if(n + 1 < Board.boardSize && l + 1 < Board.boardSize){
			ret.add(new Cell(n + 1, l + 1));
		}
		if(n + 1 < Board.boardSize && l - 1 >= 0){
			ret.add(new Cell(n + 1, l - 1));
		}
		if(n - 1 >= 0 && l + 1 < Board.boardSize){
			ret.add(new Cell(n - 1, l + 1));
		}
		if(n - 1 >= 0 && l - 1 >= 0){
			ret.add(new Cell(n - 1, l - 1));
		}
		if(n + 1 < Board.boardSize){
			ret.add(new Cell(n + 1, l));
		}
		if(n - 1 >= 0){
			ret.add(new Cell(n - 1, l));
		}
		if(l + 1 < Board.boardSize){
			ret.add(new Cell(n, l + 1));
		}
		if(l - 1 >= 0){
			ret.add(new Cell(n, l - 1));
		}
		
		//castling
		if(!p.hasMoved() && !p.inCheck(board)){
			for(Piece r : board.getPiecesOfTypes(p.getColor(), PieceTypes.Type.ROOK)){
				Rook rr = (Rook) r;
				if(!rr.hasMoved()){
					boolean occupied = false;
					for(int i = 1 + Math.min(p.getLoc().getLet(), rr.getLoc().getLet()); i < Math.max(p.getLoc().getLet(), rr.getLoc().getLet()); i++){
						if(board.getPiece(new Cell(p.getLoc().getNum(), i)) != null){
							occupied = true;
							break;
						}
					}
					if(!occupied){
						if(rr.getLoc().getLet() == Math.min(p.getLoc().getLet(), rr.getLoc().getLet())){
							Cell left = new Cell(p.getLoc().getNum(), p.getLoc().getLet()-2);
							if(!p.testCheck(board, left)){
								ret.add(left);
							}
						}
						else{
							Cell right = new Cell(p.getLoc().getNum(), p.getLoc().getLet()+2);
							if(!p.testCheck(board, right)){
								ret.add(right);
							}
						}
					}
				}
			}
		}
		
		removeSameSide(board, p, ret);
		removeChecks(board, p, ret);
		
		return ret;
	}

	public static ArrayList<Cell> knightMoves(Board board, Knight p) {
		ArrayList<Cell> ret = new ArrayList<Cell>();
		
		int n = p.getLoc().getNum();
		int l = p.getLoc().getLet();
		for(int r = -1; r <= 1; r+=2){
			for(int c = -1; c <= 1; c+=2){
				if(n + 1*r >= 0 && l + 2*c >= 0 && n + 1*r < Board.boardSize && l + 2*c < Board.boardSize){
					ret.add(new Cell(n + 1*r, l + 2*c));
				}
				if(n + 2*r >= 0 && l + 1*c >= 0 && n + 2*r < Board.boardSize && l + 1*c < Board.boardSize){
					ret.add(new Cell(n + 2*r, l + 1*c));
				}
			}
		}
		
		removeSameSide(board, p, ret);
		removeChecks(board, p, ret);
		
		return ret;
	}

	public static ArrayList<Cell> pawnMoves(Board board, Pawn p) {
		ArrayList<Cell> ret = new ArrayList<Cell>();
		
		int n = p.getLoc().getNum();
		int l = p.getLoc().getLet();
		if(p.getColor() == PieceTypes.Color.BLACK){
			if(n + 1 < Board.boardSize){
				ret.add(new Cell(n + 1, l));
				if(l + 1 < Board.boardSize){
					Cell c = new Cell(n + 1, l + 1);
					if(board.getPiece(c) != null){
						ret.add(c);
					}
				}
				if(l - 1 >= 0){
					Cell c = new Cell(n + 1, l - 1);
					if(board.getPiece(c) != null){
						ret.add(c);
					}
				}
			}
		}
		else{
			if(n - 1 >= 0){
				ret.add(new Cell(n - 1, l));
				if(l + 1 < Board.boardSize){
					Cell c = new Cell(n - 1, l + 1);
					if(board.getPiece(c) != null){
						ret.add(c);
					}
				}
				if(l - 1 >= 0){
					Cell c = new Cell(n - 1, l - 1);
					if(board.getPiece(c) != null){
						ret.add(c);
					}
				}
			}
		}
		
		removeSameSide(board, p, ret);
		removeChecks(board, p, ret);
		
		return ret;
	}

	public static ArrayList<Cell> queenMoves(Board board, Queen p) {
		ArrayList<Cell> ret = new ArrayList<Cell>();
		
		int n = p.getLoc().getNum();
		int l = p.getLoc().getLet();
		for(int i = 1; n + i < Board.boardSize && l + i < Board.boardSize; i++){
			Cell c = new Cell(n + i, l + i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; n + i < Board.boardSize && l - i >= 0; i++){
			Cell c = new Cell(n + i, l - i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; n - i >= 0 && l + i < Board.boardSize; i++){
			Cell c = new Cell(n - i, l + i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; n - i >= 0 && l - i >= 0; i++){
			Cell c = new Cell(n - i, l - i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; n + i < Board.boardSize; i++){
			Cell c = new Cell(n + i, l);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; n - i >= 0; i++){
			Cell c = new Cell(n - i, l);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; l + i < Board.boardSize; i++){
			Cell c = new Cell(n, l + i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; l - i >= 0; i++){
			Cell c = new Cell(n, l - i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		
		removeSameSide(board, p, ret);
		removeChecks(board, p, ret);
		
		return ret;
	}

	public static ArrayList<Cell> rookMoves(Board board, Rook p) {
		ArrayList<Cell> ret = new ArrayList<Cell>();
		
		int n = p.getLoc().getNum();
		int l = p.getLoc().getLet();

		for(int i = 1; n + i < Board.boardSize; i++){
			Cell c = new Cell(n + i, l);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; n - i >= 0; i++){
			Cell c = new Cell(n - i, l);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; l + i < Board.boardSize; i++){
			Cell c = new Cell(n, l + i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		for(int i = 1; l - i >= 0; i++){
			Cell c = new Cell(n, l - i);
			ret.add(c);
			if(board.getPiece(c) != null){
				break;
			}
		}
		
		removeSameSide(board, p, ret);
		removeChecks(board, p, ret);
		
		return ret;
	}
}
