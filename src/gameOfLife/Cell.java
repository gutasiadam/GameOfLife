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
	
	/**
	 * Cella jelenlegi állapotának lekérése..
	 * @return
	 */
	public boolean isAlive() {
		return state;
	}
	
	/**
	 * Cella jelenlegi állapotának beállítása.
	 * @param b
	 */
	protected void setState(boolean b) {
		this.state=b;
	}
	
	/**
	 * Cella következő iterációs állapotának lekérése.
	 * @return
	 */
	public boolean getAliveOnNextIteration() {
		return aliveOnNextIteration;
	}
	
	/**
	 * Cella következő iterációs állapotának beállítása.
	 * @param state
	 */
	protected void setNextiteration(boolean state) {
		this.aliveOnNextIteration=state;
		this.nextIterationSet=true;
	}
	
	/**
	 * Cella léptetése időben.
	 */
	public void Step() {
		this.state=this.aliveOnNextIteration;
		this.nextIterationSet=false;
	}
}
