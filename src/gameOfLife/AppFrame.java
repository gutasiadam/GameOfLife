package gameOfLife;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AppFrame extends JFrame{
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
	
	int sleepTime=200;
	
	//some settings that needs to be stored
	
	String simulationstatus;
	private void enableStepping() {
		this.stepButton.setEnabled(true);
		this.autoButton.setEnabled(true);
	}
	
	private void disableStepping() {
		this.stepButton.setEnabled(false);
		this.autoButton.setEnabled(false);
	}
	private void autoStep() {
		while(true) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mainPanel.removeAll();
		gameOfLifeGame.nextIteration();
		gameOfLifeGame.applyIteration();
		//re-render game fields
		for(gameOfLifeCellbutton i : gameGridButtons) {
			if(i.getConnectedCell().isAlive()==true) i.setBackground(Color.YELLOW);
			else i.setBackground(Color.BLACK);
		}
		mainPanel.repaint();
		mainPanel.revalidate();
		}
	}
	
	private void lockInputs() {
		this.addRuleButton.setEnabled(false);
		for(gameOfLifeCellbutton cellButton:gameGridButtons) {
			cellButton.setEnabled(false);
		}
		saveStateButton.setEnabled(true);
		loadStateButton.setEnabled(false);
		enableStepping();
	}
	
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
	public AppFrame(Game game) {
		super("Game Of Life");
		AppFrame.gameOfLifeGame=game;
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension windowSize = new Dimension(1280, 1280);
		this.setPreferredSize(windowSize);
		this.setResizable(true);
		this.mainPanel= new JPanel(); // GridBagLayout?
		this.mainPanel.setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;

		/**
		 * Top Panel
		 * Eleme a státuskijelző, betöltő és mentő gomb.
		 */
		
		JPanel topPanel= new JPanel(new GridLayout(1,6));
		mainPanel.add(topPanel,BorderLayout.PAGE_START);
		
		/**
		 * Bottom panel - statusLabel
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
		 * Bottom panel - rule input
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
		 * Bottom panel - additional spacing
		 */
	
		topPanel.setBorder(new LineBorder(Color.ORANGE, 4, true));
		topPanel.add(ruleInput, BorderLayout.CENTER);
		topPanel.add(addRuleButton);
		
		topPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		topPanel.add(saveStateButton, BorderLayout.SOUTH);
		topPanel.add(loadStateButton, BorderLayout.SOUTH);
		
		/**
		 * Bottom panel - Reset button
		 */
		
		resetButton= new JButton("Reset");
		topPanel.add(resetButton);
		resetButton.addActionListener(new resetButtonListener());
        
		
		/**
		 * Game panel
		 * Eleme egy játékmező, és a játékhoz tartozó gombok.
		 */
		JPanel gamePanel= new JPanel();
		JPanel gameField = new JPanel(new GridLayout(50,50,1,1));
		mainPanel.add(gamePanel);
		//leftside.setBackground(Color.black);
		c.gridx = 0;
		c.gridy = 0;
		gamePanel.add(gameField);
		mainPanel.add(gamePanel,BorderLayout.CENTER);
		gameField.setBorder(new LineBorder(Color.RED, 4, true));
		
			
		mainPanel.setBorder(new LineBorder(Color.BLUE, 4, true));
		
        this.setSize(windowSize);
        
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
		 * Bottom panel
		 */
		
		JPanel bottomPanel= new JPanel(new GridLayout(1,8));
		bottomPanel.setPreferredSize(new Dimension(1,100));
		//mainPanel.add(bottomPanel,BorderLayout.PAGE_END);
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(bottomPanel,BorderLayout.PAGE_END);
		bottomPanel.setBorder(new LineBorder(Color.ORANGE, 4, true));
		

		/**
		 * xN buttons
		 */
		
		this.x1Button=new JButton("x1");
		this.x2Button=new JButton("x2");
		this.x3Button=new JButton("x3");
		this.x4Button=new JButton("x4");
        /**
         * Bottom panel - Population Counter
         */
        
        populationLabel=new JLabel("Population: <>");
        bottomPanel.add(populationLabel);
        
        /**
         * Bottom panel - Iteration Counter
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
			System.out.println(ruleInput.getText().isBlank());
			if(gameOfLifeGame.validateRule(ruleText) && !(ruleInput.getText().isBlank())) {
				System.out.println("Valid rule entered");
				lockInputs();
				statusLabel.setForeground(Color.green);
				statusLabel.setText(ruleInput.getText());
				
				//B-S szétválasztása
				ruleText=ruleText.replaceAll("[A-Z]", "");
				String[] splittedRuleInput=ruleText.split("\\/");
				
				//Debug
				//System.out.println("Splitted rule input 'B' is "+ splittedRuleInput[0]);
				//System.out.println("Splitted rule input 'S' is "+ splittedRuleInput[1]);
				
				gameOfLifeGame.setbornRule(chartoIntArray(splittedRuleInput[0].toCharArray()));
				gameOfLifeGame.setsurviveRule(chartoIntArray(splittedRuleInput[1].toCharArray()));
				
				enableStepping();
				
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
			// TODO Auto-generated method stub
			sleepTime=200;
		}
    	
    }
    
    private class x2ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			sleepTime=100;
		}
    	
    }
    
    private class x3ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			sleepTime=50;
		}
    	
    }
    
    private class x4ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			sleepTime=25;
		}
    	
    }
    
    private class autoButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		autoButton.setEnabled(false);
    		autoStep();
    	}
    }
    
    private class stepButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		//mainPanel.removeAll();
    		gameOfLifeGame.nextIteration();
    		gameOfLifeGame.applyIteration();
    		//re-render game fields
    		for(gameOfLifeCellbutton i : gameGridButtons) {
    			if(i.getConnectedCell().isAlive()==true) i.setBackground(Color.YELLOW);
    			else i.setBackground(Color.BLACK);
    		}
    		mainPanel.repaint();
    		mainPanel.revalidate();
    		
    		iterationLabel.setText("<html>Iteration: <strong>"+String.valueOf(gameOfLifeGame.getIteration()+"</strong><html>"));
    		populationLabel.setText("<html>Population: <strong>"+String.valueOf(gameOfLifeGame.getPopulation()+"</strong><html>"));
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
			mainPanel.repaint();
			mainPanel.revalidate();
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
        	System.out.println("Selected cell is "+btn.getConnectedCell().getAliveOnNextIteration()+" on next iteration");
        }else {
        	btn.getConnectedCell().setState(true);
        	System.out.println("Selected cell is "+btn.getConnectedCell().getAliveOnNextIteration()+" on next iteration");
        }
        btn.setBackground(c);
    }
    
}
