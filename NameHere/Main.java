package NameHere;

import NameHere.Abstracts.Boss;
import NameHere.Interacts.Battle;
import NameHere.Abstracts.Enemy;
import NameHere.Abstracts.Enviorment;
import NameHere.Abstracts.Interactable;
import NameHere.Enviroments.LavaZone;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class Main {
    public static List<Enviorment> allPlaces = new ArrayList<>();
    public static Enviorment currentPlace;
    public static List<Enemy> allEnemies = new ArrayList<>();
    public static List<Boss> allBosses = new ArrayList<>();
    public static Random r;
    public static List<Interactable> allInteracts = new ArrayList<>(); //adds everything that can be talked to(interacted) to an arraylist
    //make a method to return the meaning of life

    public static void main(String[] args) {
        s = new Scanner(System.in);
        r = new Random();
        initTypes();
        System.out.println(Colors.CLEAR + "Press ctrl + c to quit ;)");
        //defaults for player
        
        int saves = Helper.getInput("[0] New save \n[1] Load Save", 0, 1);
        if(saves == 1){
            try {
                player = loadSave();
                if(player == null){
                    System.out.println("Corrupted Save, creating new player instead");
                    saves = 0;
                }
            } catch (Exception e) {
                saves = 0;
            }
            
        }
        if(saves == 0){
        List<String> takenNames = allPlayerFiles();
        for(int i = 0; i < takenNames.size(); i++){
            takenNames.set(i,takenNames.get(i).substring(0, takenNames.get(i).length() - 4));
        }
        String name = Helper.Prompt(Colors.CYAN + "Welcome \nEnter your player's name: " + Colors.RESET);
        while(takenNames.contains(name)){
            name = Helper.Prompt(Colors.RED + "That name is already taken, please enter a new name: " + Colors.RESET);
        }
        player = new Player(name, 40, 5,
                            new ArrayList<>());
        player.addMoney(50);
        player.setHealAmount(3);
        player.setHealVariance(1);
        }
        getNewPlace();
        if(player.getName().equals("among us")||player.getName().equals("test")){
            player.incStageNum(9);
            System.out.println(Helper.getScaleFactor());
            player.setHealAmount(100);
            player.addMoney(99999);
            player.setDmg(500);
            System.out.println("sus");
            Main.currentPlace = new LavaZone();
        }
        else if(player.getName().equalsIgnoreCase("playtest")||player.getName().equalsIgnoreCase("ptest")){
            List<Enemy> spawns;
            List<Enemy> tempenemies;
            for (int i = 0; i < 90; i++) {
                spawns = Battle.getEnemies(player);
                tempenemies = Helper.getRandomElements(spawns, 3);

                for(Enemy e: tempenemies){
                    e.randDrops(player,e);
                }
                getNewPlace();
            }
            player.incStageNum(90);
            System.out.println(Helper.getScaleFactor());
            System.out.println("sussy");
            Main.currentPlace = new LavaZone();
        }
        while (true) {
            System.out.print(Colors.RESET + Colors.CLEAR);
            System.out.println("You are currently in the " + currentPlace.getName() + ", on stage " + player.getStageNum() + Colors.PURPLE);
            for (int i = 0; i < allInteracts.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + allInteracts.get(i).getName());
            }
            int choice = -1 + Helper.getInput(Colors.RESET, allInteracts.size() + 1);
            allInteracts.get(choice).onChoose(player);
        }
    }
    public static List<String> allPlayerFiles(){
        List<String> saves = new ArrayList<String>();
        for (File f : new File(".").listFiles()) {
            if(f.getName().endsWith(".plr")){
                saves.add(f.getName());
            }
            
        }
        return saves;
    }

    private static Player loadSave() throws Exception{
        List<String> saves = allPlayerFiles();
        if(saves.size() == 0){
            System.out.println("No saves could be found");
            throw new Exception("no saves");
        }
        for (int  i =0; i < saves.size(); i++) {
            System.out.println("[" + i + "] " +  saves.get(i));
        }
        return Player.loadFromFile((saves.get(Helper.getInput("Choose a save:", 0, saves.size() - 1))));
    }


    public static void getNewPlace() {
        currentPlace = allPlaces.get(r.nextInt(allPlaces.size()));
        while (!currentPlace.isVaild(player)) {
            currentPlace = allPlaces.get(r.nextInt(allPlaces.size()));
        }
    }

    public static Player player;

    public static Scanner s;


    /**
     * peforms black magic to get all of the types
     **/
    public static void initTypes() {
        File folder = new File(".");
        initDirc(folder, "");
        for (Interactable i : allInteracts) {
            if (i.getName().equalsIgnoreCase("quit")) {
                allInteracts.remove(i);
                allInteracts.add(i);
                break;

            }
        }
        //Items?

    }

    public static void initDirc(File Dirc, String path) {
        File[] listOfFiles = Dirc.listFiles();
        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile() && listOfFile.getName().endsWith(".java")) {
                try {
                    Class<?> s = Class.forName(
                            path + listOfFile.getName().substring(0, listOfFile.getName().indexOf(".java")));
                    s.newInstance();

                } catch (Exception ignored) {
                    System.out.println(ignored);
                }
            }
            else if (listOfFile.isDirectory()) {
                initDirc(listOfFile, path + listOfFile.getName() + ".");
            }
        }
    }

}