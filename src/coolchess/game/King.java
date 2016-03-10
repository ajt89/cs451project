package coolchess.game;

public class King extends Piece {
	private static final long serialVersionUID = -1477668621166621201L;
	
	private boolean moved;
	private boolean check;

	public King(PieceType type, Cell loc, boolean moved, boolean check){
		this.type = type;
		this.loc = loc;
		this.moved = moved;
		this.check = check;
	}
	
	public King(PieceType type, Cell loc){
		this(type, loc, false, false);
	}
	
	public King(King k){
		this(k.getType(), k.getLoc(), k.hasMoved(), k.isInCheck());
	}

	public boolean isInCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
}
