package gameOfLife;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grid implements Serializable {
	private Cell[][] gameGrid;
	
	private Grid(Cell[][] grid) {
		this.gameGrid = grid;
	}
	
	/*public Cell[][] getGameGrid(){
		return gameGrid;
	}*/
	
	public Grid(int s) {
		gameGrid= new Cell[s][s];
	}
	public Cell[][] getGrid(){
		return this.gameGrid;
	}
	
	public Cell getCellByPos(int row, int col) throws IndexOutOfBoundsException{
		try {
			return gameGrid[row][col];
		}catch(IndexOutOfBoundsException e) {
			//Az adott pozición nem létezik cella, null értékkel tér vissza.
			return null;
		}
		
	}
	
	public void setCellByPos(int row, int col, Cell c) throws IndexOutOfBoundsException{
		try {
			gameGrid[row][col]=c;
		}catch(IndexOutOfBoundsException e) {
			return;
		}
	}
	
	public void printGrid() {
		System.out.println("Printing grid");
		for(int i=0;i<49;i++) {
			for(int j=0;j<49;j++) {
				if(gameGrid[i][j].isAlive()) {
					System.out.print("X");
				}else {
					System.out.print(" ");
				}
			
			}
			System.out.print("\n");
		}
	}
	
	public HashMap<String,Cell> getCellNeighbors(int row, int col) {
		//HashMap<String,Cell> cellNeighBors = new HashMap<>();
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
	
	public int activeCellNeighbors(int row, int col) {
		int res=0;
		
		HashMap<String,Cell> neighbors = getCellNeighbors(row,col);
		ArrayList<Cell> neighborCells= new ArrayList<>(neighbors.values());
		ArrayList<String> locations = new ArrayList(neighbors.keySet());

		for(int i=0;i<neighborCells.size();i++) {
			if(neighbors.get(locations.get(i))!=null) {
				if(neighborCells.get(i).isAlive()) {
					res++;
				}	
			}
		}
		return res;
	}
	
	public void printCellNeighBors(int row, int col) {
		HashMap<String,Cell> neighbors = getCellNeighbors(row,col);
		ArrayList<Cell> neighborCells= new ArrayList<>(neighbors.values());
		ArrayList<String> locations = new ArrayList(neighbors.keySet());

		for(int i=0;i<neighborCells.size();i++) {
			System.out.println(locations.get(i)+" - "+neighbors.get(locations.get(i)));
		}
	}
}
