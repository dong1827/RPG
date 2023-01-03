package LocationData;
import MenuClass.Menu;

public class Base extends Location {
    
    public Base() {
        name = "Base";
        actions.add("Travel");
        actions.add("Inventory");
        actions.add("Inspect Self");
        actions.add("Use lvl Points");

        des = "Home sweet home.";

    }

    public String doAction(int i) {
        String msg = "";
        
        if (i == 0) {
            msg = "Change Location";
        }
        else if (i == 1) {
            msg = "Open Inventory";
        }
        else if (i == 2) {
            msg = "Inspect Self";
        }
        else if (i == 3) {
            msg = "Use lvl Points";
        }
        
        return msg;

        
    }


}
