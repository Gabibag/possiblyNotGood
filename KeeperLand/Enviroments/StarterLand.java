package KeeperLand.Enviroments;

import KeeperLand.*;
import KeeperLand.Abstracts.Enemy;
import KeeperLand.Abstracts.Enviorment;

import java.util.Arrays;
import java.util.List;

public class StarterLand extends Enviorment {
    @Override
    public void BattleStart(Player p, List<Enemy> a){
        System.out.println("Welcome to your first battle!");
        Helper.continuePrompt();
        System.out.println(Colors.CLEAR + "When the battle starts, choose an option to preform an action.");
        System.out.println(Colors.RED + "[1] Attack" + Colors.RESET + " - choose an enemy to deal damage to \n" + Colors.RED + "[2] Heal" + Colors.YELLOW + " - heal some of your hp back. The lower you get, the less effective it is "+ Colors.CYAN + "\n[3] Inspect "+ Colors.RESET + "- this does not take an action, but allows you to get information on the player and it's mobs.");
        Helper.continuePrompt();
        System.out.println(Colors.CLEAR);
    }
    private int survival = 0;
    @Override
    public String getDescription() {
        return "A place to start your journey";
    }

    @Override
    public boolean isValid(Player p) {
        return false;
    }

    @Override
    public java.lang.String getName() {
        return "Starter Land";
    }

    @Override
    public List<Item> getShopItems() {
        return Arrays.asList(ItemData.warriorSword,
                             ItemData.woodenSword,
                             ItemData.bountyHunterSword);
    }

    @Override
    public void playerAction(Player p, List<Enemy> enemies) {

    }

    @Override
    public void turnEnd(Player p, List<Enemy> enemies) {
    }

    @Override
    public int modifyPlayerDamage(int preChange) {
        return preChange;
    }

    @Override
    public int modifyEnemyDamage(int preChange) {
        if(Main.player.getBattleHp() <= preChange ){
            System.out.println((survival<=20 ? "You are protected from damage by the land" : "You can't be this bad at the game."));
            survival++;
            return 0;
        }
        return preChange;
    }
    public StarterLand() {
        //do nothing. Put here to stop the super constructor from being called and adding this to the list of places
        //idk if this works
    }
}
