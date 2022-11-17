/**
 * Tesztek az AppFrame osztály és GUI interakciók tesztjéhez
 */
package gameOfLife;

import java.awt.Component;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AppFrameTest {
	Game  gameOfLife;
	AppFrame gameFrame;
	@Before
	public void SetUp() {
		gameOfLife= new Game();	
	}
	@Test
	/**
	 * Simulating the UI without setting it visible. 
	 * If Auto button is disabled, frame is loaded and components are set Correctly.
	 * @throws InterruptedException
	 */
	public void appFrameLoadTest() {
		gameFrame=new AppFrame(gameOfLife);
		Component[] c = gameFrame.getComponents();
		Assert.assertEquals(false,gameFrame.getautoEnabled());
		
	}
	
	@Test
	/**
	 * Felhasználó lépéseinek szimulálása
	 * 1. - szabály beírása
	 * 2. - Léptetés
	 * Ha populáció = az elvárttal teszt OK.
	 * @throws InterruptedException
	 */
	public void simulateUserOnAppFrame() throws InterruptedException {
		gameFrame=new AppFrame(gameOfLife);
		Assert.assertEquals(false,gameFrame.getautoEnabled());
		gameFrame.getruleInput().setText("B0/S1");
		gameFrame.getaddRuleButton().doClick();
		gameFrame.getstepButton().doClick();
		Assert.assertEquals(2500, gameOfLife.getPopulation());
	}
	
	@Test
	/**
	 * Mentés/betöltés tesztelése
	 * 1. - szabály beírása
	 * 2. - Léptetés
	 * 3. - mentés
	 * 4. - reset, populáció forcefully nullázása
	 * 5. - load 
	 * 6. - léptetés 2x
	 * Ha populáció = az elvárttal teszt OK.
	 * @throws InterruptedException
	 */
	public void simulateLoadSave() throws InterruptedException {
		gameFrame=new AppFrame(gameOfLife);
		//Thread t = new Thread(gameFrame);
		//t.run(); --
		Thread.sleep(1500);
		gameFrame.getruleInput().setText("B0/S1");
		gameFrame.getaddRuleButton().doClick();
		Thread.sleep(400);
		gameFrame.getstepButton().doClick();
		Thread.sleep(600);
		gameFrame.getSaveButton().doClick();
		Thread.sleep(600);
		gameFrame.getResetButton().doClick();
		Thread.sleep(1000);
		gameOfLife.resetIteration();
		
		gameFrame.getLoadButton().doClick();
		Thread.sleep(1000);
		gameFrame.getstepButton().doClick(); gameFrame.getstepButton().doClick();
		Thread.sleep(600);
		Assert.assertEquals(2500, gameOfLife.getPopulation());
		
	}
}

