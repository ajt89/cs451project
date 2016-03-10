package coolchess.game;

import java.io.Serializable;

public abstract class Piece implements Serializable {	
	private static final long serialVersionUID = 2509041690427883293L;
	
	protected PieceType type;
	protected int id;
	protected Cell loc;

	public PieceType getType() {
		return type;
	}

	public void setType(PieceType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cell getLoc() {
		return loc;
	}

	public void setLoc(Cell loc) {
		this.loc = loc;
	}
	
	public boolean onBoard(){
		return loc != null;
	}
}
