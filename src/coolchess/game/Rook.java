package coolchess.game;

public class Rook extends Piece {
	private static final long serialVersionUID = -2663195527492955573L;

	private boolean moved;
	
	public Rook(PieceType type, int id, Cell loc, boolean moved){
		this.type = type;
		this.id = id;
		this.loc = loc;
		this.moved = moved;
	}
	
	public Rook(PieceType type, int id, Cell loc){
		this(type, id, loc, false);
	}
	
	public Rook(Rook r){
		this(r.getType(), r.getId(), r.getLoc(), r.hasMoved());
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
}
