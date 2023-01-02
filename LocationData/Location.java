package LocationData;

import java.util.ArrayList;

public abstract class Location {
    protected String name;
    protected ArrayList<String> actions = new ArrayList<>();
    protected String des;

    public String getN() {
        return name;
    }

    public String getA() {
        String acts = "";
        // Printing out actions in the format of 1. xxx 2. xxx ...
        for (int i = 0; i < actions.size(); i++) {
            acts = acts + (i + 1) + ". " + actions.get(i) + "\n";
        }
        return acts;
    }

    public String getD() {
        return des;
    }

    // Does nothing, exists for Base and Wild class
    public String doAction(int act) {
        String msg = "";
        return msg;
    }

}
