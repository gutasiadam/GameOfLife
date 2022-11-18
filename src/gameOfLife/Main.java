/**
 * Játék elindítását végző osztály.
 */
package gameOfLife;

public class Main {
	public static void main(String[] args) {
		//Játék létrehozása
		Game gameOfLife= new Game();
		//Szál létrehozása
		//Megjelenítés
		Thread t=new Thread(new AppFrame(gameOfLife));
		t.start();
	}
}
