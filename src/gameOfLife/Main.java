package gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Main {
	public static void main(String[] args) {
		//Játék létrehozása
		Game gameOfLife= new Game();
		//Kerek létrehozása
		//Megjelenítés
		Thread t=new Thread(new AppFrame(gameOfLife));
		t.start();
		//Szomszédság-teszt:
		//gameOfLife.getGameGrid().printCellNeighBors(30, 20);
	}

}
