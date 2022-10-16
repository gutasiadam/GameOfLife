package gameOfLife;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grid implements Serializable {
	private final Cell[][] gameGrid;
	
	private Grid(Cell[][] grid) {
		this.gameGrid = grid;
	}
	
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
			return null;
		}
		
	}
	
	public HashMap<String,Cell> getCellNeighbors(int row, int col) {
		//HashMap<String,Cell> cellNeighBors = new HashMap<>();
		HashMap<String,Cell> cellNeighbors = new HashMap<>(); // U1, U2, U3, R, L, B1, B2, B3
		/*for(Direction dir: Direction.values()) {
			cellNeighbors.put(dir)
		}*/
		//U1: -1row, -1:
		//cellNeighbors.add(getCellByPos(row-1,col-1));
		
		cellNeighbors.put("U1", getCellByPos(row-1,col-1));
		//U2: -1row
		cellNeighbors.put("U2", getCellByPos(row-1,col));
		//U3: -1row, +1col
		cellNeighbors.put("U3", getCellByPos(row-1,col+1));
		//R -1 col
		cellNeighbors.put("L", getCellByPos(row,col-1));
		//L: +1 col
		cellNeighbors.put("R", getCellByPos(row,col+1));
		//B1: +1row, -1col
		cellNeighbors.put("U1", getCellByPos(row-1,col-1));
		//B2: +1row
		cellNeighbors.put("U2", getCellByPos(row-1,col-1));
		//B3: +1row, -1col
		cellNeighbors.put("U3", getCellByPos(row-1,col-1));
		
		return cellNeighbors;
	}
}
