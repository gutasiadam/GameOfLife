package gameOfLife;
import java.io.Serializable;
import java.util.HashMap;
public class Cell implements Serializable {
	HashMap<Direction, Cell> neighbors;
	boolean state=false;
	boolean aliveOnNextIteration;
	
	boolean nextIterationSet;
	
	public Cell() {
		this.state=false;
	}
	
	public boolean isAlive() {
		return state;
	}
	
	protected void setState(boolean b) {
		this.state=b;
	}
	
	public boolean getAliveOnNextIteration() {
		return aliveOnNextIteration;
	}
	
	protected void setNextiteration(boolean state) {
		this.aliveOnNextIteration=state;
		this.nextIterationSet=true;
	}
	

	
	public void Step() {
		this.state=this.aliveOnNextIteration;
		this.nextIterationSet=false;
	}
}