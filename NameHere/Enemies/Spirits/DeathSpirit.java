package NameHere.Enemies.Spirits;

import NameHere.Abstracts.Enemy;
import NameHere.Abstracts.Spirit;
import NameHere.Main;
import NameHere.Player;

import java.util.List;

public class DeathSpirit extends Spirit {
    public void setBaseStats() {
        this.baseHp = 10;
        this.coins = 5;
        this.dodgeRate = 1;
        this.xp = 5;
        this.name = "DeathSpirit";
    }

    public boolean canSpawn(Player p) {
        return Main.r.nextInt(1, 10) == 2;
    }

    @Override
    public int Attack(Player p, List<Enemy> allies) {
        //remove 10% of the regular player's hp
        System.out.println("The Death Spirit removes 10% of the player's hp");
        return (int)(p.getHp() - (p.getHp()*0.1));
    }
}
