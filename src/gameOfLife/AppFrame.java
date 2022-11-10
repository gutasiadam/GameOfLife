package gameOfLife;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AppFrame extends JFrame{
	private JPanel mainPanel;
	private ArrayList<gameOfLifeCellbutton> gameGridButtons;
	
	protected static Game gameOfLifeGame;
	private JLabel statusLabel;
	private JTextField ruleInput;
	
	private JButton addRuleButton;
	private JButton resetButton;
	private JButton saveStateButton;
	private JButton loadStateButton;
	//some settings that needs to be stored
	
	String simulationstatus;
	
	private void lockInputs() {
		this.addRuleButton.setEnabled(false);
		for(gameOfLifeCellbutton cellButton:gameGridButtons) {
			cellButton.setEnabled(false);
		}
		saveStateButton.setEnabled(true);
		loadStateButton.setEnabled(false);
	}
	
	private void unlockInputs() {
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
		Dimension windowSize = new Dimension(1280, 720);
		this.setPreferredSize(windowSize);
		this.setResizable(false);
		this.mainPanel= new JPanel(new GridLayout(0,2)); // GridBagLayout?
		
		
		/**
		 * Game panel
		 * Eleme egy játékmező, és a játékhoz tartozó gombok.
		 */
		JPanel gamePanel= new JPanel();
		JPanel gameField = new JPanel(new GridLayout(50,50,1,1));
		mainPanel.add(gamePanel);
		//leftside.setBackground(Color.black);
		gamePanel.add(gameField,BorderLayout.CENTER);
		gameField.setBorder(new LineBorder(Color.BLACK, 4, true));
		
			
		mainPanel.setBorder(new LineBorder(Color.BLUE, 4, true));
		
        this.setSize(windowSize);
        
        gameGridButtons=new ArrayList<gameOfLifeCellbutton>();
        for(int i=0;i<50;i++) {
        	for(int j=0;j<50;j++) {
        		Cell c = new Cell();
        		gameOfLifeCellbutton but= new gameOfLifeCellbutton(new ButtonAction(i+"+"+j));
        		gameOfLifeGame.getGameGrid().setCellByPos(i, j, c);
        		but.setBorderPainted(false);
        		but.setText(null);
        		but.setName(i+" "+j);
        		but.setPreferredSize(new Dimension(11, 11));
        		but.setBackground(Color.BLACK);
        		but.setOpaque(true);
        		gameGridButtons.add(but);
        		gameField.add(but);
        		gameOfLifeGame.getGameGrid().setCellByPos(i, j, c);
        		but.setConnectedCell(gameOfLifeGame.getGameGrid().getCellByPos(i, j));
        	}
        }
        JButton auto = new JButton("Auto");
        auto.setPreferredSize(new Dimension(100,50));
        JButton step = new JButton("Step");
        step.addActionListener(new stepButtonListener());
        step.setPreferredSize(new Dimension(100,50));
        
        
        /**
		 * Bottom panel
		 */
		
		JPanel bottomPanel= new JPanel();
		bottomPanel.setPreferredSize(new Dimension(1,100));
		mainPanel.add(bottomPanel,BorderLayout.PAGE_END);
		bottomPanel.setBorder(new LineBorder(Color.ORANGE, 4, true));
		
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
		bottomPanel.add(statusLabel);
		
		/**
		 * Bottom panel - rule input
		 */
		
		JLabel label = new JLabel("Rule ");
		JLabel labelDesc = new JLabel("Format: BXX/SXX");
		labelDesc.setForeground(Color.GRAY);
		ruleInput = new JTextField(20);
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
	
		bottomPanel.add(label, BorderLayout.NORTH);
		bottomPanel.add(labelDesc);
		bottomPanel.setBorder(new LineBorder(Color.ORANGE, 4, true));
		bottomPanel.add(ruleInput, BorderLayout.CENTER);
		bottomPanel.add(addRuleButton);
		
		bottomPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		bottomPanel.add(saveStateButton, BorderLayout.SOUTH);
		bottomPanel.add(loadStateButton, BorderLayout.SOUTH);
		
		/**
		 * Bottom panel - Reset button
		 */
		
		resetButton= new JButton("Reset");
		bottomPanel.add(resetButton);
		resetButton.addActionListener(new resetButtonListener());
        
        gamePanel.add(auto); gamePanel.add(step);
       
        
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
				System.out.println("Splitted rule input 'B' is "+ splittedRuleInput[0]);
				System.out.println("Splitted rule input 'S' is "+ splittedRuleInput[1]);
				
				gameOfLifeGame.setbornRule(chartoIntArray(splittedRuleInput[0].toCharArray()));
				gameOfLifeGame.setsurviveRule(chartoIntArray(splittedRuleInput[1].toCharArray()));
				
			}else {
				statusLabel.setText("Not a valid input");
				statusLabel.setForeground(Color.RED);
			}
			
		}
	}
    
    private class stepButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		gameOfLifeGame.nextIteration();
    		gameOfLifeGame.applyIteration();
    		//re-render game fields
    		for(gameOfLifeCellbutton i : gameGridButtons) {
    			if(i.getConnectedCell().isAlive()==true) i.setBackground(Color.YELLOW);
    			else i.setBackground(Color.BLACK);
    		}
    	}
    }
    
    private class loadButtonListener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent ae) {
    		gameOfLifeGame.loadGame();
    		System.out.println("Loading stuff");
    		
    		for(int i=0;i<50;i++) {
            	for(int j=0;j<50;j++) {
            		gameGridButtons.get((i+(50*j))).setConnectedCell(gameOfLifeGame.getGameGrid().getCellByPos(i, j));
            	}}
    		for(gameOfLifeCellbutton cellButton:gameGridButtons) {
    			if(cellButton.getConnectedCell().aliveOnNextIteration) {
    				cellButton.setBackground(Color.YELLOW);
    				System.out.println("Putty");
    			}
    				
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
	
	public gameOfLifeCellbutton(ButtonAction ba) {
		super(ba);
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
