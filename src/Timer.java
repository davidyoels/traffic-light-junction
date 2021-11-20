/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david salmon
 */
public class Timer extends Thread
{
    private final long time;
    private final Event evTime;

    public Timer(long time,Event evTime)
    {
        this.time=time;
        this.evTime=evTime;
        setDaemon(true);
        start();
    }

    public void run()
    {
        try 
		{
            sleep(time);
        } catch (InterruptedException ex) {}
        evTime.sendEvent();
    }

}
