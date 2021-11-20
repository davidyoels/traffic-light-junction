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
import static java.lang.Thread.yield;

import javax.swing.JPanel;

public class ShloshaAvot extends Thread {

    Ramzor ramzor;
    JPanel panel;

    enum DayState {
        ON_WEEK, ON_SATURDAY
    }

    enum RamzorState {
        ON_RED, ON_ORANGE_RED, ON_GREEN, ON_FLICKER, ON_ORANGE, OFF_ORANGE
    }

    enum InRamzorState {
        ON_GREEN, OFF_GREEN
    }

    Event evWeek, evSaturday, evToGreen, evToRed, evAckRed, evTimer;

    Timer timer;

    DayState dayState;
    RamzorState ramzorState;
    InRamzorState inRamzorState;

    private boolean stop = true;

    public ShloshaAvot(Ramzor ramzor, JPanel panel, int key) {
        this.ramzor = ramzor;
        this.panel = panel;
//		new CarsMaker(panel,this,key);
        start();
    }

    public ShloshaAvot(Ramzor ramzor, JPanel panel, int key, Event evChaol, Event evShabat, Event evToGreen, Event evToRed, Event evAckRed) {
        this.evWeek = evChaol;
        this.evSaturday = evShabat;
        this.evToGreen = evToGreen;
        this.evToRed = evToRed;
        this.evAckRed = evAckRed;

        this.ramzor = ramzor;
        this.panel = panel;

        new CarsMaker(panel, this, key);

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
                                //need to add evTimer
                                while (true) {
                                    if (evToGreen.arrivedEvent()) {
                                        evToGreen.waitEvent();
                                        ramzorState = RamzorState.ON_ORANGE_RED;
                                        setToOrangeAndRed();
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        turnOff();
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;
                            case ON_ORANGE_RED:
                                evTimer = new Event();
                                timer = new Timer(1000, evTimer);
                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.ON_GREEN;
                                        setToGreen();
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        turnOff();
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
                                        ramzorState = RamzorState.ON_FLICKER;
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        turnOff();
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;
                            case ON_FLICKER:
                                int count = 0;
                                inRamzorState = InRamzorState.ON_GREEN;

                                while (count < 4) {
                                    switch (inRamzorState) {
                                        case ON_GREEN:
                                            evTimer = new Event();
                                            timer = new Timer(700, evTimer);
                                            while (true) {
                                                if (evSaturday.arrivedEvent()) {
                                                    evSaturday.waitEvent();
                                                    dayState = dayState.ON_SATURDAY;
                                                    count = 4;
                                                    break;
                                                } else if (evTimer.arrivedEvent()) {
                                                    evTimer.waitEvent();
                                                    inRamzorState = InRamzorState.OFF_GREEN;
                                                    turnOff();
                                                    count++;
                                                    break;
                                                } else {
                                                    yield();
                                                }
                                            }
                                            break;

                                        case OFF_GREEN:
                                            evTimer = new Event();
                                            timer = new Timer(500, evTimer);
                                            while (true) {
                                                if (evSaturday.arrivedEvent()) {
                                                    evSaturday.waitEvent();
                                                    dayState = DayState.ON_SATURDAY;
                                                    count = 4;
                                                    break;
                                                } else if (evTimer.arrivedEvent()) {
                                                    evTimer.waitEvent();
                                                    inRamzorState = InRamzorState.ON_GREEN;
                                                    setToGreen();
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
                                ramzorState = ramzorState.ON_ORANGE;
                                setToOrange();
                                break;
                            case ON_ORANGE:
                                evTimer = new Event();
                                timer = new Timer(1000, evTimer);

                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.ON_RED;
                                        setToRed();
                                        evAckRed.sendEvent();
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        turnOff();
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
                    setToOrange();
                    ramzorState = RamzorState.ON_ORANGE;

                    while (dayState == DayState.ON_SATURDAY) {
                        switch (ramzorState) {
                            case ON_ORANGE:
                                evTimer = new Event();
                                timer = new Timer(1500, evTimer);
                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.OFF_ORANGE;
                                        turnOff();
                                        break;
                                    } else if (evWeek.arrivedEvent()) {
                                        evWeek.waitEvent();
                                        dayState = DayState.ON_WEEK;
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            case OFF_ORANGE:
                                evTimer = new Event();
                                timer = new Timer(1500, evTimer);
                                //the while is necassary for checking all the time if the event is come.
                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.ON_ORANGE;
                                        setToOrange();
                                        break;
                                    } else if (evWeek.arrivedEvent()) {
                                        evWeek.waitEvent();
                                        dayState = DayState.ON_WEEK;
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

                default:
                    break;
            }
        }
    }

    public void setLight(int place, Color color) {
        ramzor.colorLight[place - 1] = color;
        panel.repaint();
    }

    public boolean isStop() {
        return (ramzorState == RamzorState.ON_RED
                || ramzorState == RamzorState.ON_ORANGE
                || dayState == DayState.ON_SATURDAY);
    }

    private void turnOff() {
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.LIGHT_GRAY);
    }

    private void setToRed() {
        setLight(1, Color.RED);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.LIGHT_GRAY);
    }

    private void setToOrangeAndRed() {
        setLight(1, Color.RED);
        setLight(2, Color.ORANGE);
        setLight(3, Color.LIGHT_GRAY);
    }

    private void setToOrange() {
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.ORANGE);
        setLight(3, Color.LIGHT_GRAY);
    }

    private void setToGreen() {
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.GREEN);
    }
}
