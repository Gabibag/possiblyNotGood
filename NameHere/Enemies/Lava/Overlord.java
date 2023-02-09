package NameHere.Enemies.Lava;

import NameHere.Abstracts.Enemy;
import NameHere.Enviroments.LavaZone;
import NameHere.Main;
import NameHere.Player;

import java.util.Random;

public class Overlord extends Enemy {
    Random r = new Random();
public void setBaseStats(){
        this.baseHp = Integer.MAX_VALUE/10000;
        this.damage = Integer.MAX_VALUE/10000;
        this.xp = 1000;
        this.name = "Overlord";
        this.coins = 1000;

    }

    @Override
    public boolean canSpawn(Player p) {

        return r.nextInt(50) == 2 && Main.currentPlace instanceof LavaZone; //(r.nextInt([spawnchance]) == 2)

    }
}
