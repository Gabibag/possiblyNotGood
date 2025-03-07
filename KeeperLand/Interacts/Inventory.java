package KeeperLand.Interacts;

import KeeperLand.Abstracts.Interactable;
import KeeperLand.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Inventory extends Interactable {
    static List<Item> printItems = new ArrayList<>();
    static String inventoryDisplay = "";

    public void onChoose(Player p) {
        System.out.println(Colors.PURPLE + "[0] Go Back");
        System.out.println("[1] Inventory");
        System.out.println("[2] Stats" + Colors.RESET);
        int c = Helper.getInput("", 0, 2);
        //if c is 1, show inventory, case c is 2, print player p
        if (c == 1) {
            inventory(p);
        } else if (c == 2) {
            System.out.println(p);
            Helper.continuePrompt();
        }

    }

    private static void printAdded(Item inspect) {
        if (inspect.getHealIncr() / 3 > 0 || inspect.getHealVarIncr() / 3 > 0 || inspect.getHpIncr() / 3 > 0 || inspect.getDmgIncr() / 3 > 0) {
            String printString = "You got ";
            if (inspect.getDmgIncr() / 3 > 0)
                printString += Colors.YELLOW + inspect.getDmgIncr() / 3 + " damage, ";
            if (inspect.getHpIncr() / 3 > 0)
                printString += Colors.YELLOW + inspect.getHpIncr() / 3 + " health, ";
            if (inspect.getHealIncr() / 3 > 0)
                printString += Colors.YELLOW + inspect.getHealIncr() / 3 + " heal amount, ";
            if (inspect.getHealVarIncr() / 3 > 0)
                printString += Colors.YELLOW + inspect.getHealVarIncr() / 3 + " heal variance, ";
            printString = printString.substring(0, printString.length() - 2);
            System.out.println(printString + Colors.RESET);
        }
    }

    private static List<Item> displayList(Player p) {
        System.out.println(returnDisplayableInventory(p));
        return printItems;
    }

    public static String returnDisplayableInventory(Player p) {
        String returnString = "";
        returnString += p.getName() + "'s inventory: \n";
        returnString += "Current Balance " + Colors.CYAN + p.getMoney() + "◊\n";
        returnString += "⚔ = Damage, ❤ = Health, ✧ = Heal, ⚕ = Heal Variance\n";

        if (!inventoryDisplay.isEmpty()) {
            return returnString + inventoryDisplay;
        }


        List<Item> printItems = new ArrayList<>(p.getInventory());

        int maxTotalDmg = 0;
        int maxTotalHp = 0;
        int maxTotalHeal = 0;
        int maxTotalHealVariance = 0;

        for (Item item : printItems) {
            String itemType = item.getName();
            int totalDmg = 0;
            int totalHp = 0;
            int totalHeal = 0;
            int totalHealVariance = 0;
            List<Item> inventory = p.getInventory();
            for (Item i : inventory) {
                if (i.getName().equals(itemType)) {
                    totalDmg += i.getDmgIncr();
                    totalHp += i.getHpIncr();
                    totalHeal += i.getHealIncr();
                    totalHealVariance += i.getHealVarIncr();
                }
            }
            maxTotalDmg = Math.max(maxTotalDmg, totalDmg);
            maxTotalHp = Math.max(maxTotalHp, totalHp);
            maxTotalHeal = Math.max(maxTotalHeal, totalHeal);
            maxTotalHealVariance = Math.max(maxTotalHealVariance, totalHealVariance);
        }

        int maxNameLength = 0;
        for (Item item : printItems) {
            maxNameLength = Math.max(item.getName().length(), maxNameLength);
        }
        int maxColLength = 0;
        maxColLength = Math.max(maxColLength, String.valueOf(maxTotalDmg).length());
        maxColLength = Math.max(maxColLength, String.valueOf(maxTotalHp).length());
        maxColLength = Math.max(maxColLength, String.valueOf(maxTotalHeal).length());
        maxColLength = Math.max(maxColLength, String.valueOf(maxTotalHealVariance).length());
        maxColLength += 2;

        int count = 1;

        //sort list by count
        printItems.sort(Comparator.comparingInt(Item::getCount));
        //if the item in printItems is a shard, bring it to the top
        for (int i = 0; i < printItems.size(); i++) {
            if (printItems.get(i).getName().toLowerCase().contains("shard")) {
                Item temp = printItems.get(i);
                printItems.remove(i);
                printItems.addFirst(temp);
            }
        }
        //number of unique items
        float avgPercent = printItems.size();
        for (Item items : printItems) {

            String itemType = items.getName();
            int totalDmg = items.getDmgIncr() * items.getCount();
            int totalHp = items.getHpIncr() * items.getCount();
            int totalHeal = items.getHealIncr() * items.getCount();
            int totalHealVariance = items.getHealVarIncr() * items.getCount();
            float itemPercent = items.getCount() / ((float) p.getInventory().size());
            float diff = avgPercent - itemPercent;
            String col = Colors.RESET;
            if (diff > 0.25 && items.getCount() > 40) {
                col = Colors.RED_BRIGHT;
            } else if (diff > 0.1 && items.getCount() > 20) {
                col = Colors.RED;
            } else if (diff > 0.05 && items.getCount() > 10) {
                col = Colors.YELLOW;
            }
            if (items.getName().toLowerCase().contains("shard")) {
                col = Colors.BLUE;
            }
            StringBuilder variCount = new StringBuilder();
            StringBuilder hpCount = new StringBuilder();
            StringBuilder healCount = new StringBuilder();
            StringBuilder dmgCount = new StringBuilder();
            StringBuilder countString = new StringBuilder();
            String spaceCount = " ".repeat(Math.max(0, maxNameLength - items.getName().length() + 2 - String.valueOf(count).length()));
            variCount.append(" ".repeat(Math.max(0, maxColLength - String.valueOf(totalHealVariance).length())));
            hpCount.append(" ".repeat(Math.max(0, maxColLength - String.valueOf(totalHp).length())));
            healCount.append(" ".repeat(Math.max(0, maxColLength - String.valueOf(totalHeal).length())));
            dmgCount.append(" ".repeat(Math.max(0, maxColLength - String.valueOf(totalDmg).length())));
            countString.append(" ".repeat(Math.max(0, (maxColLength + 3) - String.valueOf(items.getCount()).length())));

            inventoryDisplay += Colors.CYAN + "[" + (count) + "] " + col + items.getName() + spaceCount +
                    isNot0(Colors.RED, totalDmg) + " ⚔" + totalDmg + dmgCount + isNot0(Colors.GREEN, totalHp) + " ❤" + totalHp + hpCount + isNot0(Colors.YELLOW, totalHeal) + " ✧" + totalHeal + healCount + isNot0(Colors.PURPLE, totalHealVariance) + " ⚕" + totalHealVariance + variCount + Colors.CYAN + " x" + items.getCount() + countString + (Colors.RESET + " " + (items).getDescription()) + Colors.RESET + "\n";
            count++;
        }

        int tempMaxDmg = p.getDamage();
        int tempMaxHealth = p.getHp();
        int tempMaxHeal = p.getHealAmount();
        int tempMaxHealVar = p.getHealVariance();
        for (Item i : p.getInventory()) {

            tempMaxDmg += i.getDmgIncr();
            tempMaxHealth += i.getHpIncr();
            tempMaxHeal += i.getHealIncr();
            tempMaxHealVar += i.getHealVarIncr();
        }
        int totalDmg = 0;
        int totalHp = 0;
        int totalHeal = 0;
        int totalHealVariance = 0;
        for (Item i : p.getInventory()) {
            totalDmg += i.getDmgIncr();
            totalHp += i.getHpIncr();
            totalHeal += i.getHealIncr();
            totalHealVariance += i.getHealVarIncr();
        }

        inventoryDisplay += "\n" + Colors.CYAN + "Total Benefits: \n" + Colors.RESET;
        if (totalDmg != 0) {
            inventoryDisplay += (" + " + totalDmg + " damage (Contributes to " + Math.round((totalDmg / ((float) tempMaxDmg)) * 10000) / 100f + "%) of total damage \n");
        }
        if (totalHp != 0) {
            inventoryDisplay += (" + " + totalHp + " health (Contributes to " + Math.round((totalHp / ((float) tempMaxHealth)) * 10000) / 100f + "%) of total health \n");
        }
        if (totalHeal != 0) {
            inventoryDisplay += (" + " + totalHeal + " healing (Contributes to " + Math.round((totalHeal / ((float) tempMaxHeal)) * 10000) / 100f + "%) of total healing \n");
        }
        if (totalHealVariance != 0) {
            inventoryDisplay += (" + " + totalHealVariance + " healing variance (Contributes to " + Math.round((totalHealVariance / ((float) tempMaxHealVar)) * 10000) / 100f + "%) of total healing variance \n");
        }
        return returnString + inventoryDisplay + ("\nTotal Items: " + Main.player.getInventory().size());
    }

    private static void sellItems(Item item) {
        Player player = Main.player;
        //ask user how many to sell
        int num = 0;
        for (Item i : player.getInventory()) {
            if (!i.getName().equalsIgnoreCase(item.getName())) {
                continue;
            }
            num++;
        }
        if (num == 1) {
            System.out.println(Colors.RED + "Confirm selling " + item.getName() + "?" + Colors.RESET);
            System.out.println(Colors.PURPLE + "[1]" + Colors.RED + " Yes");
            System.out.println(Colors.PURPLE + "[2] No" + Colors.RESET);
            int c = Helper.getInput("", 1, 2);
            if (c == 2) {
                return;
            }
            System.out.println(Colors.PURPLE + "Sold " + item.getName() + " for " + (int) (item.getCost() * (9f / 10)));
            player.addMoney((int) (item.getCost() * (9f / 10)));
            player.getInventory().remove(item);
            return;
        }
        System.out.println(Colors.RED + "How many " + item.getName() + " would you like to sell? [1-" + num + "]" + Colors.RESET);
        System.out.println(Colors.PURPLE + "[0] Cancel" + Colors.RESET);
        int c = Helper.getInput("", 1, num);
        System.out.println("Sold " + item.getName() + " for " + (int) (item.getCost() * (9f / 10)));
        player.addMoney((int) (item.getCost() * (9f / 10)));
        player.getInventory().remove(item);
    }

    public void inventory(Player p) {
        printItems = displayList(p);
        int input = Helper.getInput(Colors.PURPLE + "Enter an item number for more info \n[0] Exit" + Colors.RESET, 0,
                p.getInventory().size());

        if (input != 0) {
            int tempMaxDmg = p.getDamage();
            int tempMaxHealth = p.getHp();
            int tempMaxHeal = p.getHealAmount();
            int tempMaxHealVar = p.getHealVariance();
            for (Item i : p.getInventory()) {

                tempMaxDmg += i.getDmgIncr();
                tempMaxHealth += i.getHpIncr();
                tempMaxHeal += i.getHealIncr();
                tempMaxHealVar += i.getHealVarIncr();
            }


            Item inspect = printItems.get(input - 1);

            //count the number of duplicate items with the same tier in inspectList
            HashMap<Integer, Integer> tierCount = new HashMap<>();


            //sort items in inspectList by tier
            System.out.println(Colors.YELLOW + "You have " + inspect.getCount() + " " + inspect.getName() + "s" + Colors.RESET);
            System.out.println(inspect + Colors.RESET + "  " + inspect.getDescription());

            int totalDmg = inspect.getDmgIncr() * inspect.getCount();
            int totalHp = inspect.getHpIncr() * inspect.getCount();
            int totalHeal = inspect.getHealIncr() * inspect.getCount();
            int totalHealVariance = inspect.getHealVarIncr() * inspect.getCount();
            System.out.println("Total stats:");
            if (inspect.getDmgIncr() != 0) {
                System.out.println(" + " + totalDmg + " damage (Contributes to " + Math.round((totalDmg / ((float) tempMaxDmg)) * 10000) / 100f + "%) of total damage");
            }
            if (inspect.getHpIncr() != 0) {
                System.out.println(" + " + totalHp + " health (Contributes to " + Math.round((totalHp / ((float) tempMaxHealth)) * 10000) / 100f + "%) of total health");
            }
            if (inspect.getHealIncr() != 0) {
                System.out.println(" + " + totalHeal + " healing (Contributes to " + Math.round((totalHeal / ((float) tempMaxHeal)) * 10000) / 100f + "%) of total healing");
            }
            if (inspect.getHealVarIncr() != 0) {
                System.out.println(" + " + totalHealVariance + " healing variance (Contributes to " + Math.round((totalHealVariance / ((float) tempMaxHealVar)) * 10000) / 100f + "%) of total healing variance");
            }
            System.out.println(Colors.RESET);
            if (inspect.getName().toLowerCase().contains("shard")) {
                System.out.println(Colors.PURPLE + "[0]" + " Back" + Colors.RESET);
                System.out.println(Colors.YELLOW + "[1]" + " Shatter" + Colors.RESET);
                int c = Helper.getInput("", 0, 1);
                if (c == 1) {
                    System.out.println(Colors.YELLOW + "Shattered " + inspect.getName() + Colors.RESET);
                    p.getInventory().remove(inspect);
                    //random number between 1 and 3
                    int ran = (int) (Math.random() * 3) + 1;
                    if (ran == 3) {
                        //assign a random item to the player using allItem from Main.java
                        Item randomItem = Main.allItem.get((int) (Math.random() * Main.allItem.size()));
                        while (randomItem.getName().toLowerCase().contains("shard")) {
                            randomItem = Main.allItem.get((int) (Math.random() * Main.allItem.size()));
                        }
                        System.out.println(Colors.YELLOW + "You got a " + randomItem.getName() + Colors.RESET);
                        p.addToInventory(randomItem);
                    } else {
                        //give back a percentage of the stats of the item to the player
                        p.setDamage(p.getDamage() + inspect.getDmgIncr() / 3);
                        p.setHp(p.getHp() + inspect.getHpIncr() / 3);
                        p.setHealAmount(p.getHealAmount() + inspect.getHealIncr() / 3);
                        p.setHealVariance(p.getHealVariance() + inspect.getHealVarIncr() / 3);
                        printAdded(inspect);
                    }
                    Helper.continuePrompt();
                }
            } else {
                System.out.println(Colors.PURPLE + "[0]" + " Back" + Colors.RESET);
                int c = Helper.getInput(Colors.YELLOW + "[1]" + " Sell" + Colors.RESET, 0, 1);
                if (c == 1) {
                    sellItems(inspect);
                    Helper.continuePrompt();
                }

            }
            System.out.println(Colors.CLEAR);
            inventory(p);
        }
        inventoryDisplay = "";
    }

    public static String isNot0(String col, int stat) {
        if (stat != 0) {
            return col;
        }
        return Colors.WHITE;
    }

    public String getName() {
        return "Info";
    }

}