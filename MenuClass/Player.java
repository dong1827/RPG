package MenuClass;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


import InventoryData.*;

public class Player {
    protected static final String PHYS = "Physical";
    protected static final String FI = "Fire";
    protected static final String CO = "Cold";
    protected static final String LIGHT = "Lightning";

    protected LinkedHashMap<String, Equipment> equipments = new LinkedHashMap<>();
    protected Inventory inv = new Inventory();
    protected int exp;
    protected ArrayList<Integer> expData = new ArrayList<>();
    protected LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();
    protected int lvl;
    protected int lvlPoints;
    protected LinkedHashMap<String, Integer> dmg = new LinkedHashMap<>();
    protected HashMap<String, Integer> afterEffect = new HashMap<>();
    

    public Player() throws FileNotFoundException {
        Equipment empty = new Equipment("Empty");

        for (int i = 0; i <= 7; i++) {
            equipments.put(Equipment.getSlot(i), empty);
        }
        exp = 0;
        stats.put("Health", 100);
        stats.put("Strength", 5);
        stats.put("Dexterity", 1);
        stats.put("Intelligence", 1);
        stats.put("Armour", 0);
        stats.put("Elemental Resist", 0);
        stats.put("Luck", 0);
        lvl = 1;
        lvlPoints = 0;
        // Make into static variables?
        File expF = new File("MenuClass\\expdata.txt");
        Scanner expS = new Scanner(expF);
        while (expS.hasNextLine()) {
            expData.add(Integer.parseInt(expS.nextLine()));
        }
        expS.close();

        dmg.put(PHYS, stats.get("Strength"));
    }

    public Player(LinkedHashMap<String, Equipment> equip, Inventory inven, int experi, LinkedHashMap<String, Integer> st, int level) {
        equipments = equip;
        inv = inven;
        exp = experi;
        stats = st;
        lvl = level;

    }

    public int getStat(String stat) {
        return stats.get(stat);
    }

    public void changeStat(String stat, int value) {
        stats.put(stat, value);
    }


    // Return the dmg the player will do
    public LinkedHashMap<String, Integer> getDmg() {
        return dmg;
    }

    public void addLoot(String name) {
        // Checking the type of the loot
        final String eq = "equip";
        final String s = "skills";
        final String it = "items";

        String type = "null";
        String[] types = {eq, s, it};
        int i = 0;
        InventoryObjects loot = null;

        while (type.equals("null")) {
            try {
                if (types[i].equals(eq)) {
                    loot = new Equipment(name);
                    type = eq;
                }
                else if (types[i].equals(it)) {
                    loot = new Items(name);
                    type = it;
                }
                else {
                    i++;
                }
            }
            catch (FileNotFoundException e){
                i++;
                if (i > 2) {
                    break;
                }
            }
        }

        inv.addObject(loot);

    }

    // Get what's in the player's inventory
    public String getInventory() {
        String msg;
        StringBuilder bld = new StringBuilder();
        ArrayList<InventoryObjects> lst = (ArrayList<InventoryObjects>) inv.getIven();

        if (lst.isEmpty()) {
            msg = "Empty inventory \n";
        }
        else {
            for (int i = 0; i < lst.size(); i++) {
                bld.append((i+1) + ". " + lst.get(i).getName() + "\n");
            }
            
            msg = "Inventory: \n" + bld.toString();
        }
        
        return msg;

    }

    // Get what's in the player's equipment slot
    public String getEquip() {
        String msg;
        StringBuilder bld = new StringBuilder();
        
        for (Map.Entry<String, Equipment> entry : equipments.entrySet()) {
            bld.append(entry.getKey() + ": " + entry.getValue().getName() + "\n");
        }

        msg = "Equipments \n" + bld.toString();

        return msg;
    }

    // Using the object in player's inventory
    public String use(int act) {
        String msg = "";

        ArrayList<InventoryObjects> lst = (ArrayList<InventoryObjects>) inv.getIven();
        InventoryObjects temp = lst.get(act);
        String objClass = temp.getClass().toString();

        if (objClass.contains("Equip")) {
            String type = temp.getType();
            Equipment eq = (Equipment) temp;
            if (equipments.get(type).getName().equals("Empty")) {
                equipments.put(type, eq);
            }
            else {
                InventoryObjects equiped = equipments.get(type);
                equipments.put(type, eq);
                inv.addObject(equiped);
                // remove special
            }
            msg = "Equipped " + eq.getName() + " in " + type + " slot. \nClosing inventory...\n\n";
        }
        else if (objClass.contains("Item")) {

        }

        // Calculating The effect of the object
        this.addSpecial(temp);
        inv.removeIven(act);
        

        return msg;
    }

    // Inspecting the object in inventory 
    public String inspect(int act) {
        String msg = "";

        ArrayList<InventoryObjects> lst = (ArrayList<InventoryObjects>) inv.getIven();
        InventoryObjects cur = lst.get(act);

        msg = cur.getDes() + "\n";
        StringBuilder bld = new StringBuilder();
        LinkedHashMap<String, Map.Entry<String, Integer>> special = cur.getSpecial();
        
        // Initializing damage descriptions
        if (cur.getType().equals("Weapon")) {
            Equipment e = (Equipment) cur;
            LinkedHashMap<String, Integer> dmgs = e.getDamage();
            int i = 0;
            for (Map.Entry<String, Integer> entry : dmgs.entrySet()) {
                // the first line of the weapon base damage is the scaling
                if (i == 0) {
                    bld.append(entry.getKey() + " increases the base damage of the weapon by " + entry.getValue() + " times of its value\n");
                }
                else {
                    bld.append(entry.getKey() + " base damage: " + entry.getValue() + "\n");
                }
                i++;
            } 
        }

        for (String s : special.keySet()) {
            bld.append(s + "\n");
        }

        msg += bld.toString() + "\n\nClosing inventory...\n\n";
        
        return msg;
    }

    // Remove an object from inventroy
    public String remove(int act) {
        String msg = "";
        InventoryObjects cur = inv.getIven().get(act);
        inv.removeIven(act);

        msg = "Removed " + cur.getType() + " " + cur.getName() + " from the inventory\n";
        return msg;
    }

    // Adding on special effect
    public void addSpecial(InventoryObjects ob) {

        String type = ob.getType();
        LinkedHashMap<String, Map.Entry<String, Integer>> special = ob.getSpecial();

        if (type.equals("Weapon")) {
            // Changing the damage of the player
            int scal = 1;
            Equipment weapon = (Equipment) ob;
            LinkedHashMap<String, Integer> baseDamage = weapon.getDamage(); 
            for (Map.Entry<String, Integer> first : baseDamage.entrySet()) {
                scal = this.getStat(first.getKey()) * first.getValue();
                break;
            }

            Set<String> dmgs = baseDamage.keySet();
            // Consider using enum
            ////////////////////////
            if (dmgs.contains(PHYS)) {
                dmg.put(PHYS, scal * baseDamage.get(PHYS));
            }
            if (dmgs.contains(FI)) {
                dmg.put(FI, scal * baseDamage.get(FI));
            }
            if (dmgs.contains(CO)) {
                dmg.put(CO, scal * baseDamage.get(CO));
            }
            if (dmgs.contains(LIGHT)) {
                dmg.put(LIGHT, scal * baseDamage.get(LIGHT));
            }

        }

        // Checking the type of special stats
        Set<String> after = new HashSet<>();
        Set<String> addOn = new HashSet<>();
        Set<String> convert = new HashSet<>();
        Set<String> other = new HashSet<>();
        Iterator<String> itr = special.keySet().iterator();
        String key = " ";

        while (itr.hasNext()) {
            key = itr.next();
            if (key.contains("after")) {
                after.add(key);
            }
            else if (key.contains("Add")) {
                addOn.add(key);
            }
            else if (key.contains("Convert")) {
                convert.add(key);
            }
            else {
                other.add(key);
            }
        }

        itr = addOn.iterator();
        Map.Entry<String, Integer> entry;
        while (itr.hasNext()) {
            key = itr.next();
            entry = special.get(key);
            this.changeStat(entry.getKey(), this.getStat(entry.getKey()) + entry.getValue());
        }

        itr = after.iterator();
        while (itr.hasNext()) {
            key = itr.next();
            entry = special .get(key);
            afterEffect.put(entry.getKey(), entry.getValue());
        }
    }

    public HashMap<String, Integer> getAfterEffect() {
        return afterEffect;
    }

    // Adding exp and calculate if the player lvled up 
    public String addExp(int e) {
        String msg = "Player gained " + e + " experiences.\n";
        exp += e;
        if (exp > expData.get(lvl - 1)) {
            lvl++;
            lvlPoints += 5;
            msg += "Player leveled up!\n";
        }
        msg += "Current exp: " + exp + "/" + expData.get(lvl - 1);

        return msg;
    }

    public String allocatePoints(int i) {
        String msg = "";
        
        return msg;
    }

}
