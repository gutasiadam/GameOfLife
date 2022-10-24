package gameOfLife;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Game implements Serializable{
	private static int iteration=0;
	private static int population=0;
	private Grid gameGrid;

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
	
	public boolean validateRule(String input) {
		return(input.matches("B{1}[0-9]+\\/S{1}[0-9]+"));
	}
	
	public void saveGame() {
		System.out.println("Saving");
		try {
		FileOutputStream fout=new FileOutputStream("gameOfLifeGameState.txt");    
		ObjectOutputStream out=new ObjectOutputStream(fout);    
		out.writeObject(gameGrid);    
		out.flush();
		out.close();
		}catch(IOException e) {}   
		
	}
	
	public void loadGame() {
		System.out.println("Loading");
		try {
		FileInputStream fin=new FileInputStream("gameOfLifeGameState.txt");    
		ObjectInputStream in=new ObjectInputStream(fin);    
		gameGrid=(Grid)in.readObject();
		in.close();
		}catch(IOException e) {} catch (ClassNotFoundException e) {
		}   
		
	}
	
	public Grid getGameGrid() {
		return this.gameGrid;
	}
	
	public void increasePopulation() {
		this.population++;
	}
	public void decreasePopulation() {
		this.population--;
	}
}
