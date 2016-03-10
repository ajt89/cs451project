package coolchess.game;

public class Queen extends Piece {
	private static final long serialVersionUID = -6814293853280554409L;

	public Queen(PieceTypes.Color color, PieceTypes.Type type, Cell loc){
		this.color = color;
		this.type = type;
		this.loc = loc;
	}
	
	public Queen(Queen q){
		this(q.getColor(), q.getType(), q.getLoc());
	}
}
