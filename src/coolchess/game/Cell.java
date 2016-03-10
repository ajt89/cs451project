package coolchess.game;

import java.io.Serializable;

public class Cell implements Serializable{
	private static final long serialVersionUID = -1445032280984057587L;

	private int num;
	private int let;
	
	public Cell(int num, int let){
		// num => number / r, let => letter / c
		this.setNum(num);
		this.setLet(let);
	}
	
	public Cell(Cell c){
		this(c.getNum(), c.getLet());
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getLet() {
		return let;
	}

	public void setLet(int let) {
		this.let = let;
	}
	
	public boolean equals(Cell c){
		return this.num == c.getNum() && this.let == c.getLet();
	}
}
