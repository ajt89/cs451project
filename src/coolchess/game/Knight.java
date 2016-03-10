package coolchess.game;

public class Knight extends Piece {
	private static final long serialVersionUID = -3665387097756541138L;

	public Knight(PieceType type, int id, Cell loc){
		this.type = type;
		this.id = id;
		this.loc = loc;
	}
	
	public Knight(Knight k){
		this(k.getType(), k.getId(), k.getLoc());
	}
}
