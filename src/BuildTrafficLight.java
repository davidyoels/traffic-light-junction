import javax.swing.JRadioButton;

/*
 * Created on Mimuna 5767  upDate on Addar 5772 
 */
 /**
 * https://ufile.io/f7dhv
 * @author david salmon
 */
public class BuildTrafficLight {

    public static void main(String[] args) {
        init();

    }

    public static void init() {
        final int numOfLights = 4 + 12 + 1;
        Ramzor ramzorim[] = new Ramzor[numOfLights];
        ramzorim[0] = new Ramzor(3, 40, 430, 110, 472, 110, 514, 110);
        ramzorim[1] = new Ramzor(3, 40, 450, 310, 450, 352, 450, 394);
        ramzorim[2] = new Ramzor(3, 40, 310, 630, 280, 605, 250, 580);
        ramzorim[3] = new Ramzor(3, 40, 350, 350, 308, 350, 266, 350);
        ramzorim[4] = new Ramzor(2, 20, 600, 18, 600, 40);
        ramzorim[5] = new Ramzor(2, 20, 600, 227, 600, 205);
        ramzorim[6] = new Ramzor(2, 20, 600, 255, 600, 277);
        ramzorim[7] = new Ramzor(2, 20, 600, 455, 600, 433);
        ramzorim[8] = new Ramzor(2, 20, 575, 475, 553, 475);
        ramzorim[9] = new Ramzor(2, 20, 140, 608, 150, 590);
        ramzorim[10] = new Ramzor(2, 20, 205, 475, 193, 490);
        ramzorim[11] = new Ramzor(2, 20, 230, 475, 250, 475);
        ramzorim[12] = new Ramzor(2, 20, 200, 453, 200, 433);
        ramzorim[13] = new Ramzor(2, 20, 200, 255, 200, 277);
        ramzorim[14] = new Ramzor(2, 20, 200, 227, 200, 205);
        ramzorim[15] = new Ramzor(2, 20, 200, 18, 200, 40);
        ramzorim[16] = new Ramzor(1, 30, 555, 645);

        TrafficLightFrame tlf = new TrafficLightFrame(" תשע''ב installation of traffic lights", ramzorim);

        //init events
        Event[] evChols = new Event[16];
        for (int i = 0; i < 16; i++) {
            evChols[i] = new Event();
        }

        Event[] evShabats = new Event[16];
        for (int i = 0; i < 16; i++) {
            evShabats[i] = new Event();
        }
        Event[] evToGreen = new Event[16];
        for (int i = 0; i < 16; i++) {
            evToGreen[i] = new Event();
        }
        Event[] evToRed = new Event[16];
        for (int i = 0; i < 16; i++) {
            evToRed[i] = new Event();
        }
        Event[] evAckReds = new Event[16];
        for (int i = 0; i < 16; i++) {
            evAckReds[i] = new Event();
        }

        //event for 12 button
        Event evClickButt = new Event();

        //event for general shabat button
        Event evShabat = new Event();

        //event for general chol button
        Event evChol = new Event();

        ShloshaAvot[] shloshaAvot = new ShloshaAvot[4];
        for (int i = 0; i < 4; i++) {
            shloshaAvot[i] = new ShloshaAvot(
                    ramzorim[i],
                    tlf.myPanel,
                    i + 1,
                    evChols[i],
                    evShabats[i],
                    evToGreen[i],
                    evToRed[i],
                    evAckReds[i]);
        }

        //init ShneyLuchot
        ShneyLuchot[] shneyLuchot = new ShneyLuchot[12];
        for (int i = 0; i < 12; i++) {
            shneyLuchot[i] = new ShneyLuchot(
                    ramzorim[i + 4],
                    tlf.myPanel,
                    evChols[i + 4],
                    evShabats[i + 4],
                    evToGreen[i + 4],
                    evToRed[i + 4],
                    evAckReds[i + 4]
            );
        }
        //init Echad
        Echad echad = new Echad(ramzorim[16], tlf.myPanel);

//        new ShloshaAvot(ramzorim[0], tlf.myPanel, 1);
//        new ShloshaAvot(ramzorim[1], tlf.myPanel, 2);
//        new ShloshaAvot(ramzorim[2], tlf.myPanel, 3);
//        new ShloshaAvot(ramzorim[3], tlf.myPanel, 4);
//
//        new ShneyLuchot(ramzorim[4], tlf.myPanel);
//        new ShneyLuchot(ramzorim[5], tlf.myPanel);
//        new ShneyLuchot(ramzorim[9], tlf.myPanel);
//        new ShneyLuchot(ramzorim[10], tlf.myPanel);
        MyActionListener myListener = new MyActionListener(evClickButt, evShabat, evChol);

        JRadioButton butt[] = new JRadioButton[13];

        for (int i = 0; i < butt.length - 1; i++) {
            butt[i] = new JRadioButton();
            butt[i].setName(Integer.toString(i + 4));
            butt[i].setOpaque(false);
            butt[i].addActionListener(myListener);
            tlf.myPanel.add(butt[i]);
        }
        butt[0].setBounds(620, 30, 18, 18);
        butt[1].setBounds(620, 218, 18, 18);
        butt[2].setBounds(620, 267, 18, 18);
        butt[3].setBounds(620, 447, 18, 18);
        butt[4].setBounds(566, 495, 18, 18);
        butt[5].setBounds(162, 608, 18, 18);
        butt[6].setBounds(213, 495, 18, 18);
        butt[7].setBounds(240, 457, 18, 18);
        butt[8].setBounds(220, 443, 18, 18);
        butt[9].setBounds(220, 267, 18, 18);
        butt[10].setBounds(220, 218, 18, 18);
        butt[11].setBounds(220, 30, 18, 18);

        butt[12] = new JRadioButton();
        butt[12].setName(Integer.toString(16));
        butt[12].setBounds(50, 30, 55, 20);
        butt[12].setText("שבת");
        butt[12].setOpaque(false);
        butt[12].addActionListener(myListener);
        tlf.myPanel.add(butt[12]);

//        the controler
        Controller ctrl = new Controller(
                evChols,
                evShabats,
                evToGreen,
                evToRed,
                evAckReds,
                evShabat,
                evChol,
                evClickButt);

    }

}
