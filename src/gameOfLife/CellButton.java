/**
 * Egyedi, a JButtont kiegészítő osztály
 * Olyan UI gomb, melyhez kapcsolódik egy cella, így ő tudja váltpztatni annak állapotát.
 * A két elem a UI konstruálása során kacsolódik össze.
 */
package gameOfLife;

import javax.swing.JButton;

public class CellButton extends JButton{
		private static final long serialVersionUID = 1L;
		private Cell connectedCell;
		public void setConnectedCell(Cell c) {
			this.connectedCell=c;
		}
		public Cell getConnectedCell() {
			if(connectedCell!=null) {
				return this.connectedCell;
			}
			return null;
			
		}
		
		public CellButton(ButtonAction bA) {
			super(bA);
		}
}
