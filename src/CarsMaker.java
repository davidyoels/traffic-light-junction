/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david salmon
 */
import javax.swing.JPanel;

public class CarsMaker extends Thread
{
	JPanel myPanel;
	private ShloshaAvot myRamzor;
	int key;
	public CarsMaker(JPanel myPanel,ShloshaAvot myRamzor, int key) 
	{
		this.myPanel=myPanel;
		this.myRamzor=myRamzor;
		this.key=key;
		setDaemon(true);
		start();
	}

	public void run()
	{
		try {
			while (true)
			{
				sleep(300);
				if ( !myRamzor.isStop())
				{
					new CarMooving(myPanel,myRamzor,key);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

