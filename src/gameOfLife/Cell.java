package gameOfLife;
import java.util.HashMap;
public class Cell {
	HashMap<Direction, Cell> neighbors;
	boolean state=false;
	boolean aliveOnNextIteration;
	
	boolean nextIterationSet;
	
	public void Cell(boolean alive) {
		this.state=alive;
	}
	
	public boolean isAlive() {
		return state;
	}
	
	protected void setNextiteration(boolean state) {
		this.aliveOnNextIteration=state;
		this.nextIterationSet=true;
	}
	
	public void Tick() {
		this.state=this.aliveOnNextIteration;
		this.nextIterationSet=false;
	}
}
