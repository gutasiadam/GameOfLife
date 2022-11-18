/**
 * A játék beállított szabályait, játékmezőjét, statisztikáit egybefogó osztály
 * @author gutasiadam
 */
package gameOfLife;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Game implements Serializable{
	private int iteration=0;
	private int population=0;
	private int[] bornRule;
	private int[] surviveRule;
	private Grid gameGrid;


	public Game(Grid grid) {
		this.gameGrid = grid;
	}
	
	public Game() {
		this.gameGrid=new Grid(50);
	}
	
	/**
	 * Következő iteráció szimulálása. Átállítja a cellák nextIteration értékeit.
	 */
	public void nextIteration() {
		this.population=0;
		//System.out.println("Calculating iteration.");
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
				if(c.isAlive()) this.population++;
			}
		}
	}
	
	/**
	 * Szabály validálása regex segítségével.
	 * @param input
	 * @return
	 */
	public boolean validateRule(String input) {
		return(input.matches("B{1}[0-9]+\\/S{1}[0-9]+"));
	}
	
	/**
	 * Születési szabály-tömb beállítása
	 * @param b
	 */
	public void setbornRule(int[] b) {
		bornRule=new int[b.length];
		for(int i=0;i<b.length;i++) {
			this.bornRule[i]=b[i];
		}
	}
	
	/**
	 * Túlélési szabály-tömb beállítása
	 * @param b
	 */
	public void setsurviveRule(int[] s) {
		surviveRule=new int[s.length];
		for(int i=0;i<s.length;i++) {
			this.surviveRule[i]=s[i];
		}
	}
	
	/**
	 * Születési szabály-tömb lekérdezése
	 * @param b
	 */
	public int[] getbornRule() {
		return this.bornRule;
	}
	
	/**
	 * Túlélési szabály-tömb lekérdezése
	 * @param b
	 */
	public int[] getsurviveRule() {
		return this.surviveRule;
	}
	
	/**
	 * Játékállás kimentése
	 * @param b
	 */
	public void saveGame() {
		try {
		FileOutputStream fout=new FileOutputStream("gameOfLifeGameState.txt");    
		ObjectOutputStream out=new ObjectOutputStream(fout);    
		out.writeObject(gameGrid);    
		out.writeObject(bornRule);
		out.writeObject(surviveRule);
		out.writeObject(iteration);
		out.writeObject(population);
		out.flush();
		out.close();
		}catch(IOException e) {}   
		
	}
	
	/**
	 * Előző játékmenet betöltése
	 * @return
	 */
	public boolean loadGame() {
		try {
			File gameData = new File("gameOfLifeGameState.txt");    
			if (gameData.exists()) {
			    FileInputStream fileInputStream = new FileInputStream(gameData);
			    ObjectInputStream in=new ObjectInputStream(fileInputStream);    
			    gameGrid=(Grid)in.readObject();
			    bornRule=(int[])in.readObject();;
			    surviveRule=(int[])in.readObject();;
			    iteration=(int)in.readObject();;
			    population=(int)in.readObject();;
			    in.close();
			}else {
				return false;
			}
		}catch(IOException e) {} catch (ClassNotFoundException e) {
			//...
		}   
		System.out.println("Game: Loading done.");
		return true;
	}
	
	/**
	 * Játékmező lekérése.
	 * @param b
	 */
	public Grid getGameGrid() {return this.gameGrid;}
	
	/**
	 * Populáció értékének növelése.
	 * @param b
	 */
	public void increasePopulation() {this.population++;}
	
	/**
	 * Populáció számának lekérése.
	 * @param b
	 */
	public int getPopulation() {return this.population;}
	
	/**
	 * Iteráció számának lekérése.
	 * @param b
	 */
	public int getIteration() {return this.iteration;}
	
	/**
	 * Iteráció visszaállítása.
	 */
	public void resetIteration() {this.iteration=0;}
}
