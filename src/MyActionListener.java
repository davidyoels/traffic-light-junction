/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david salmon
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

public class MyActionListener implements ActionListener {

    Event evClickButt;
    Event evShabat;
    Event evChol;

    //shabat flag
    boolean shabat = false;

    public MyActionListener() {
    }

    public MyActionListener(Event evClickButt, Event evShabat, Event evChol) {
        this.evClickButt = evClickButt;
        this.evShabat = evShabat;
        this.evChol = evChol;
    }

    public void actionPerformed(ActionEvent e) {
        JRadioButton butt = (JRadioButton) e.getSource();
        int buttNumbet = Integer.parseInt(butt.getName());

        //check the kind of button
        if (buttNumbet < 16) {
            if (!evClickButt.arrivedEvent()) {
                evClickButt.sendEvent(butt);
                butt.setEnabled(false);
            } else {
                butt.setSelected(false);
            }

        } else {
            if (!shabat) {
                evShabat.sendEvent();
                butt.setSelected(true);
                shabat = true;
            } else {
                evChol.sendEvent();
                butt.setSelected(false);
                shabat = false;
            }
        }

//		System.out.println(butt.getName());
//		butt.setEnabled(false);
//		butt.setSelected(false);
    }

}
