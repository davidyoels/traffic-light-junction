/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david salmon
 */
import javax.swing.JFrame;



public class TrafficLightFrame extends JFrame 
{
	private final int WIDTH = 800, HEIGHT = 750;
	TrafficLightPanel myPanel;

	public TrafficLightFrame(String h, Ramzor[] ramzorim) 
	{
		super(h);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(90, -15);
		myPanel = new TrafficLightPanel(ramzorim);
		add(myPanel);
		pack();
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setVisible(true);
	}
}

