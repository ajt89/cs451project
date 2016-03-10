package coolchess.game;

public class Queen extends Piece {
	private static final long serialVersionUID = -6814293853280554409L;

	public Queen(PieceType type, Cell loc){
		this.type = type;
		this.loc = loc;
	}
	
	public Queen(Queen q){
		this(q.getType(), q.getLoc());
	}
}
