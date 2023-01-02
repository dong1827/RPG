package MenuClass;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import LocationData.*;

public class Menu {
    
    protected String focus;
    protected String des;
    protected String actions;
    protected Location location;
    protected ArrayList<String> allLocations = new ArrayList<>();
    protected Player p;
    protected Combat curCombat;

    public Menu() throws Exception {
        // Beginning in Base
        location = new Base();
        // Focus and description all set to base
        focus = "Location";
        des = location.getD();
        actions = location.getA();
        

        // Creating a list of available locations
        allLocations.add("Base");
        allLocations.add("Beginner Village");

        p = new Player();
        curCombat = new Combat();
    }

    public Menu(Player p) {
        location = new Base();
        focus = "Location";
        des = location.getD();
        actions = location.getA();

        allLocations.add("Base");
        allLocations.add("Beginner Village");

        this.p = p;
        curCombat = new Combat();

    }

    public String getF() {
        return focus;
    }

    public String getD() {
        return des;
    }

    public String getA() {
        return actions;
    }

    public String doAction(int act) throws Exception {
        String msg = "";
        act--;

        // Check which action should be performed using focus
        if (focus.equals("Location")) {
            msg = location.doAction(act);
        }
        else if (focus.equals("Travel")) {
            this.travel(allLocations.get(act));
            msg = this.getD() + "\n" + this.getA();

        }
        else if (focus.equals("Inventory")) {
            msg = p.getInventory();
            if (act == 0) {
                focus = "Inspect";
            }
            else if (act == 1) {
                focus = "Use";
            }
            else if (act == 2) {
                focus = "Remove";
            }
        }
        else if (focus.equals("Inspect")) {
            msg = p.inspect(act);
            focus = "Location";
        }
        else if (focus.equals("Use")) {
            msg = p.use(act);
            focus = "Location";
        }
        else if (focus.equals("Remove")) {
            msg = p.remove(act);
            focus = "Location";
        }
        

        // Check the side effect of the action
        if (msg.equals("Change Location")) {
            msg = this.getLocations();
            focus = "Travel";
        }
        // If the player found an enemy
        else if (msg.contains("Enemy found")) {
            System.out.println(msg);
            // Slight delay before the combat starts
            TimeUnit.SECONDS.sleep(2);
            Combat curCombat = new Combat(msg.substring(13), "Enemy");
            msg = curCombat.fight(p);
        }
        // ////////////////////////////////Improve////////////////////////////////////
        else if (msg.contains("Boss")) {
            Combat curCombat = new Combat(msg.substring(12), "Boss");
            curCombat.fight(p);
        }
        // If player is opening inventory
        else if (msg.equals("Open Inventory")) {
            focus = "Inventory";
            msg = Inventory.getActs();
        }
        

        // Check for side effect of combat
        if (msg.equals("Continue exploring")) {
            msg = this.getA();
        }
        else if (msg.equals("Player died")) {
            System.out.println("Player died... Returning to base");
            TimeUnit.SECONDS.sleep(1);
            this.travel("Base");
            msg = this.getA();
        }

        // Check for side effect of opening inventory 
        if (msg.contains("inventory")) {
            focus = "Location";
            msg += this.getA();
        }
        
        // Error Checking
        if (msg.equals("")) {
            msg = "Invalid action";
        }

        if (msg.equals("Invalid action")) {
            msg += "\n \n" + this.getA();
        }
    
        return msg;
    }

    public String getLocations() {
        String msg = "";

        // Generating location string
        for (int i = 0; i < allLocations.size(); i++) {
            msg = msg + (i + 1) + ". " + allLocations.get(i) + "\n";  
        }
        return msg;
    }

    public void travel(String name) throws Exception {
        // Changing the location
        if (name.equals("Base")) {
            location = new Base();
        }
        else {
            // Can't be new wild
            // Bug with Location
            location = new Wild(name);
            
        }

        // Setting new description and action
        des = location.getD();
        actions = location.getA();
        focus = "Location";

    }


    // Combat System
    public String combat() {
        String msg = "";
        return msg;
    }
}
