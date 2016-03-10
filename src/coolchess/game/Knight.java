package coolchess.game;

public class Knight extends Piece {
	private static final long serialVersionUID = -3665387097756541138L;

	public Knight(PieceTypes.Color color, PieceTypes.Type type, Cell loc){
		this.color = color;
		this.type = type;
		this.loc = loc;
	}
	
	public Knight(Knight k){
		this(k.getColor(), k.getType(), k.getLoc());
	}
}
