package MenuClass;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import EnemyData.*;

public class Combat {
    protected Enemy curEnemy;
    protected Random rand = new Random();
    protected Player p;

    public Combat () {

    };

    public Combat(String name, String type) throws Exception {
        curEnemy = new Enemy(name);
    }

    public String fight(Player player) throws Exception {
        String msg = "";
        p = player;
        System.out.println("Fight start");

        // Initialize player and enemy stats
        int pHp = p.getStat("Health");
        LinkedHashMap<String, Integer> pDMG = p.getDmg();

        // Calculating enemy damage
        int eHp = curEnemy.getStat("Health");
        int ePhys = (int) Math.round(curEnemy.getStat("Pdmg") * ((100 - p.getStat("Armour"))/ 100.0));
        int eEle = (int) Math.round(curEnemy.getStat("Edmg") * ((100 - p.getStat("Elemental Resist"))/ 100.0));         

        double rng = 0;

        while (pHp > 0 && eHp > 0) {
            TimeUnit.SECONDS.sleep(1);
            // Rolling a rng number for player and enemy damage
            rng = (80 + rand.nextInt(40)) / 100.0;
            
            // Iterate through player damage type and creating message
            String dmgMsg = "Player attacks for ";
            int total = 0;
            StringBuilder bld = new StringBuilder();
            // Getting all the after effects
            HashMap<String, Integer> afterEffect = p.getAfterEffect();

            for (Map.Entry<String, Integer> entry : pDMG.entrySet()) { 
                int dmgDealt = (int) Math.round(rng * entry.getValue() * ((100 - curEnemy.getStat(entry.getKey())) / 100.0));
                if (afterEffect.containsKey(entry.getKey())) {
                    dmgDealt += afterEffect.get(entry.getKey());
                }
                // adding defence penetration later...
                /////////////////////////////////////
                eHp -= dmgDealt ;
                total += dmgDealt;
                bld.append(dmgDealt + " " + entry.getKey() + " damage, ");
            }

            dmgMsg += bld.toString() + "for a total of " + total + " damage.";
            if (eHp < 0) {
                eHp = 0;
            }

            System.out.println(dmgMsg + "\n\n" + curEnemy.getName() + " have " + eHp + " hp left\n");
            
            TimeUnit.SECONDS.sleep(1);
            if (eHp > 0) {
                rng = (80 + rand.nextInt(40)) / 100.0;
                int eDmg = 0;
                int eTotal = 0;
                String dmgTaken = curEnemy.getName() + " attacks for ";
                if (ePhys > 0) {
                    eDmg = (int) Math.round(ePhys * rng);
                    pHp -= eDmg;
                    eTotal += eDmg;
                    dmgTaken += eDmg + " physical damage, ";
                }
                if (eEle > 0) {
                    eDmg = (int) Math.round(eEle * rng);
                    pHp -= eDmg;
                    eTotal += eDmg;
                    dmgTaken += eDmg + " elemental damage, ";
                }
                if (pHp < 0) {
                    pHp = 0;
                }
                System.out.println(dmgTaken + "for a total of " + eTotal + " damage.\n\nPlayer have " + pHp + " hp left\n" );
            }
        }

        if (pHp == 0) {
            // If player died
            msg = "Player died";
        }
        else {
            // If player won
            System.out.println(curEnemy.getName() + " defeated");
            // evaluate drop
            this.calculateLoot();
            msg = "Continue exploring";
            
            
        }
        
        System.out.println(p.save());
        return msg;
    }

    public void switchEnemy(String name, String type) throws Exception {
        curEnemy = new Enemy(name);
    }

    public void calculateLoot() {
        // Getting the possible loot and use rng to check which loot dropped
        LinkedHashMap<String, Integer> loot = curEnemy.getloot();
        int roll = curEnemy.getStat("Roll");
        int maxRoll = roll + (int) Math.round((roll - curEnemy.getStat("In Range")) * (100 - p.getStat("Luck")) / 100.0);
        roll = rand.nextInt(maxRoll);
        String drop = "nothing";

        for (Map.Entry<String, Integer> entry : loot.entrySet()) {
            if (entry.getValue() > roll) {
                drop = entry.getKey();
                break;
            }            
        }

        // Creating loot and putting into player's inventory
        if (!drop.equals("nothing")) {
            p.addLoot(drop);
        }
        int exp = curEnemy.getStat("Experience");
        String msg = p.addExp(exp);
        System.out.println(curEnemy.getName() + " droped: " + drop + "\n" + msg);
    }

}
