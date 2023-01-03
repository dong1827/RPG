package MenuClass;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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

    protected String name;
    protected File playerF;
    protected LinkedHashMap<String, Equipment> equipments = new LinkedHashMap<>();
    protected Inventory inv = new Inventory();
    protected ArrayList<Integer> expData = new ArrayList<>();
    protected LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();
    protected LinkedHashMap<String, Integer> dmg = new LinkedHashMap<>();
    protected HashMap<String, Integer> afterEffect = new HashMap<>();
    protected int exp;
    protected int lvl;
    protected int lvlPoints;
    

    public Player() throws FileNotFoundException {
        Equipment empty = new Equipment("Empty");

        for (int i = 0; i <= 6; i++) {
            equipments.put(Equipment.getSlot(i), empty);
        }

        stats.put("Health", 100);
        stats.put("Strength", 5);
        stats.put("Dexterity", 1);
        stats.put("Intelligence", 1);
        stats.put("Armour", 0);
        stats.put("Elemental Resist", 0);
        stats.put("Luck", 0);
        
        // Make into static variables?
        File expF = new File("MenuClass\\PlayerData\\expdata.txt");
        Scanner expS = new Scanner(expF);
        while (expS.hasNextLine()) {
            expData.add(Integer.parseInt(expS.nextLine()));
        }
        expS.close();

        dmg.put(PHYS, stats.get("Strength"));
        dmg.put(FI, 0);
        dmg.put(CO, 0);
        dmg.put(LIGHT, 0);

        exp = 0;
        lvl = 1;
        lvlPoints = 0;
    }

    public Player(String name) throws IOException {
        this();
        this.name = name;
        playerF = new File("MenuClass\\PlayerData\\" + name + ".txt");
        try {
            // If the player doesn't exist
            if (playerF.createNewFile()) {
                // Consider some other ways of printing 
                System.out.println("new Player\nCreating new character...\n");
            }
            // If the player exists
            else {
                // If somehow the player data is empty
                Scanner playerS = new Scanner(playerF);
                System.out.println("\nWelcome back " + name + "\n");
                if (!playerS.hasNextLine()) {
                    playerS.close();
                    return;
                }
                // Getting the equipments
                String slot = "";
                String n = "";
                for (int i = 0; i < 7; i++) {
                    slot = playerS.nextLine();
                    n = playerS.nextLine();
                    Equipment e = new Equipment(n);
                    equipments.put(slot, e);
                }
                // Getting the inventory 
                int size = playerS.nextInt();
                playerS.nextLine();
                String type = "";
                InventoryObjects invOb = null;
                for (int i = 0; i < size; i++) {
                    n = playerS.nextLine();
                    type = playerS.nextLine();
                    if (type.equals("Equipment")) {
                        invOb = new Equipment(n);
                    }
                    else {
                        invOb = new Items(n);
                    }
                    inv.addObject(invOb);
                }

                // Getting player stats
                int stat = 0;
                for (int i = 0; i < 7; i++) {
                    n = playerS.nextLine();
                    stat = playerS.nextInt();
                    playerS.nextLine();
                    stats.put(n, stat);
                }

                // Getting player damage
                for (int i = 0; i < 4; i++) {
                    n = playerS.nextLine();
                    stat = playerS.nextInt();
                    playerS.nextLine();
                    dmg.put(n, stat);
                }
                
                // Getting player afterEffects
                size = playerS.nextInt();
                playerS.nextLine();
                for (int i = 0; i < size; i++) {
                    n = playerS.nextLine();
                    stat = playerS.nextInt();
                    playerS.nextLine();
                    afterEffect.put(n, stat);
                }

                // Getting exp, lvl and lvl points
                exp = playerS.nextInt();
                lvl = playerS.nextInt();
                lvlPoints = playerS.nextInt();

                playerS.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String save() {
        String msg = "";

        try {
            FileWriter f = new FileWriter(playerF);

            // Saving the equipments
            for (Map.Entry<String, Equipment> entry : equipments.entrySet()) {
                f.write(entry.getKey() + "\n");
                // Potentially add saving special stats here
                f.write(entry.getValue().getName() + "\n");
            }
            
            // Saving inventory
            ArrayList<InventoryObjects> inven = (ArrayList) inv.getIven();
            f.write(inven.size() + "\n");
            for (int i = 0; i < inven.size(); i++) {
                InventoryObjects ob = inven.get(i);
                f.write(ob.getName() + "\n");
                f.write(ob.getObType() + "\n");
            }
            
            // Saving player stats
            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                f.write(entry.getKey() + "\n");
                f.write(entry.getValue() + "\n");
            }

            // Saving player damages. Could potentially be read from equipments instead of saving?
            for (Map.Entry<String, Integer> entry : dmg.entrySet()) {
                f.write(entry.getKey() + "\n");
                f.write(entry.getValue() + "\n");
            }
            
            // Saving player afterEffects. Could potentially not save?
            f.write(afterEffect.size() + "\n");
            for (Map.Entry<String, Integer> entry : afterEffect.entrySet()) {
                f.write(entry.getKey() + "\n");
                f.write(entry.getValue() + "\n");
            }

            // Saving exp and etc.
            f.write(exp + " " + lvl + " " + lvlPoints);

            f.close();
            msg = "Data saved";
        }

        catch (Exception e) {
            msg = "Error in saving";
        }
        
        return msg;
    }

    public void save(String change) {

    }

    public int getStat(String stat) {
        return stats.get(stat);
    }

    public void changeStat(String stat, int value) {
        stats.put(stat, value);
    }


    // Return the dmg the player will do
    public LinkedHashMap<String, Integer> getDmg() {
        LinkedHashMap<String, Integer> playerDamage = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : dmg.entrySet()) {
            if (entry.getValue() > 0) {
                playerDamage.put(entry.getKey(), entry.getValue());
            }
        }

        return playerDamage;
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
            msg = "Empty inventory\n\n";
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
        String objClass = temp.getObType();

        if (objClass.equals("Equipment")) {
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
        else if (objClass.equals("Item")) {

        }

        // Calculating The effect of the object
        this.addSpecial(temp);
        // this.remove speical
        inv.removeIven(act);
        
        // Potentially change
        this.save();
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

        // Changing the damage of the player if the equipment is a weapon
        if (type.equals("Weapon")) {
            // Removing fist base damage
            dmg.put(PHYS, 0);
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
                dmg.put(PHYS, (dmg.get(PHYS) + scal * baseDamage.get(PHYS)));
            }
            if (dmgs.contains(FI)) {
                dmg.put(FI, (dmg.get(FI) + scal * baseDamage.get(FI)));
            }
            if (dmgs.contains(CO)) {
                dmg.put(CO, (dmg.get(CO) + scal * baseDamage.get(CO)));
            }
            if (dmgs.contains(LIGHT)) {
                dmg.put(LIGHT, dmg.get(LIGHT) + (scal * baseDamage.get(LIGHT)));
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

        // evaluating add on effects 
        itr = addOn.iterator();
        Map.Entry<String, Integer> entry;
        while (itr.hasNext()) {
            key = itr.next();
            entry = special.get(key);
            // if the add on effects is about damage
            if (key.contains("damage")) {
                dmg.put(entry.getKey(), dmg.get(entry.getKey()) + entry.getValue());
            }
            // if the add on effect is not about damage
            else {
                this.changeStat(entry.getKey(), this.getStat(entry.getKey()) + entry.getValue());
            }
            
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
        if (exp >= expData.get(lvl - 1)) {
            lvl++;
            lvlPoints += 3;
            msg += "Player leveled up! Gained 3 lvl points\n";
        }
        msg += "Current exp: " + exp + "/" + expData.get(lvl - 1);

        return msg;
    }

    //  Generating a string that contains the information about the player
    public String inspectSelf() {
        String msg = "";
        StringBuilder bld = new StringBuilder();
        
        // Generating stat string
        bld.append("Stats:\n");
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            bld.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }

        // Generating equipment string
        bld.append("\nEquipments:\n");
        for (Map.Entry<String, Equipment> entry : equipments.entrySet()) {
            bld.append(entry.getKey() + ": " + entry.getValue().getName() + "\n");
        }

        // Generating damage string
        bld.append("\nDamage:\n"); 
        for (Map.Entry<String, Integer> entry : dmg.entrySet()) {
            bld.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }

        // Generating exp string
        bld.append("\nlvl: " + lvl + " exp: " + exp + "/" + expData.get(lvl - 1) + " lvl points: " + lvlPoints + "\n");

        msg = bld.toString();
        return msg;
    }

    // Using stat points
    public String useStatPoint(int stat) {
        String msg = "";
        // If lvl points is bigger than 1
        if (lvlPoints < 1) {
            return "no lvl point available\n";
        }

        // If the stat the player chose is health, increase it by 10
        if (this.statString(stat).equals("Health")) {
            stats.put("Health", stats.get("Health") + 10);
            lvlPoints--;
            msg = "Health is increased by 10\n";
        }
        // else increase it by 1
        else {
            stats.put(statString(stat), stats.get(statString(stat)) + 1);
            lvlPoints--;
            msg = statString(stat) + " is increased by 1\n";
        }

        if (lvlPoints > 0 ) {
            msg += this.getlvlPointString();
        }
        else {
            msg += "no lvl points left\n\n";
        }
        
        return msg;
    }

    public int getlvlPoint() {
        return lvlPoints;
    }

    // Generates the string associates with spending lvl points
    public String getlvlPointString() {
        String msg = "";
        StringBuilder bld = new StringBuilder();
        bld.append("lvl points: " + this.getlvlPoint() + "\n\n");
        for (int i = 0; i < 5; i++) {
            bld.append(i + 1 + ". " + this.statString(i) + "\n");
            }
        bld.append("6. Back\n");
        msg = bld.toString();
        return msg;
    }

    // Getting which stat is associated with the number entered
    public String statString(int stat) {
        String msg = "";
        
        // Consier using enum
        /////////////////////
        ///////////////////// 
        switch (stat) {
            case 0: 
                msg = "Health";
                break;
            case 1:
                msg = "Strength";
                break;
            case 2:
                msg = "Dexterity";
                break;
            case 3:
                msg = "Intelligence";
                break;
            case 4: 
                msg = "Luck";
                break;
            default:
                msg = "Invalid stat";
        }

        return msg;
    }
}
