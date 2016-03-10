package coolchess.game;

public class Queen extends Piece {
	private static final long serialVersionUID = -6814293853280554409L;

	public Queen(PieceType type, int id, Cell loc){
		this.type = type;
		this.id = id;
		this.loc = loc;
	}
	
	public Queen(Queen q){
		this(q.getType(), q.getId(), q.getLoc());
	}
}
