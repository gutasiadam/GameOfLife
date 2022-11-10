package gameOfLife;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable{
	private static int iteration=0;
	private static int population=0;
	private static int[] bornRule;
	private static int[] surviveRule;
	private static Grid gameGrid;
	
	/*private void autorun() {
		for(int i=0;)
	}*/

	private Game(Grid grid) {
		this.gameGrid = grid;
	}
	
	public Game() {
		this.gameGrid=new Grid(50);
		System.out.println("Game Init!");
	}
	
	/**
	 * Következő iteráció szimulálása. Átállítja a cellák nextIteration értékeit.
	 */
	public void nextIteration() {
		this.population=0;
		System.out.println("Calculating iteration.");
		this.iteration++;
		for(int i=0;i<50;i++) {
			for(int j=0;j<50;j++) {
				
				Cell c = gameGrid.getCellByPos(i, j);
				c.setNextiteration(false);
				int activeNeighborsCount=gameGrid.activeCellNeighbors(i, j);
				
				//System.out.print("("+i+","+j+") - "+activeNeighborsCount);
				
				//Ha a cella nem él, akkor a B... szabály alapján kell vizsgálnunk a szomszédokat.
				if(c.isAlive()==false) {
					for(int k=0;k<bornRule.length;k++) {
						if(bornRule[k]==activeNeighborsCount) {
							
							c.setNextiteration(true);
							break;
						}
					}
				}else {
					this.population++;
					//Ha a cella él, akkor a S... szabály alapján kell vizsgálnunk a szomszádokat.
					for(int k=0;k<surviveRule.length;k++) {
						if(surviveRule[k]==activeNeighborsCount) {
							
							c.setNextiteration(true);
							break;
						}
					}
				}

			}
			//System.out.print("\n");
		}
	}
	
	/**
	 * Cellák frissítése. nextIteration értékek átmásolása a jelenlegi iterációjéba. nextItertation értékek alaphelyzetbe állítása.
	 */
	public void applyIteration() {
		for(int i=0;i<50;i++) {
			for(int j=0;j<50;j++) {
				Cell c = gameGrid.getCellByPos(i, j);
				c.Step();
			}
		}
	}
	
	public boolean validateRule(String input) {
		return(input.matches("B{1}[1-9]+\\/S{1}[1-9]+"));
	}
	
	public void setbornRule(int[] b) {
		bornRule=new int[b.length];
		for(int i=0;i<b.length;i++) {
			this.bornRule[i]=b[i];
		}
	}
	
	public void setsurviveRule(int[] s) {
		surviveRule=new int[s.length];
		for(int i=0;i<s.length;i++) {
			this.surviveRule[i]=s[i];
		}
	}
	
	public int[] getbornRule() {
		return this.bornRule;
	}
	
	public int[] getsurviveRule() {
		return this.surviveRule;
	}
	
	public void saveGame() {
		System.out.println("Saving");
		gameGrid.printGrid();
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
		gameGrid.printGrid();
		}catch(IOException e) {} catch (ClassNotFoundException e) {
			System.out.println("Valami nem jó");
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
