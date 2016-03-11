package coolchess.game;

public class King extends Piece {
	private static final long serialVersionUID = -1477668621166621201L;
	
	private boolean moved;

	public King(PieceTypes.Color color, PieceTypes.Type type, Cell loc, boolean moved){
		this.color = color;
		this.type = type;
		this.loc = loc;
		this.moved = moved;
	}
	
	public King(PieceTypes.Color color, PieceTypes.Type type, Cell loc){
		this(color, type, loc, false);
	}
	
	public King(King k){
		this(k.getColor(), k.getType(), k.getLoc(), k.hasMoved());
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	
	public boolean testCheck(Board b, Cell loc){
		for(Piece p : b.getPiecesOfType(this.color == PieceTypes.Color.BLACK ? PieceTypes.Color.WHITE : PieceTypes.Color.BLACK)){
			switch(p.getType()){
			case BISHOP:    
				for(Cell c : Validator.bishopMoves(b, (Bishop)p, p.getColor() == PieceTypes.Color.BLACK)){
					if(loc.equals(c)){
						return true;
					}
				}
				break;
			case KING:
				// kings can't put other kings in check
				break;
			case KNIGHT:
				for(Cell c : Validator.knightMoves(b, (Knight)p, p.getColor() == PieceTypes.Color.BLACK)){
					if(loc.equals(c)){
						return true;
					}
				}
				break;
			case PAWN:
				for(Cell c : Validator.pawnMoves(b, (Pawn)p, p.getColor() == PieceTypes.Color.BLACK)){
					if(loc.equals(c)){
						return true;
					}
				}
				break;
			case QUEEN:
				for(Cell c : Validator.queenMoves(b, (Queen)p, p.getColor() == PieceTypes.Color.BLACK)){
					if(loc.equals(c)){
						return true;
					}
				}
				break;
			case ROOK:
				for(Cell c : Validator.rookMoves(b, (Rook)p, p.getColor() == PieceTypes.Color.BLACK)){
					if(loc.equals(c)){
						return true;
					}
				}
				break;
			default:
				// nothing
			}
		}
		return false;
	}
	
	public boolean inCheck(Board b){
		return testCheck(b, this.getLoc());
	}
}
