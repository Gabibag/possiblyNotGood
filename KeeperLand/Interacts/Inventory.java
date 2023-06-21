package KeeperLand.Interacts;

import KeeperLand.Abstracts.Interactable;
import KeeperLand.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventory extends Interactable {
    public void onChoose(Player p) {
        System.out.println(Colors.PURPLE + "[0] Go Back");
        System.out.println("[1] Inventory");
        System.out.println("[2] Stats" + Colors.RESET);
        int c = Helper.getInput("", 0, 2);
        //if c is 1, show inventory, case c is 2, print player p
        if  (c == 0){
            return;
        }else if (c == 1) {
            inventory(p);
        } else if (c == 2) {
            System.out.println(p);
            Helper.continuePrompt();
        }

    }

    private void inventory(Player p) {
        List<Item> printItems = displayList(p);
        int input = Helper.getInput(Colors.PURPLE + "Enter an item number for more info \n[0] Exit" + Colors.RESET, 0,
                p.getInventory().size());

        if (input != 0) {
            Item inspect = printItems.get(input - 1);
            System.out.println(inspect);
            if (inspect.getName().toLowerCase().contains("shard")){
                System.out.println(Colors.PURPLE + "[0]" + " Back" + Colors.RESET);
                System.out.println(Colors.YELLOW + "[1]" + " Shatter" + Colors.RESET);
                int c = Helper.getInput("", 0, 1);
                if (c == 1){
                    System.out.println(Colors.YELLOW + "Shattered " + inspect.getName() + Colors.RESET);
                    p.getInventory().remove(inspect);
                    //random number between 1 and 3
                    int ran = (int) (Math.random() * 3) + 1;
                    if (ran ==3){
                        //assign a random item to the player using allItem from Main.java
                        Item randomItem = Main.allItem.get((int) (Math.random() * Main.allItem.size()));
                        while (randomItem.getName().toLowerCase().contains("shard")){
                            randomItem = Main.allItem.get((int) (Math.random() * Main.allItem.size()));
                        }
                        System.out.println(Colors.YELLOW + "You got a " + randomItem.getName() + Colors.RESET);
                        p.getInventory().add(randomItem);
                    } else{
                        //give back a percentage of the stats of the item to the player
                        p.setDamage(p.getDamage() + inspect.getDmgIncr() / 3);
                        p.setHp(p.getHp() + inspect.getHpIncr() / 3);
                        p.setHealAmount(p.getHealAmount() + inspect.getHealIncrease() / 3);
                        p.setHealVariance(p.getHealVariance() + inspect.getHealVariance() / 3);
                        printAdded(inspect);
                    }
                    Helper.continuePrompt();
                }
            }
            else Helper.Prompt("Press a enter when done");
            System.out.println(Colors.CLEAR);
            inventory(p);
        }
    }

    private static void printAdded(Item inspect) {
        if (inspect.getHealIncrease() / 3 > 0 || inspect.getHealVariance() / 3 > 0 || inspect.getHpIncr() / 3 > 0 || inspect.getDmgIncr() / 3 > 0) {
            String printString = "You got ";
            if (inspect.getDmgIncr() / 3 > 0)
                printString += Colors.YELLOW + inspect.getDmgIncr() / 3 + " damage, " + Colors.RESET;
            if (inspect.getHpIncr() / 3 > 0)
                printString += Colors.YELLOW + inspect.getHpIncr() / 3 + " health, " + Colors.RESET;
            if (inspect.getHealIncrease() / 3 > 0)
                printString += Colors.YELLOW + inspect.getHealIncrease() / 3 + " heal amount, " + Colors.RESET;
            if (inspect.getHealVariance() / 3 > 0)
                printString += Colors.YELLOW + inspect.getHealVariance() / 3 + " heal variance, " + Colors.RESET;
            printString = printString.substring(0, printString.length() - 2);
            System.out.println(printString);
        }
    }

    private static @NotNull List<Item> displayList(Player p) {
        System.out.println(p.getName() + "'s inventory: ");
        System.out.println("Current Balance " + Colors.CYAN + p.getMoney() + "◊");
        System.out.println("⚔ = Damage, ❤ = Health, ✧ = Heal, ⚕ = Heal Variance");
        HashMap<String, Integer> iCount = new HashMap<>();
        for (Item i : p.getInventory()) {
            if (iCount.containsKey(i.getName())) {
                iCount.put(i.getName(), iCount.get(i.getName()) + 1);
            } else {
                iCount.put(i.getName(), 1);
            }
        }
        //copy the player's inventory to a new list
        List<Item> printItems = new ArrayList<>(p.getInventory());
//        printItems.addAll(p.getInventory());
        //check if the item is already in the list, if it is, remove the item from the list
        for (int i = 0; i < printItems.size(); i++) {
            for (int j = i + 1; j < printItems.size(); j++) {
                if (printItems.get(i).getName().equals(printItems.get(j).getName())) {
                    printItems.remove(j);
                    j--;
                }
            }
        }

        int maxNameLength = 0;
        for (Item item : printItems) {
            if (item.getName().length() > maxNameLength) {
                maxNameLength = item.getName().length();
            }
        }
        int maxColLength = 0;
        for (Item printItem : printItems) {
            if (String.valueOf(printItem.getHealVariance()).length() > maxColLength) {
                maxColLength = String.valueOf(printItem.getHealVariance()).length();
            }
            if (String.valueOf(printItem.getHealIncrease()).length() > maxColLength) {
                maxColLength = String.valueOf(printItem.getHealIncrease()).length();
            }
            if (String.valueOf(printItem.getHpIncr()).length() > maxColLength) {
                maxColLength = String.valueOf(printItem.getHpIncr()).length();
            }
            if (String.valueOf(printItem.getDmgIncr()).length() > maxColLength) {
                maxColLength = String.valueOf(printItem.getDmgIncr()).length();
            }
        }


        int count = 1;
        //if the item in printItems is a shard, bring it to the top
        for(int i = 0; i < printItems.size(); i++){
            if(printItems.get(i).getName().toLowerCase().contains("shard")){
                Item temp = printItems.get(i);
                printItems.remove(i);
                printItems.add(0, temp);
//                i++;

            }
        }
        for (Item items : printItems) {
            String col = Colors.RESET;
            if (iCount.get(items.getName()) > 100) {
                col = Colors.RED_BRIGHT;
            } else if (iCount.get(items.getName()) > 50) {
                col = Colors.RED;
            } else if (iCount.get(items.getName()) > 25) {
                col = Colors.YELLOW;
            } else if (iCount.get(items.getName()) > 10) {
                col = Colors.GREEN;
            }
            if (items.getName().toLowerCase().contains("shard")) {
                col = Colors.BLUE;
            }
            StringBuilder spaceCount = new StringBuilder();
            StringBuilder variCount = new StringBuilder();
            StringBuilder hpCount = new StringBuilder();
            StringBuilder healCount = new StringBuilder();
            StringBuilder dmgCount = new StringBuilder();
            spaceCount.append(" ".repeat(Math.max(0, maxNameLength - items.getName().length() + 2 - String.valueOf(count).length())));
            variCount.append(" ".repeat(Math.max(0, maxColLength - String.valueOf(items.getHealVariance()).length())));
            hpCount.append(" ".repeat(Math.max(0, maxColLength - String.valueOf(items.getHpIncr()).length())));
            healCount.append(" ".repeat(Math.max(0, maxColLength - String.valueOf(items.getHealIncrease()).length())));
            dmgCount.append(" ".repeat(Math.max(0, maxColLength - String.valueOf(items.getDmgIncr()).length())));
            System.out.println(
                    Colors.CYAN + "[" + (count) + "] " + col + items.getName() + spaceCount +
                            Colors.RED + " ⚔" + items.getDmgIncr() + dmgCount + Colors.GREEN + " ❤" + (items).getHpIncr() + hpCount + Colors.YELLOW + " ✧" + (items).getHealIncrease() + healCount + Colors.PURPLE + " ⚕" + (items).getHealVariance() + variCount + " x" + iCount.get(items.getName()) + " " + (Helper.moreShopInfo ? Colors.RESET + " (" + (items).getDescription() + ")" : "") + Colors.RESET);
            count++;
        }

        return printItems;
    }

    public String getName() {
        return "Info";
    }

}