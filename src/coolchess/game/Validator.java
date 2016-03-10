package coolchess.game;

import java.util.ArrayList;
import java.util.Iterator;

public class Validator {
	private static void removeSameSide(Board board, ArrayList<Cell> ret, boolean isBlack){
		for(Iterator<Cell> iter = ret.iterator(); iter.hasNext();){
			Piece pp = board.getPiece(iter.next());
			if(pp != null){
				if(isBlack){
					switch(pp.getType()){
					case BLACK_BISHOP:
					case BLACK_KING:
					case BLACK_KNIGHT:
					case BLACK_PAWN:
					case BLACK_QUEEN:
					case BLACK_ROOK:
						iter.remove();
						break;
					default:
						break;
					}
				}
				else{
					switch(pp.getType()){
					case WHITE_BISHOP:
					case WHITE_KING:
					case WHITE_KNIGHT:
					case WHITE_PAWN:
					case WHITE_QUEEN:
					case WHITE_ROOK:
						iter.remove();
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	public static ArrayList<Cell> bishopMoves(Board board, Piece p, boolean isBlack) {
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
		
		removeSameSide(board, ret, isBlack);
		
		return ret;
	}

	public static ArrayList<Cell> kingMoves(Board board, Piece p, boolean isBlack) {
		return new ArrayList<Cell>();
	}

	public static ArrayList<Cell> knightMoves(Board board, Piece p, boolean isBlack) {
		return new ArrayList<Cell>();
	}

	public static ArrayList<Cell> pawnMoves(Board board, Piece p, boolean isBlack) {
		return new ArrayList<Cell>();
	}

	public static ArrayList<Cell> queenMoves(Board board, Piece p, boolean isBlack) {
		ArrayList<Cell> ret = new ArrayList<Cell>();
		
		int n = p.getLoc().getNum();
		int l = p.getLoc().getLet();
		for(int i = 1; n + i < Board.boardSize && l + i < Board.boardSize; i++){
				ret.add(new Cell(n + i, l + i));
		}
		for(int i = 1; n + i < Board.boardSize && l - i >= 0; i++){
			ret.add(new Cell(n + i, l - i));
		}
		for(int i = 1; n - i >= 0 && l + i < Board.boardSize; i++){
			ret.add(new Cell(n - i, l + i));
		}
		for(int i = 1; n - i >= 0 && l - i >= 0; i++){
			ret.add(new Cell(n - i, l - i));
		}
		
		removeSameSide(board, ret, isBlack);
		
		return ret;
	}

	public static ArrayList<Cell> rookMoves(Board board, Piece p, boolean isBlack) {
		return new ArrayList<Cell>();
	}
	
	public static void main(String[] args){
		Board b = new Board(BoardState.BLACK_TURN);
		Piece p = b.getPiecesOfType(PieceType.BLACK_BISHOP).get(0);
		System.out.println(bishopMoves(b, p, true).size());
		Piece p2 = new Bishop(PieceType.BLACK_BISHOP, new Cell(3, 3));
		b.getPieces().add(p2);
		System.out.println(bishopMoves(b, p2, true).size());
	}
}
