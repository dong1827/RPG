package LocationData;
import MenuClass.Menu;

public class Base extends Location {
    
    public Base() {
        name = "Base";
        actions.add("Travel");
        actions.add("Crafting");
        actions.add("Inventory");
        actions.add("Use stat points");

        des = "Home sweet home.";

    }

    public String doAction(int i) {
        String msg = "";
        
        if (i == 0) {
            msg = "Change Location";
        }

        if (i == 2) {
            msg = "Open Inventory";
        }
        return msg;
    }


}
