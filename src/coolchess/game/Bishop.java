package coolchess.game;

public class Bishop extends Piece {
	private static final long serialVersionUID = -6814293853280554409L;

	public Bishop(PieceType type, int id, Cell loc){
		this.type = type;
		this.id = id;
		this.loc = loc;
	}
	
	public Bishop(Bishop b){
		this(b.getType(), b.getId(), b.getLoc());
	}
}