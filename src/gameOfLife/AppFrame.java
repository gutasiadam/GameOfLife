package gameOfLife;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AppFrame extends JFrame implements Runnable{
	protected JPanel mainPanel;
	private ArrayList<gameOfLifeCellbutton> gameGridButtons;
	
	protected static Game gameOfLifeGame;
	private JLabel statusLabel;
	private JTextField ruleInput;
	
	private JButton addRuleButton;
	private JButton resetButton;
	private JButton saveStateButton;
	private JButton loadStateButton;
	
	private JLabel iterationLabel;
	private JLabel populationLabel;
	
	private JButton stepButton;
	private JButton autoButton;
	
	private JButton x1Button;
	private JButton x2Button;
	private JButton x3Button;
	private JButton x4Button;
	
	private int sleepTime;
	private boolean autoSimulationAllowed;
	
	String simulationstatus;
	public boolean getautoEnabled(){return autoButton.isEnabled();}
	public void setautoEnabled(boolean b) {autoButton.setEnabled(b);}
	
	public boolean getautoSimulationAllowed(){return autoSimulationAllowed;}
	public void setautoSimulationAllowed(boolean b) {autoSimulationAllowed=b;}
	
	public int getSleepTime() {return this.sleepTime;}
	public void setSleepTime(int to) {this.sleepTime=to;}
	
	
	private void enableStepping() {
		this.stepButton.setEnabled(true);
	}
	
	private void disableStepping() {
		this.stepButton.setEnabled(false);
	}
	private void Step() {
		gameOfLifeGame.nextIteration();
		gameOfLifeGame.applyIteration();
		
		for(gameOfLifeCellbutton i : gameGridButtons) {
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
	 * Megtiltja, hogy a felhasználó mentett jáékmenetet töltsön be, vagy állítja a cellák állapotát.
	 */
	private void lockInputs() {
		this.addRuleButton.setEnabled(false);
		for(gameOfLifeCellbutton cellButton:gameGridButtons) {
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
		for(gameOfLifeCellbutton cellButton:gameGridButtons) {
			cellButton.setEnabled(true);
			cellButton.getConnectedCell().setNextiteration(false);
			cellButton.setBackground(Color.BLACK);
		}
		saveStateButton.setEnabled(false);
		loadStateButton.setEnabled(true);
		
	}
	
	
	public void executeAutoSimulation(){
		
	}
	/**
	 * A Swing Frame, amiben a játék UI látszik.
	 * @param game
	 */
	public AppFrame(Game game) {
		super("Game Of Life");
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
        gameGridButtons=new ArrayList<gameOfLifeCellbutton>();
        for(int i=0;i<50;i++) {
        	for(int j=0;j<50;j++) {
        		Cell ce = new Cell();
        		gameOfLifeCellbutton but= new gameOfLifeCellbutton(new ButtonAction(i+"+"+j));
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
        autoButton.setEnabled(false);
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
		 * xN buttons - segítésgükkel állítható az Auto szimuláció sebessége
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
	 * @author gutasiadam
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
    
    private class x1ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setSleepTime(400);
		}
    	
    }
    
    private class x2ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setSleepTime(200);
		}
    	
    }
    
    private class x3ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setSleepTime(100);
		}
    	
    }
    
    private class x4ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setSleepTime(50);}
    }
    
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
    
    private class stepButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		Step();
    	}
    }
    
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
        		for(gameOfLifeCellbutton cellButton:gameGridButtons) {
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
    
    private class saveButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		gameOfLifeGame.saveGame();
    	}
    }
    
    
    private class resetButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		for(gameOfLifeCellbutton i : gameGridButtons) {
    			i.getConnectedCell().setNextiteration(false);
    			i.getConnectedCell().setState(false);
    		}
    		unlockInputs();
			statusLabel.setText("No rule set.");
			statusLabel.setForeground(Color.BLACK);
			iterationLabel.setText("0");
    		populationLabel.setText("0");
    		gameOfLifeGame.resetIteration();
    		
    		disableStepping();
    		setautoSimulationAllowed(false);
    		autoButton.setText("Auto");
    		
			mainPanel.repaint();
			mainPanel.revalidate();
			
    	}
    }


	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.setVisible(true);
		this.setautoEnabled(true);
	}
	
	/**
	 * 
	 * @author gutasiadam
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
class gameOfLifeCellbutton extends JButton{
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
	
	public gameOfLifeCellbutton(ButtonAction bA) {
		super(bA);
	}
}
class ButtonAction extends AbstractAction {
    private static final Color OFF = Color.BLACK;
    private static final Color ON = Color.YELLOW;

    public ButtonAction(String name) {
        super(name);
        int mnemonic = (int) name.charAt(0);
        putValue(MNEMONIC_KEY, mnemonic);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	gameOfLifeCellbutton btn = (gameOfLifeCellbutton) e.getSource();
    	
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


