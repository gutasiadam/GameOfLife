package gameOfLife;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame{
	private JPanel mainPanel;
	
	private static Game gameOfLifeGame;
	
	//some settings that needs to be stored
	
	String simulationstatus;
	
	public AppFrame(Game game) {
		super("Game Of Life");
		
		this.mainPanel= new JPanel(new GridLayout());
		JPanel leftside= new JPanel();
		JPanel center= new JPanel();
		
		mainPanel.add(leftside);
		mainPanel.add(center);
		
		AppFrame.gameOfLifeGame=game;
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension windowSize = new Dimension(1280, 720);
		this.setPreferredSize(windowSize);
		//

		
		JPanel newPanel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Rule:");
		JTextField userName = new JTextField(20);
		 
		leftside.add(label, BorderLayout.NORTH);
		center.add(userName, BorderLayout.SOUTH);
		
		/*JLabel status= new JLabel("no status Set");
		status.setForeground(Color.RED);
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setMinimumSize(new Dimension(300, 40));
		status.setPreferredSize(new Dimension(300, 40));
		status.setMaximumSize(new Dimension(500, 40));
		status.setFont(new Font("big", Font.PLAIN, 30));
		mainPanel.add(status);
		*/
		
        this.setSize(windowSize);
        
		//this.add(mainPanel, BorderLayout.CENTER);
		this.add(mainPanel);
		
		
       
		this.pack();
		this.setLocationRelativeTo(null);

}
	
}
