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

class Echad extends Thread {

    Ramzor ramzor;
    JPanel panel;

    enum Flicker {
        ON_ORANGE, OFF_ORANGE
    }
    Flicker flicker;

    Event evTimer;

    Timer timer;

    public Echad(Ramzor ramzor, JPanel panel) {
        this.ramzor = ramzor;
        this.panel = panel;
        start();
    }

    public void run() {
        flicker = Flicker.ON_ORANGE;
        setToOrange();
        while (true) {
            switch (flicker) {
                case ON_ORANGE:
                    evTimer = new Event();
                    timer = new Timer(500, evTimer);
                    evTimer.waitEvent();
                    setOffOrange();
                    flicker = Flicker.OFF_ORANGE;
                    break;
                case OFF_ORANGE:
                    evTimer = new Event();
                    timer = new Timer(500, evTimer);
                    evTimer.waitEvent();
                    setToOrange();
                    flicker = Flicker.ON_ORANGE;
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

    private void setToOrange() {
        setLight(1, Color.orange);
    }

    private void setOffOrange() {
        setLight(1, Color.GRAY);
    }
}
