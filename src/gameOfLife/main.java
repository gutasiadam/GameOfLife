package gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class main {
	public static void main(String[] args) {
		//Játék létrehozása
		Game gameOfLife= new Game();
		//Kerek létrehozása
		AppFrame frame = new AppFrame(gameOfLife);
		//Megjelenítés
		frame.setVisible(true);
		//Szomszédság-teszt:
		//gameOfLife.getGameGrid().printCellNeighBors(30, 20);
	}

}
