package MenuClass;

import java.util.ArrayList;
import java.util.List;

import InventoryData.InventoryObjects;

public class Inventory {
    protected static String acts = "1. Inspect Items\n2. Use\n3. Remove"; 
    protected ArrayList<InventoryObjects> inven;

    public Inventory() {
        inven = new ArrayList<>();
    }

    public void addObject(InventoryObjects ob) {
        inven.add(ob);
    }

    public List<InventoryObjects> getIven() {
        return inven;
    }

    public void removeIven(int i) {
        inven.remove(i);
    }

    // Getting the actions
    public static String getActs() {
        return acts;
    }

}
