package coolchess.game;

public class Pawn extends Piece {
	private static final long serialVersionUID = -7195066257305237501L;
	
	private boolean moved;
	private boolean advanced;
	
	public Pawn(PieceTypes.Color color, PieceTypes.Type type, Cell loc, boolean moved, boolean advanced){
		this.color = color;
		this.type = type;
		this.loc = loc;
		this.moved = moved;
		this.advanced = advanced;
	}
	
	public Pawn(PieceTypes.Color color, PieceTypes.Type type, Cell loc){
		this(color, type, loc, false, false);
	}
	
	public Pawn(Pawn p){
		this(p.getColor(), p.getType(), p.getLoc(), p.hasMoved(), p.hasAdvanced());
	}

	public boolean hasAdvanced() {
		return advanced;
	}

	public void setAdvanced(boolean advanced) {
		this.advanced = advanced;
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
}
