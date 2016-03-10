package coolchess.game;

public class Rook extends Piece {
	private static final long serialVersionUID = -2663195527492955573L;

	private boolean moved;
	
	public Rook(PieceTypes.Color color, PieceTypes.Type type, Cell loc, boolean moved){
		this.color = color;
		this.type = type;
		this.loc = loc;
		this.moved = moved;
	}
	
	public Rook(PieceTypes.Color color, PieceTypes.Type type, Cell loc){
		this(color, type, loc, false);
	}
	
	public Rook(Rook r){
		this(r.getColor(), r.getType(), r.getLoc(), r.hasMoved());
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
}
