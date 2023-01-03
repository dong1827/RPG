package LocationData;

import java.io.File;
import java.util.*;

import EnemyData.Boss;
import EnemyData.Enemy;

public class Wild extends Location {
    
    protected Integer BossDis = 0;
    protected LinkedHashMap<String, Integer> Enemies = new LinkedHashMap<String, Integer>();
    protected int discoveryRate;

    public Wild(String name) throws Exception {
        // Initializing the wild
        this.name = name;
        actions.add("Walk around");
        actions.add("Travel");
        actions.add("Inventory");
        File LocF = new File("LocationData\\WildData\\" + name + ".txt");
        Scanner LocS = new Scanner(LocF);

        des = LocS.nextLine();
        discoveryRate = LocS.nextInt();

        // Creating list of encounerable enemies in the area
        
        int numOfE = LocS.nextInt();
        LocS.useDelimiter(",\\s*");
        LocS.next();
        String Temp;
        String x;
        for (int i = 0; i < numOfE; i++) {
            Temp = LocS.next();
            x = LocS.next();
            Enemies.put(Temp, Integer.parseInt(x));
        }

        // Creating Boss in the area
        LocS.nextLine();
        Temp = "Boss" + LocS.next();
        Enemies.put(Temp, LocS.nextInt());
        BossDis = LocS.nextInt();

        LocS.close();
        
    }

    public String doAction(int act) {
        String msg = "";
        
        if (act == 0) {
            msg = this.Walk();
        }
        else if (act == 1) {
            msg = "Change Location";
        }
        else if (act == 2) {
            msg = "Open Inventory";
        }
        else if (act == 3) {
            if (BossDis == 0) {
                msg = "Challenge Boss";
            }
            else {
                msg = "Invalid action";
            }
        }

        return msg;
    }

    public String Walk() {
        String msg = "";

        String EnemyD = "";
        // Creating random number for enemies
        Random rand = new Random();

        int dis = rand.nextInt(discoveryRate);

        // Iterate through the enemies to see which enemy was found
        for (Map.Entry<String, Integer> entry : Enemies.entrySet()) {
            if (dis <= entry.getValue()) {
                EnemyD = entry.getKey();
                break;
            }
        }

        if (EnemyD.contains("Boss")) {
            msg = "Boss found: " + EnemyD.substring(4);
            discoveryRate -= BossDis;
            BossDis = 0;
        }
        else {
            msg = "Enemy found: " + EnemyD;
        }
        return msg;
    }

    public void bossFound () {
        discoveryRate -= BossDis;
        BossDis = 0;
        actions.add("Challenge Boss");
    }
}
