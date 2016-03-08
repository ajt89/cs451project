package coolchess.game;

import java.io.Serializable;

public class Move implements Serializable {
	private static final long serialVersionUID = 6068364453892314431L;
	
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
