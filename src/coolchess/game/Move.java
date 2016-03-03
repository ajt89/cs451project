package coolchess.game;

public class Move {
	private Cell from;
	private Cell to;
	
	public Move(Cell from, Cell to){
		this.from = from;
		this.to = to;
	}

	public Move(Move m){
		this.from = m.getFrom();
		this.to= m.getTo();
	}
	
	public Cell getFrom() {
		return new Cell(from);
	}

	public void setFrom(Cell from) {
		this.from = from;
	}

	public Cell getTo() {
		return new Cell(to);
	}

	public void setTo(Cell to) {
		this.to = to;
	}
}
