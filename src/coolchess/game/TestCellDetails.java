package coolchess.game;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCellDetails {
	
	private Cell c;

	@Test
	public void test() {
		c = new Cell(0, 5);
		assertTrue(0 == c.getNum());
		assertTrue(5 == c.getLet());
		assertTrue(("(0, 5)").equals(c.toString()));
		assertTrue(c.equals(new Cell(0, 5)));
		assertFalse(2 == c.getNum());
		assertFalse(3 == c.getLet());
		assertFalse(("(2, 3)").equals(c.toString()));
		assertFalse(c.equals(new Cell(2, 3)));
	}
}
