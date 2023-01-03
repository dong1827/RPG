package EnemyData;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class Enemy {
    protected String name;
    protected String des;
    protected TreeMap<String, Integer> stats = new TreeMap<String, Integer>();
    protected LinkedHashMap<String, Integer> drops = new LinkedHashMap<>();
    protected int roll;

    public Enemy(String n) throws Exception {
        name = n;
        File eneF = new File("EnemyData\\Enemies\\" + name + ".txt");
        Scanner eneS = new Scanner(eneF);

        des = eneS.nextLine();
        stats.put("Health", eneS.nextInt());
        stats.put("Pdmg", eneS.nextInt());
        stats.put("Edmg", eneS.nextInt());
        stats.put("Physical", eneS.nextInt());
        stats.put("Fire", eneS.nextInt());
        stats.put("Cold", eneS.nextInt());
        stats.put("Lightning", eneS.nextInt());
        stats.put("Experience", eneS.nextInt());
        stats.put("In Range", eneS.nextInt());
        stats.put("Roll", eneS.nextInt());
        
        int totDrop = eneS.nextInt();
        for (int i = 0; i < totDrop; i++) {
            eneS.nextLine();
            drops.put(eneS.nextLine(), eneS.nextInt());
        }

        eneS.close();
    }

    public String getName() {
        return name;
    }

    public int getStat(String stat) {
        return stats.get(stat);
    }

    

    public LinkedHashMap<String, Integer> getloot() {
        return this.drops;
    }

}
