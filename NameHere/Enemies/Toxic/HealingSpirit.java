package NameHere.Enemies.Toxic;

import java.util.List;

import NameHere.Player;
import NameHere.Abstracts.Enemy;
import NameHere.Enviroments.ToxicEnv;
import NameHere.Colors;
import NameHere.Helper;
import NameHere.Main;

public class HealingSpirit extends Enemy {
    public HealingSpirit() {
        super();
        this.baseHp = 10;
        this.coins = 5;
        this.battleHp = baseHp;
        this.dodgeRate = 1;
        this.xp = 5;
        this.name = "Healing Spirit";
    }

    public boolean canSpawn(Player p) {
        return Main.currentPlace instanceof ToxicEnv;
    }

    @Override
    public int Attack(Player p, List<Enemy> allies) {
        Enemy target = Helper.getRandomElements(allies, 1).get(0);
        System.out.println(Colors.GREEN + "The Healing Spirit heals " + target.getName() + " for 3 Hp");
        target.setBattleHp(target.getBattleHp() + 3);
        return 0;
    }
}
