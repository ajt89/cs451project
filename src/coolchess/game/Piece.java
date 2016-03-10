package coolchess.game;

import java.io.Serializable;

public abstract class Piece implements Serializable {	
	private static final long serialVersionUID = 2509041690427883293L;
	
	protected PieceTypes.Color color;
	protected PieceTypes.Type type;
	protected Cell loc;

	public PieceTypes.Color getColor() {
		return color;
	}

	public void setColor(PieceTypes.Color color) {
		this.color = color;
	}
	
	public PieceTypes.Type getType() {
		return type;
	}

	public void setType(PieceTypes.Type type) {
		this.type = type;
	}

	public Cell getLoc() {
		return loc;
	}

	public void setLoc(Cell loc) {
		this.loc = loc;
	}
	
	public boolean equals(Piece p){
		return this.color == p.getColor() && this.type == p.getType() && this.loc.equals(p.getLoc());
	}
}
