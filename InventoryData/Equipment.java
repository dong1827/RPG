package InventoryData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Equipment implements InventoryObjects {
    protected String name;
    protected String des;
    protected String type;
    protected LinkedHashMap<String, Integer> damage = new LinkedHashMap<>();
    protected LinkedHashMap<String, Map.Entry<String, Integer>> special = new LinkedHashMap<>(); 

    // Creating equipment
    public Equipment(String name) throws FileNotFoundException {

        this.name = name;
        if (name.equals("Empty")) {
            des = "Empty";
            return;
        }
        // Finds the equipment in the data file and initialize the equipment
        File equipF = new File("InventoryData\\Equipments\\" + name + ".txt");
        Scanner equipS = new Scanner(equipF);
        
        StringBuilder bld = new StringBuilder();
        bld.append(equipS.nextLine());
        String temp = equipS.nextLine();

        while (!temp.equals("/")) {
            bld.append("\n" + temp);
            temp = equipS.nextLine();
        }

        des = bld.toString() + "\n";
        
        type = equipS.nextLine();
        temp = equipS.nextLine();
        while (!temp.equals("/")) {
            // Something is wrong with the data
            //////////////////////////////////////////
            damage.put(temp, equipS.nextInt());
            equipS.nextLine();
            temp = equipS.nextLine();
        }

        temp = equipS.nextLine();
        Map.Entry<String, Integer> entry;
        while (!temp.equals("/")) {
            entry = new AbstractMap.SimpleEntry<>(equipS.nextLine(), equipS.nextInt());
            special.put(temp, entry);
            equipS.nextLine();
            temp = equipS.nextLine();
        }

        
        equipS.close();
    }

    public String getName() {
        return name;
    }

    // Return the slot associated with the number
    public static String getSlot(int slot) {
        String strSlot;
        switch (slot) {
            case 0: 
                strSlot = "Head";
                break;
            case 1:
                strSlot = "Chest";
                break;
            case 2:
                strSlot = "Arms";
                break;
            case 3:
                strSlot = "Legs";
                break;
            case 4: 
                strSlot = "Boots";
                break;
            case 5: 
                strSlot = "Left Ring";
                break;
            case 6:
                strSlot = "Right Ring";
                break;
            case 7: 
                strSlot = "Weapon";
                break;
            default:
                strSlot = "Null";
        }

        return strSlot;
    }

    public String getDes() {
        return des;
    }

    public String getType() {
        return type;
    }

    public LinkedHashMap<String, Integer> getDamage() {
        return damage;
    }

    // Return the speical effect of the item
    public LinkedHashMap<String, Map.Entry<String, Integer>> getSpecial() {
        return special;
    }

}
