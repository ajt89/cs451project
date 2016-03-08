package coolchess.game;

public class Board {
	public static final int boardSize = 8;
	private Cell[][] cells;
	private BoardState state;
	
	public Board(BoardState state){
		this.state = state;
		
		cells = new Cell[boardSize][boardSize];
		
		CellState[] twoWhite = {CellState.WHITE_ROOK, CellState.WHITE_KNIGHT, CellState.WHITE_BISHOP};
		for(int c = 0; c < 3; c++){
			cells[0][c] = new Cell(0, c, twoWhite[c]);
			cells[0][boardSize-1-c] = new Cell(0, boardSize-1-c, twoWhite[c]);
		}
		
		for(int c = 0; c < boardSize; c++){
			cells[1][c] = new Cell(1, c, CellState.WHITE_PAWN);
		}
		
		cells[0][3] = new Cell(0, 3, CellState.WHITE_QUEEN);
		cells[0][4] = new Cell(0, 4, CellState.WHITE_KING);
		
		CellState[] twoBlack = {CellState.BLACK_ROOK, CellState.BLACK_KNIGHT, CellState.BLACK_BISHOP};
		for(int c = 0; c < 3; c++){
			cells[boardSize-1][c] = new Cell(boardSize-1, c, twoBlack[c]);
			cells[boardSize-1][boardSize-1-c] = new Cell(boardSize-1, boardSize-1-c, twoBlack[c]);
		}
		
		for(int c = 0; c < boardSize; c++){
			cells[boardSize-2][c] = new Cell(boardSize-2, c, CellState.BLACK_PAWN);
		}
		
		cells[boardSize-1][3] = new Cell(boardSize-1, 3, CellState.BLACK_QUEEN);
		cells[boardSize-1][4] = new Cell(boardSize-1, 4, CellState.BLACK_KING);
		
		for(int r = 2; r < boardSize-2; r++){
			for(int c = 0; c < boardSize; c++){
				cells[r][c] = new Cell(r, c, CellState.EMPTY);
			}
		}
	}
	
	public Board(Board b){
		this.cells = b.getCells();
		this.state = b.getState();
	}
	
	public Cell[][] getCells(){
		Cell[][] ret = new Cell[boardSize][boardSize];
		for(int r = 0; r < boardSize; r++){
			for(int c = 0; c < boardSize; c++){
				ret[r][c] = new Cell(cells[r][c]);
			}
		}
		
		return ret;
	}
	
	public void setCells(Cell[][] cells){
		this.cells = cells;
	}
	
	public BoardState getState(){
		return this.state;
	}
	
	public void setBoardState(BoardState state){
		this.state = state;
	}
	
	public void printBoard(){
		for(int r = boardSize-1; r >= 0; r--){
			for(int c = 0; c < boardSize; c++){
				System.out.print(this.cells[r][c].toIcon() + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args){
		Board b = new Board(BoardState.WHITE_TURN);
		b.printBoard();
	}
}
