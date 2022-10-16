package gameOfLife;

import java.io.Serializable;

public class Game implements Serializable{
	private static int iteration=0;
	private static int population=0;
	private final Grid gameGrid;

	private Game(Grid grid) {
		this.gameGrid = grid;
	}
	
	public Game() {
		this.gameGrid=new Grid(100);
		System.out.println("Game Init!");
	}
	
	public void nextIteration() {
		this.iteration++;
		// somebody simulates next iteration and sets cell flags accordingly.
	}
	
	public void renderIteration() {
		//renders iteration that was set in nextIteration() method.
	}
	
	public void saveGame() {
		
	}
}
