package InventoryData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Items implements InventoryObjects {
    protected String type;
    protected String name;
    protected String des;
    protected LinkedHashMap<String, Map.Entry<String, Integer>> special = new LinkedHashMap<>();

    public Items(String name) throws FileNotFoundException {
        this.name = name;
        File equipF = new File("InventoryData\\" + name + ".txt");
        Scanner equipS = new Scanner(equipF);

        equipS.close();
    }

    public String getName() {
        return name;
    }
    
    public String getDes() {
        return des;
    }

    public String getType() {
        return type;
    }

    public LinkedHashMap<String, Map.Entry<String, Integer>> getSpecial() {
        return special;
    }

    public String getObType() {
        return "Item";
    }
}
