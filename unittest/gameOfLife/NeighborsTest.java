package gameOfLife;

import org.junit.Test;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;

public class NeighborsTest {
	
	Grid g;
	@Before
	public void setUp() {
	g = new Grid(3);
	/* =Tesztadat=
	 * 1 O 1
	 * O 1 O
	 * O O O
	 * ==========
	 */
	g.getCellByPos(0, 0).setState(true);
	g.getCellByPos(0, 2).setState(true);
	g.getCellByPos(1, 1).setState(true);
	}

	@Test
	/**
	 * Szomszédságok tesztelése, ha a cellának 8 szomszédja van.
	 */
	public void testNeighBors_full() {

	HashMap<String,Cell> neighBorsData= new HashMap<>(g.getCellNeighbors(1, 1));
	Cell[] expecteds= {
			g.getCellByPos(0, 0),g.getCellByPos(0, 1),g.getCellByPos(0, 2),
			g.getCellByPos(1, 0),g.getCellByPos(1, 2),
			g.getCellByPos(2, 0),g.getCellByPos(2, 1),g.getCellByPos(2, 2)};
	Cell[] results= {
			neighBorsData.get("U1"), neighBorsData.get("U2"), neighBorsData.get("U3"),
			neighBorsData.get("L"), neighBorsData.get("R"),
			neighBorsData.get("D1"), neighBorsData.get("D2"), neighBorsData.get("D3")
	};
	Assert.assertArrayEquals(expecteds, results);
	}
	@Test
	/**
	 * Szomszédságok tesztelése: Aktív szomszédok száának ellenőrzése
	 */
	public void testNeighBors_corner() {
		Assert.assertEquals(2, g.activeCellNeighbors(1, 1));
	}
	@Test
	/**
	 * Cella behelyezése pozíció alapján
	 */
	public void testSetCellByPos() {
		Cell cE=new Cell();
		g.setCellByPos(2, 2, cE);
		Assert.assertEquals(cE, g.getCellByPos(2, 2));
	}
	
}
