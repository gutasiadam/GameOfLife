package gameOfLife;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grid implements Serializable {
	private ArrayList<ArrayList<Cell>> Cells;
	
	
	public Grid(int s) {
		Cells = new ArrayList<ArrayList<Cell>>();
		for(int i=0;i<s;i++) {
			Cells.add(new ArrayList<Cell>());
			for(int j=0;j<s;j++) {
				Cells.get(i).add(new Cell());
			}
		}
	}
	public ArrayList<ArrayList<Cell>> getGrid(){
		return Cells;
	}
	
	/**
	 * Cella lekérése pozíció alapján.
	 * @param row - A cella sorának száma.
	 * @param col - A cella oszlopának száma.
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public Cell getCellByPos(int row, int col) throws IndexOutOfBoundsException{
		try {
			return Cells.get(row).get(col);
		}catch(IndexOutOfBoundsException e) {
			//Az adott pozición nem létezik cella, null értékkel tér vissza.
			return null;
		}
		
	}
	
	
	/**
	 * Cella beállítása egy adott pozícióra.
	 * @param row
	 * @param col
	 * @param c
	 * @throws IndexOutOfBoundsException
	 */
	public void setCellByPos(int row, int col, Cell c) throws IndexOutOfBoundsException{
		try {
			Cells.get(row).set(col, c);
		}catch(IndexOutOfBoundsException e) {
			return;
		}
	}
	
	/**
	 * Játékmező kirajzolása a szabványos kimenetre.
	 */
	public void printGrid() {
		System.out.println("Printing grid");
		for(int i=0;i<49;i++) {
			for(int j=0;j<49;j++) {
				if(Cells.get(i).get(j).isAlive()) {
					System.out.print("X");
				}else {
					System.out.print(" ");
				}
			
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Cella szomszédainak lekérdezése
	 * @param row - A cella sorának száma.
	 * @param col - A cella oszlopának száma.
	 * @return
	 */
	public HashMap<String,Cell> getCellNeighbors(int row, int col) {
		HashMap<String,Cell> cellNeighbors = new HashMap<>(); // U1, U2, U3, R, L, B1, B2, B3
		
		cellNeighbors.put("U1", getCellByPos(row-1,col-1));
		//U2: -1row
		cellNeighbors.put("U2", getCellByPos(row-1,col));
		//U3: -1row, +1col
		cellNeighbors.put("U3", getCellByPos(row-1,col+1));
		//R -1 col
		cellNeighbors.put("L", getCellByPos(row,col-1));
		//L: +1 col
		cellNeighbors.put("R", getCellByPos(row,col+1));
		//D1: +1row, -1col
		cellNeighbors.put("D1", getCellByPos(row+1,col-1));
		//D2: +1row
		cellNeighbors.put("D2", getCellByPos(row+1,col));
		//D3: +1row, -1col
		cellNeighbors.put("D3", getCellByPos(row+1,col+1));
		
		return cellNeighbors;
	}
	
	/**
	 * Cella aktív szomszédainak számának lekérdezése
	 * @param row - A cella sorának száma.
	 * @param col - A cella oszlopának száma.
	 * @return
	 */
	public int activeCellNeighbors(int row, int col) {
		int res=0;
		
		HashMap<String,Cell> neighbors = getCellNeighbors(row,col);
		ArrayList<Cell> neighborCells= new ArrayList<Cell>(neighbors.values());
		ArrayList<String> locations = new ArrayList<String>(neighbors.keySet());

		for(int i=0;i<neighborCells.size();i++) {
			if(neighbors.get(locations.get(i))!=null) {
				if(neighborCells.get(i).isAlive()) {
					res++;
				}	
			}
		}
		return res;
	}
}
