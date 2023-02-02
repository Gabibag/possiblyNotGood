package NameHere.Enemies.Spirits;

import NameHere.Abstracts.Enemy;
import NameHere.Abstracts.Spirit;
import NameHere.Colors;
import NameHere.Main;
import NameHere.Player;

import java.util.List;

public class BuffSpirit extends Spirit {
    public void setBaseStats() {
        this.baseHp = 40;
        this.coins = 5;
        this.dodgeRate = 1;
        this.xp = 5;
        this.name = "Buff Spirit";
    }

    public boolean canSpawn(Player p) {
        return Main.r.nextInt(1, 10) == 2;
    }

    @Override
    public int Attack(Player p, List<Enemy> allies) {
        for (Enemy target : allies) {
            target.setDamage(target.getDamage() + (int)(target.getDamage()*0.1));
        }
        System.out.println(Colors.GREEN + "The Buff Spirit buffs it's party for 1 damage" + Colors.RESET);
        return 0;
    }
}
