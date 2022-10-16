package gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class main {

	public static void main(String[] args) {
		/*
		 * Note: When using the app on macOS the menubar is moved to the top menubar and the default laf is preserved.
		 */
		if(Objects.equals(System.getProperty("os.name"), "Mac OS X")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		//Creating the Frame
		Game gameOfLife= new Game();
		AppFrame frame = new AppFrame(gameOfLife);
		frame.setVisible(true);
	}

}
