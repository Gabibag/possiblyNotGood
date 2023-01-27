package NameHere;

import java.util.function.Function;

public class Item {
    private int healIncrease = 0;
    private int dmgIncr;
    private int hpIncr;
    private int HealVariance = 0;
    private String name;
    private String description;
    private int rarity;
    /*make this a number from 1-1000, for drop chance, also doubles as epic, common, etc.
    1-10  - common
    10-20 - uncommon
    20-30 - rare
    30-40 - not epic but still cool
    40-60 - super cool
    60-100 - legendary
     */
    private int cost;

    public String toString() {
        String r =
                this.getName() + ":" + "\n" + this.getDescription() + Colors.RED + "\nDamage Increase: " +
                this.getDmgIncr() +
                "\nHealth Increase: " + this.getHpIncr() + "\n" + Colors.RESET + "Rarity: " +
                Helper.getWordRarity(this);
        return r;
    }
    public Function<Player, Void> use;
    public Item(int dmgIncr, int hpIncr, String name, String description, int rarity, int cost, int heal, int healvair) {
        this.dmgIncr = dmgIncr;
        this.hpIncr = hpIncr;
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.cost = cost;
        this.healIncrease = heal;
        this.HealVariance = healvair;
    }
    public int getHealVariance(){return HealVariance;}
    public void setHealVariance(int v){HealVariance = v;}
    public void setHealIncrease(int s){
        this.healIncrease = s;
    }
    public int getHealIncrease(){return this.healIncrease;}

    public int getDmgIncr() {
        return dmgIncr;
    }

    public void setDmgIncr(int dmgIncr) {
        this.dmgIncr = dmgIncr;
    }

    public int getHpIncr() {
        return hpIncr;
    }

    public void setHpIncr(int hpIncr) {
        this.hpIncr = hpIncr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    //Items


}
