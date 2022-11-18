/**
 * A játék UI elemei, annak listenerjei.
 * @author gutasiadam
 */
package gameOfLife;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AppFrame extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	protected JPanel mainPanel;
	private ArrayList<CellButton> gameGridButtons;
	
	protected static Game gameOfLifeGame;
	private JLabel statusLabel; //Státuskijelző címke.
	private JTextField ruleInput; //Szabálybemeneti mező.
	
	private JButton addRuleButton; // OK gomb.
	private JButton resetButton; // Reset gomb.
	private JButton saveStateButton; // Save game gomb.
	private JButton loadStateButton; // Load game gomb.
	
	private JLabel iterationLabel; // Jelenlegi iteráció számát kijelző címke.
	private JLabel populationLabel; // Jelenlegi populáció számát kijelző címke.
	
	private JButton stepButton; // Kézi léptető gomb.
	private JButton autoButton; // Automata szimuláció gomb.
	
	private JButton x1Button; // 1x szimuláció gomb.
	private JButton x2Button; // 2x szimuláció gomb.
	private JButton x3Button; // 3x szimuláció gomb.
	private JButton x4Button; // 4x szimuláció gomb.
	
	private int sleepTime; // A szimuáció iterációi közti időköz
	private boolean autoSimulationAllowed; // Engefélyezett-e az auto szimuláció iterálása.
	
	/**
	 * Step gomb lekérése
	 * @return
	 */
	public JButton getstepButton() {return this.stepButton;}
	
	/**
	 * OK gomb lekérése
	 * @return
	 */
	public JButton getaddRuleButton() {return this.addRuleButton;}
	
	/**
	 * Szabály bemeneti mező lekérése
	 * @return
	 */
	public JTextField getruleInput() {return this.ruleInput;}
	
	/**
	 * Load gomb lekérése
	 * @return
	 */
	public JButton getSaveButton() {return this.saveStateButton;}
	
	/**
	 * Save gomb lekérése
	 * @return
	 */
	public JButton getLoadButton() {return this.loadStateButton;}
	
	/**
	 * Reset gomb lekérése
	 * @return
	 */
	public JButton getResetButton() {return this.resetButton;}
	
	/**
	 * Visszaadja, hogy engedélyezett-e az Auto gomb megnyomása.
	 * @return
	 */
	public boolean getautoEnabled(){return autoButton.isEnabled();}
	
	/**
	 * Beállítja, hogy engedályezett-e az Auto gomb megnyomása.
	 * @param b
	 */
	public void setautoEnabled(boolean b) {autoButton.setEnabled(b);}
	
	/**
	 * Visszaadja, hogy éppen engedélyezett-e az automatikus léptetés/szimulálás.
	 * @return
	 */
	public boolean getautoSimulationAllowed(){return autoSimulationAllowed;}
	
	/**
	 * Beállítja, hogy engedélyezett-e az automatikus léptetés/szimulálás.
	 * @param b
	 */
	public void setautoSimulationAllowed(boolean b) {autoSimulationAllowed=b;}
	
	/**
	 * Két szimulált iteráció közötti időköz lekérése.
	 * @return
	 */
	public int getSleepTime() {return this.sleepTime;}
	
	/**
	 * Két szimulált iteráció közötti időköz beállítása.
	 * @return
	 */
	public void setSleepTime(int to) {this.sleepTime=to;}
	
	/**
	 * Léptetés engedélyezése.
	 */
	private void enableStepping() {
		this.stepButton.setEnabled(true);
	}
	
	/**
	 * Léptetés tiltása.
	 */
	private void disableStepping() {
		this.stepButton.setEnabled(false);
	}
	
	/**
	 * UI cellatáblázatának frissítése.
	 * meghívja a Game osztály iterációért felelőt methódusait, majd az eredmény alapján
	 * átállítja a UI cellák színeit.
	 */
	private void Step() {
		gameOfLifeGame.nextIteration();
		gameOfLifeGame.applyIteration();
		
		for(CellButton i : gameGridButtons) {
			if(i.getConnectedCell().isAlive()==true) i.setBackground(Color.YELLOW);
			else i.setBackground(Color.BLACK);
		}
		//re-render game fields
		mainPanel.repaint();
		mainPanel.revalidate();
		iterationLabel.setText("<html>Iteration: <strong>"+String.valueOf(gameOfLifeGame.getIteration()+"</strong><html>"));
		populationLabel.setText("<html>Population: <strong>"+String.valueOf(gameOfLifeGame.getPopulation()+"</strong><html>"));
	}
	
	/**
	 * Megtiltja, hogy a felhasználó mentett jáékmenetet töltsön be, vagy állítsa a cellák állapotát.
	 */
	private void lockInputs() {
		this.addRuleButton.setEnabled(false);
		for(CellButton cellButton:gameGridButtons) {
			cellButton.setEnabled(false);
		}
		saveStateButton.setEnabled(true);
		loadStateButton.setEnabled(false);
	}
	
	/**
	 * Engedélyezi, hogy a felhasználó mentett jáékmenetet töltsön be, vagy állítja a cellák állapotát.
	 */
	private void unlockInputs() {
		disableStepping();
		this.addRuleButton.setEnabled(true);
		for(CellButton cellButton:gameGridButtons) {
			cellButton.setEnabled(true);
			cellButton.getConnectedCell().setNextiteration(false);
			cellButton.setBackground(Color.BLACK);
		}
		saveStateButton.setEnabled(false);
		loadStateButton.setEnabled(true);
		
	}
	
	/**
	 * A Swing Frame, amiben a játék UI látszik.
	 * @param game
	 */
	public AppFrame(Game game) {
		super("Game Of Life");
		
		ImageIcon im = new ImageIcon(this.getClass().getResource("/assets/icon.png"));
		this.setIconImage(im.getImage());
		
		this.sleepTime=1000;
		AppFrame.gameOfLifeGame=game;
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension windowSize = new Dimension(1280, 1280);
		this.setPreferredSize(windowSize);
		this.setResizable(true);
		this.mainPanel= new JPanel(); // GridBagLayout?
		this.mainPanel.setLayout(new BorderLayout());

		/**
		 * Top Panel
		 * Eleme a státuskijelző, betöltő és mentő gomb.
		 */
		
		JPanel topPanel= new JPanel(new GridLayout(1,6));
		mainPanel.add(topPanel,BorderLayout.PAGE_START);
		
		/**
		 * Top panel - statusLabel
		 */
		statusLabel= new JLabel("no status Set");
		statusLabel.setForeground(Color.BLACK);
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusLabel.setMinimumSize(new Dimension(300, 40));
		statusLabel.setPreferredSize(new Dimension(300, 40));
		statusLabel.setMaximumSize(new Dimension(500, 40));
		statusLabel.setFont(new Font("big", Font.PLAIN, 30));
		topPanel.add(statusLabel);
		
		/**
		 * Top panel - rule input
		 */
		
		ruleInput = new JTextField(25);
		addRuleButton=new JButton("OK");  
		addRuleButton.addActionListener(new addRuleButtonListener());
		saveStateButton= new JButton("Save state");
		saveStateButton.addActionListener(new saveButtonListener());
		saveStateButton.setEnabled(false);
		
		loadStateButton= new JButton("Load state");
		loadStateButton.addActionListener(new loadButtonListener());
		
		/**
		 * Top panel - additional spacing
		 */
	
		topPanel.setBorder(new LineBorder(Color.ORANGE, 4, true));
		topPanel.add(ruleInput, BorderLayout.CENTER);
		topPanel.add(addRuleButton);
		
		topPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		topPanel.add(saveStateButton, BorderLayout.SOUTH);
		topPanel.add(loadStateButton, BorderLayout.SOUTH);
		
		/**
		 * Top panel - Reset button
		 */
		
		resetButton= new JButton("Reset");
		topPanel.add(resetButton);
		resetButton.addActionListener(new resetButtonListener());
        
		
		/**
		 * Game panel
		 * Eleme egy játékmező.
		 */
		JPanel gamePanel= new JPanel();
		JPanel gameField = new JPanel(new GridLayout(50,50,1,1));
		mainPanel.add(gamePanel);
		gamePanel.add(gameField);
		mainPanel.add(gamePanel,BorderLayout.CENTER);
		gameField.setBorder(new LineBorder(Color.RED, 4, true));
		
			
		mainPanel.setBorder(new LineBorder(Color.BLUE, 4, true));
		
        this.setSize(windowSize);
        
        //A játékmezőt felépítő JButton elemek.
        gameGridButtons=new ArrayList<CellButton>();
        for(int i=0;i<50;i++) {
        	for(int j=0;j<50;j++) {
        		Cell ce = new Cell();
        		CellButton but= new CellButton(new ButtonAction(i+"+"+j));
        		gameOfLifeGame.getGameGrid().setCellByPos(i, j, ce);
        		but.setBorderPainted(false);
        		but.setText(null);
        		but.setName(i+" "+j);
        		but.setPreferredSize(new Dimension(11, 11));
        		but.setBackground(Color.BLACK);
        		but.setOpaque(true);
        		gameGridButtons.add(but);
        		gameField.add(but);
        		gameOfLifeGame.getGameGrid().setCellByPos(i, j, ce);
        		but.setConnectedCell(gameOfLifeGame.getGameGrid().getCellByPos(i, j));
        	}
        }
        autoButton = new JButton("Auto");
        autoButton.setPreferredSize(new Dimension(100,50));
        setautoEnabled(false);
        autoButton.addActionListener(new autoButtonListener());
        stepButton = new JButton("Step");
        stepButton.addActionListener(new stepButtonListener());
        stepButton.setEnabled(false);
        stepButton.setPreferredSize(new Dimension(100,50));
        
        
        /**
		 * Bottom panel - eleme a statisztika kijelzők, és a léptetéshez szükséges gombok.
		 */
		
		JPanel bottomPanel= new JPanel(new GridLayout(1,8));
		bottomPanel.setPreferredSize(new Dimension(1,100));
		mainPanel.add(bottomPanel,BorderLayout.PAGE_END);
		bottomPanel.setBorder(new LineBorder(Color.ORANGE, 4, true));
		

		/**
		 * xN buttons - Segítésgükkel állítható az Auto szimuláció sebessége
		 */
		
		this.x1Button=new JButton("x1"); this.x1Button.addActionListener(new x1ButtonListener());
		this.x2Button=new JButton("x2"); this.x2Button.addActionListener(new x2ButtonListener());
		this.x3Button=new JButton("x3"); this.x3Button.addActionListener(new x3ButtonListener());
		this.x4Button=new JButton("x4"); this.x4Button.addActionListener(new x4ButtonListener());
		
        /**
         * Bottom panel - Népességszámláló
         */
        
        populationLabel=new JLabel("Population: <>");
        bottomPanel.add(populationLabel);
        
        /**
         * Bottom panel - Iteráció számláló
         */
        
        iterationLabel=new JLabel("Iteration: <>");
        bottomPanel.add(iterationLabel);
        bottomPanel.add(autoButton);
        bottomPanel.add(x1Button); bottomPanel.add(x2Button); bottomPanel.add(x3Button); bottomPanel.add(x4Button);
        bottomPanel.add(stepButton);
       
        
		this.add(mainPanel);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	/**
	 * Az új szabály beviteléhez tartozó gomb Listenerje.
	 *
	 */
    private class addRuleButtonListener implements ActionListener
	{	
    	/**
    	 * karakter tömb átalakítása karakterenként, számtömbbé.
    	 * 
    	 * @param c
    	 * @return int-ekből álló tömb
    	 */
    	public int[] chartoIntArray(char[] c) {
    		int len=c.length;
    		int[] res=new int[len];
    		
    		for(int i=0;i<len;i++) {
    			res[i]=c[i]-'0';
    		}
    		return res;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent ae) {
			String ruleText=ruleInput.getText();
			//System.out.println(ruleInput.getText().isBlank());
			if(gameOfLifeGame.validateRule(ruleText.toUpperCase()) && !(ruleInput.getText().isBlank())) {
				//System.out.println("Valid rule entered");
				lockInputs();
				statusLabel.setForeground(Color.green);
				statusLabel.setText(ruleInput.getText());
				
				//B/S szétválasztása
				ruleText=ruleText.replaceAll("[A-Z]", "");
				String[] splittedRuleInput=ruleText.split("\\/");
				
				//Debug
				//System.out.println("Splitted rule input 'B' is "+ splittedRuleInput[0]);
				//System.out.println("Splitted rule input 'S' is "+ splittedRuleInput[1]);
				
				gameOfLifeGame.setbornRule(chartoIntArray(splittedRuleInput[0].toCharArray()));
				gameOfLifeGame.setsurviveRule(chartoIntArray(splittedRuleInput[1].toCharArray()));
				enableStepping();
				setautoEnabled(true);
				
			}else {
				statusLabel.setText("Not a valid input");
				statusLabel.setForeground(Color.RED);
				disableStepping();
			}
			
		}
	}
    
    /**
     * Az egyszeres sebességű szimulációt beállító gombhoz tartozó listener.
     */
    private class x1ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setSleepTime(400);
		}
    	
    }
    
    
    /**
     * A kétszeres sebességű szimulációt beállító gombhoz tartozó listener.
     */
    private class x2ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setSleepTime(200);
		}
    	
    }
    
    /**
     * A háromszoros sebességű szimulációt beállító gombhoz tartozó listener.
     */
    private class x3ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setSleepTime(100);
		}
    	
    }
    
    
    /**
     * A négyszeres sebességű szimulációt beállító gombhoz tartozó listener.
     */
    private class x4ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setSleepTime(50);}
    }
    
    
    /**
     * Az automata szimulációt állító gombhoz tartozó listener.
     */
    private class autoButtonListener implements ActionListener{
    	public static SimulationWorker s;
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		if(autoButton.getText()=="Auto") {
    			s=new SimulationWorker();
    			setautoSimulationAllowed(true);
    			s.execute();
    			autoButton.setText("Pause");
    			disableStepping();
    			
    		}else {
    			autoButton.setText("Auto");
    			setautoSimulationAllowed(false);
    			s.done();
    			enableStepping();
    		}
    	}
    }
    
    
    /**
     * A kézi léptető gombhoz tartozó listener.
     */
    private class stepButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		Step();
    	}
    }
    
    /*
     * A játékállás betöltéséhez tartozó gomb listenerje.
     */
    private class loadButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		if(gameOfLifeGame.loadGame()) {
    			statusLabel.setText("Saved game loaded.");
    			lockInputs();
    			enableStepping();
        		iterationLabel.setText("<html>Iteration: <strong>"+String.valueOf(gameOfLifeGame.getIteration()+"</strong><html>"));
        		populationLabel.setText("<html>Population: <strong>"+String.valueOf(gameOfLifeGame.getPopulation()+"</strong><html>"));
        		for(int i=0;i<50;i++) {
                	for(int j=0;j<50;j++) {
                		gameGridButtons.get((i+(50*j))).setConnectedCell(gameOfLifeGame.getGameGrid().getCellByPos(i, j));
                	}}
        		for(CellButton cellButton:gameGridButtons) {
        			if(cellButton.getConnectedCell().aliveOnNextIteration) {
        				cellButton.setBackground(Color.YELLOW);
        			}
        				
        		}
    		}else {
    			unlockInputs();
    			disableStepping();
    			statusLabel.setText("Failed to load saved game.");
    		}
    	}
    }
    
    /*
     * A játékállás mentéséhez tartozó gomb listenerje.
     */
    private class saveButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		gameOfLifeGame.saveGame();
    	}
    }
    
    
    /*
     * A játékállás alpahelyzetbe állításához tartozó gomb listenerje.
     */
    private class resetButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		disableStepping();
    		setautoSimulationAllowed(false);
    		autoButton.setText("Auto");
    		for(CellButton i : gameGridButtons) {
    			i.getConnectedCell().setNextiteration(false);
    			i.getConnectedCell().setState(false);
    		}
    		unlockInputs();
			statusLabel.setText("No rule set.");
			statusLabel.setForeground(Color.BLACK);
			iterationLabel.setText("0");
    		populationLabel.setText("0");
    		gameOfLifeGame.resetIteration();
    		

    		
			mainPanel.repaint();
			mainPanel.revalidate();
			
    	}
    }


	@Override
	public void run() {
		this.setVisible(true);
	}
	
	/**
	 * Automatikus szimuláció megvalósítása egy külön szálon.
	 * Ameddig engedélyezett az automatikus szimulálás, addig automatikusan iterál, a megadott
	 * időközökkel.
	 * @url https://www.oreilly.com/library/view/learning-java-4th/9781449372477/ch16s05.html
	 */
	class SimulationWorker extends SwingWorker<String, Object> {
	    @Override
	    public String doInBackground() {

	        // Simulation for sleepTime, without blocking UI
	        try {
	        	while(getautoSimulationAllowed()==true) {
		            Thread.currentThread();
					Thread.sleep(sleepTime);
					Step();
	        	}
	        } catch (InterruptedException ignore) {}   
	        return "ended";
	    }

	    @Override
	    protected void done() {
	        try {
	            Thread.currentThread().interrupt();
	            //System.out.println("Bye");
	        } catch (Exception ignore) {}
	    }
	}
	
}

/**
 * A beállítási fázis folyamán, a játékmezőn elvégzett felhasználói utasításokat
 * kezelő listener.
 */
class ButtonAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
	private static final Color OFF = Color.BLACK;
    private static final Color ON = Color.YELLOW;

    public ButtonAction(String name) {
        super(name);
        int mnemonic = (int) name.charAt(0);
        putValue(MNEMONIC_KEY, mnemonic);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	CellButton btn = (CellButton) e.getSource();	
        Color c = btn.getBackground();
        c = c == OFF ? ON : OFF;
        if(c==OFF) {
        	btn.getConnectedCell().setState(false);
        }else {
        	btn.getConnectedCell().setState(true);
        }
        btn.setBackground(c);
    }
    
}


