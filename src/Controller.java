
import static java.lang.Thread.yield;
import java.util.stream.IntStream;
import javax.swing.JRadioButton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author david salmon
 */
public class Controller extends Thread {

    enum DayState {
        ON_WEEK, ON_SATURDAY
    }

    enum RamzorState {
        GROUP_1_IN_GREEN, GROUP_2_IN_GREEN, GROUP_3_IN_GREEN,
        Verify_1, Verify_2, Verify_3,
        GROUP_1_IN_RED, GROUP_2_IN_RED, GROUP_3_IN_RED
    }

    DayState dayState;
    RamzorState ramzorState;
    boolean buttonClicked = false;
    //events from/to ramzorim
    Event[] evWeeks;
    Event[] evSaturdays;
    Event[] evToGreen;
    Event[] evToRed;
    Event[] evAckReds;

    Event evTimer;

    //general events
    Event evAckRed;
    Event evSaturday;
    Event evWeek;
    Event evClickButt;

    Timer timer;

    //group of ramzorim
    int[] group1;
    int[] group2;
    int[] group3;

    int[] group1_2;
    int[] groupNOT1_2;
    int[] group1_3;
    int[] group3_1;
    int[] groupNOT1_3;
    int[] group2_1;
    int[] group4;

    public Controller(Event[] evChols, Event[] evShabats, Event[] evToGreen, Event[] evToRed, Event[] evAckReds, Event evShabat, Event evChol, Event evClickButt) {
        this.evWeeks = evChols;
        this.evSaturdays = evShabats;
        this.evToGreen = evToGreen;
        this.evToRed = evToRed;
        this.evAckReds = evAckReds;
        this.evSaturday = evShabat;
        this.evWeek = evChol;
        this.evClickButt = evClickButt;
        initCtrl();
        start();
    }

    void initCtrl() {
        //init goups
        group1 = new int[]{1, 2, 4, 5, 6, 7, 12, 13};
        group2 = new int[]{0, 6, 7, 9, 10, 12, 13};
        group3 = new int[]{2, 3, 4, 5, 8, 11, 14, 15};

        group1_2 = new int[]{1, 2, 4, 5};
        group2_1 = new int[]{0, 9, 10};
 
        group1_3 = new int[]{1, 6, 7, 12, 13};
        group3_1 = new int[]{3, 8, 11, 14, 15};

        groupNOT1_3 = new int[]{2, 4, 5};
        groupNOT1_2 = new int[]{6, 7, 12, 13};

        group4 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    }

    public void run() {
        dayState = DayState.ON_WEEK;
        while (true) {
            switch (dayState) {
                case ON_WEEK:

                    checkAckRed(group4);
                    if (evAckRed.arrivedEvent()) {
                        evAckRed.waitEvent();
                        ramzorState = RamzorState.GROUP_1_IN_GREEN;
                        groupToGreen(group1);
                    } else {
                        break;
                    }

                    while (dayState == DayState.ON_WEEK) {
                        switch (ramzorState) {
                            case GROUP_1_IN_GREEN:
                                evTimer = new Event();
                                timer = new Timer(7000, evTimer);

                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.Verify_1;
                                        // groupToRed(group1_2);
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        allToSabat();
                                        break;
                                    } else if (evClickButt.arrivedEvent()) {
                                        JRadioButton butt = (JRadioButton) evClickButt.waitEvent();
                                        int buttNum = Integer.parseInt(butt.getName());

                                        if (IntStream.of(group3).anyMatch(x -> x == buttNum)) {
                                            if (!IntStream.of(group1).anyMatch(x -> x == buttNum)) {
                                                buttonClicked = true;
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                                ramzorState = RamzorState.Verify_1;
                                                break;
                                            } else {
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                            }
                                        } else if (IntStream.of(group2).anyMatch(x -> x == buttNum)) {
                                            if (!IntStream.of(group1).anyMatch(x -> x == buttNum)) {
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                                ramzorState = RamzorState.Verify_1;
                                                break;
                                            } else {
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                            }
                                        } else if (IntStream.of(group1).anyMatch(x -> x == buttNum)) {
                                            butt.setSelected(false);
                                            butt.setEnabled(true);
                                        }
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            case Verify_1:
                                evTimer = new Event();
                                timer = new Timer(222, evTimer);
                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        groupToRed(group1_2);
                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.GROUP_1_IN_RED;
                                        break;
                                    } else if (buttonClicked) {
                                        groupToRed(group1_3);
                                        ramzorState = RamzorState.GROUP_1_IN_RED;
                                        buttonClicked = false;
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        allToSabat();
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            case GROUP_1_IN_RED:

                                evTimer = new Event();
                                timer = new Timer(1700, evTimer);
                                while (true) {

                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        checkAckRed(group1_2);
                                        if (evAckRed.arrivedEvent()) {
                                            evAckRed.waitEvent();
                                            ramzorState = RamzorState.GROUP_2_IN_GREEN;
                                            groupToGreen(group2_1);
                                            break;
                                        }

                                        checkAckRed(group1_3);
                                        if (evAckRed.arrivedEvent()) {
                                            evAckRed.waitEvent();
                                            ramzorState = RamzorState.GROUP_3_IN_GREEN;
                                            groupToGreen(group3_1);
                                            break;
                                        }
                                        break;
                                    }

                                    if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        allToSabat();
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            case GROUP_2_IN_GREEN:
                                evTimer = new Event();
                                timer = new Timer(7000, evTimer);

                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.Verify_2;
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        allToSabat();
                                        break;
                                    } else if (evClickButt.arrivedEvent()) {
                                        JRadioButton butt = (JRadioButton) evClickButt.waitEvent();
                                        int buttNum = Integer.parseInt(butt.getName());

                                        if (IntStream.of(group1).anyMatch(x -> x == buttNum)) {
                                            if (!IntStream.of(group2).anyMatch(x -> x == buttNum)) {
                                                buttonClicked = true;
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                                ramzorState = RamzorState.Verify_2;
                                                break;
                                            } else {
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                            }
                                        } else if (IntStream.of(group3).anyMatch(x -> x == buttNum)) {
                                            if (!IntStream.of(group2).anyMatch(x -> x == buttNum)) {
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                                ramzorState = RamzorState.Verify_2;
                                                break;
                                            } else {
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                            }
                                        } else if (IntStream.of(group2).anyMatch(x -> x == buttNum)) {
                                            butt.setSelected(false);
                                            butt.setEnabled(true);
                                        }
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            case Verify_2:
                                evTimer = new Event();
                                timer = new Timer(222, evTimer);

                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        groupToRed(group2);
                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.GROUP_2_IN_RED;
                                        break;

                                    } else if (buttonClicked) {
                                        groupToRed(group2_1);
                                        ramzorState = RamzorState.GROUP_2_IN_RED;
                                        buttonClicked = false;
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        allToSabat();
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            case GROUP_2_IN_RED:

                                evTimer = new Event();
                                timer = new Timer(1700, evTimer);
                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        checkAckRed(group2);
                                        if (evAckRed.arrivedEvent()) {
                                            evAckRed.waitEvent();
                                            ramzorState = RamzorState.GROUP_3_IN_GREEN;
                                            groupToGreen(group3);
                                            break;
                                        }

                                        checkAckRed(group2_1);
                                        if (evAckRed.arrivedEvent()) {
                                            evAckRed.waitEvent();
                                            ramzorState = RamzorState.GROUP_1_IN_GREEN;
                                            groupToGreen(group1_2);
                                            break;
                                        }
                                        break;
                                    }

                                    if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        allToSabat();
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            case GROUP_3_IN_GREEN:
                                evTimer = new Event();
                                timer = new Timer(7000, evTimer);

                                while (true) {
                                    if (evTimer.arrivedEvent()) {

                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.Verify_3;
                                        break;

                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        allToSabat();
                                        break;
                                    } else if (evClickButt.arrivedEvent()) {
                                        JRadioButton butt = (JRadioButton) evClickButt.waitEvent();
                                        int buttNum = Integer.parseInt(butt.getName());

                                        if (IntStream.of(group2).anyMatch(x -> x == buttNum)) {
                                            if (!IntStream.of(group3).anyMatch(x -> x == buttNum)) {
                                                buttonClicked = true;
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                                ramzorState = RamzorState.Verify_3;
                                                break;
                                            } else {
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                            }
                                        } else if (IntStream.of(group1).anyMatch(x -> x == buttNum)) {
                                            if (!IntStream.of(group3).anyMatch(x -> x == buttNum)) {
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                                ramzorState = RamzorState.Verify_3;
                                                break;
                                            } else {
                                                butt.setSelected(false);
                                                butt.setEnabled(true);
                                            }
                                        } else if (IntStream.of(group3).anyMatch(x -> x == buttNum)) {
                                            butt.setSelected(false);
                                            butt.setEnabled(true);
                                        }
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            case Verify_3:
                                evTimer = new Event();
                                timer = new Timer(222, evTimer);

                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        groupToRed(group3_1);
                                        evTimer.waitEvent();
                                        ramzorState = RamzorState.GROUP_3_IN_RED;
                                        break;

                                    } else if (buttonClicked) {
                                        groupToRed(group3);
                                        ramzorState = RamzorState.GROUP_3_IN_RED;
                                        buttonClicked = false;
                                        break;
                                    } else if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        allToSabat();
                                        break;
                                    } else {
                                        yield();
                                    }
                                }
                                break;

                            case GROUP_3_IN_RED:
                                evTimer = new Event();
                                timer = new Timer(1700, evTimer);
                                while (true) {
                                    if (evTimer.arrivedEvent()) {
                                        evTimer.waitEvent();
                                        checkAckRed(group3_1);
                                        if (evAckRed.arrivedEvent()) {
                                            evAckRed.waitEvent();
                                            groupToGreen(group1_3);
                                            ramzorState = RamzorState.GROUP_1_IN_GREEN;
                                            break;
                                        }

                                        checkAckRed(group3);
                                        if (evAckRed.arrivedEvent()) {
                                            evAckRed.waitEvent();
                                            ramzorState = RamzorState.GROUP_2_IN_GREEN;
                                            groupToGreen(group2);
                                            break;
                                        }
                                        break;
                                    }

                                    if (evSaturday.arrivedEvent()) {
                                        evSaturday.waitEvent();
                                        dayState = DayState.ON_SATURDAY;
                                        allToSabat();
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
                    evWeek.waitEvent();
                    allToChol();
                    dayState = DayState.ON_WEEK;
                    break;

                default:
                    break;
            }
        }
    }

    //--------------------------function------------------------
    private void checkAckRed(int[] group) {
        evAckRed = new Event();

        //check if all ramzors by at red
        for (int i : group) {
            if (!evAckReds[i].arrivedEvent()) {
                return;
            }
        }

        //clear events
        for (int i : group) {
            evAckReds[i].waitEvent();
        }

        //send evAckRed event
        evAckRed.sendEvent();
        return;
    }

    private void allToSabat() {
        for (Event ev : evSaturdays) {
            ev.sendEvent();
        }
    }

    private void allToChol() {
        for (Event ev : evWeeks) {
            ev.sendEvent();
        }
    }

    void groupToRed(int[] group) {
        for (int i : group) {
            evToRed[i].sendEvent();
        }
    }

    void groupToGreen(int[] group) {
        for (int i : group) {
            evToGreen[i].sendEvent();
        }
    }
}
