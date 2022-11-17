/**
 * Tesztek a Game osztály Unit tesztjéhez.
 * @author gutasiadam
 */
package gameOfLife;

import org.junit.Test;


import org.junit.Assert;
import org.junit.Before;

public class GameClassTest {
	Game game;
	Grid grid;
	@Before
	public void setUp() {
		grid=new Grid(50);
		game = new Game(grid);
		/*
		 * 					  +
		 * + + + -iteration-> +
		 * 					  +
		 */
		grid.getCellByPos(1, 3).setState(true);
		grid.getCellByPos(1, 2).setState(true);
		grid.getCellByPos(1, 4).setState(true);
		int[] bornRule= {3};
		int[] surviveRule= {2,3};
		game.setbornRule(bornRule);
		game.setsurviveRule(surviveRule);
	}
	@Test
	public void goodRuleValidationTest() {
		String goodRule="B0123456789/S0123456789";
		boolean result=game.validateRule(goodRule);
		Assert.assertEquals(true, result);
	}
	@Test
	public void badRuleValidationTest() {
		String goodRule="1/3456";
		boolean result=game.validateRule(goodRule);
		Assert.assertEquals(false, result);
	}
	@Test
	public void gameIterationTest_CellChange() {
		boolean[] expectedResults= {true,true,true};
		game.nextIteration();
		game.applyIteration();
		boolean[] results= {
				grid.getCellByPos(0, 3).isAlive(),
				grid.getCellByPos(1, 3).isAlive(),
				grid.getCellByPos(2, 3).isAlive()};
		Assert.assertArrayEquals(expectedResults, results);
		}
	@Test
	public void gameIterationTest_PopulationChange() {
		game.nextIteration();
		game.applyIteration();
		int pop=game.getPopulation();
		game.nextIteration();
		game.applyIteration();
		int pop2=game.getPopulation();
		Assert.assertEquals(pop, pop2);
	}
	
	
}
