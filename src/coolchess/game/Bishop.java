package coolchess.game;

public class Bishop extends Piece {
	private static final long serialVersionUID = -6814293853280554409L;

	public Bishop(PieceTypes.Color color, PieceTypes.Type type, Cell loc){
		this.color = color;
		this.type = type;
		this.loc = loc;
	}
	
	public Bishop(Bishop b){
		this(b.getColor(), b.getType(), b.getLoc());
	}
}
