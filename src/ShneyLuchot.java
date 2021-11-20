/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david salmon
 */
import java.awt.Color;

import javax.swing.JPanel;

class ShneyLuchot extends Thread {

    Ramzor ramzor;
    JPanel panel;
    Event evWeek, evSaturday, evToGreen, evToRed, evAckRed, evTimer;
    Timer timer;

    enum DayState {
        ON_WEEK, ON_SATURDAY
    }

    enum RamzorState {
        ON_RED, ON_GREEN
    }

    DayState dayState;
    RamzorState ramzorState;

    public ShneyLuchot( Ramzor ramzor,JPanel panel)
	{
		this.ramzor=ramzor;
		this.panel=panel;
		start();
	}

	public ShneyLuchot(Ramzor ramzor, JPanel panel, Event evChaol, Event evShabat, Event evToGreen, Event evToRed, Event evAckRed)
	{
		this.evWeek = evChaol;
		this.evSaturday = evShabat;
		this.evToGreen = evToGreen;
		this.evToRed = evToRed;
		this.evAckRed = evAckRed;
		this.ramzor = ramzor;
		this.panel = panel;

		start();
	}


    public void run() {
        dayState = DayState.ON_WEEK;
        while (true) {
            switch (dayState) {

                case ON_WEEK:
                    evAckRed.sendEvent();
                    ramzorState = RamzorState.ON_RED;
                    setToRed();

                    while (dayState == DayState.ON_WEEK) {
                        switch (ramzorState) {
                            case ON_RED:
                                evTimer = new Event();
                                timer = new Timer(1100, evTimer);
                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        if (evToGreen.arrivedEvent()) {
                                            evToGreen.waitEvent();
                                            setToGreen();
                                            ramzorState = RamzorState.ON_GREEN;
                                        }
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;
                            case ON_GREEN:
                                while (true) {
                                    if (evToRed.arrivedEvent()) {
                                        evToRed.waitEvent();
                                        setToRed();
                                        evAckRed.sendEvent();
                                        ramzorState = RamzorState.ON_RED;
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    }
                    break;
                case ON_SATURDAY:
                    setToGray();
                    evWeek.waitEvent();
                    dayState = DayState.ON_WEEK;
                    break;
                default:
                    break;
            }
        }
    }

    public void setLight(int place, Color color) {
        ramzor.colorLight[place - 1] = color;
        panel.repaint();
    }

    private void setToGreen() {
        setLight(1, Color.GRAY);
        setLight(2, Color.GREEN);
    }

    private void setToRed() {
        setLight(1, Color.RED);
        setLight(2, Color.GRAY);
    }

    //turn off ramzor
    private void setToGray() {
        setLight(1, Color.GRAY);
        setLight(2, Color.GRAY);
    }
}

/*
 sleep(1000);
                    setLight(1, Color.GRAY);
                    setLight(2, Color.GREEN);
                    sleep(1000);
                    setLight(1, Color.RED);
                    setLight(2, Color.GRAY);
 */
