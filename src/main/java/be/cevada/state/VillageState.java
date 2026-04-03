package be.cevada.state;

import be.cevada.models.Player;
import be.cevada.panels.WorldPanel;
import com.googlecode.lanterna.TextColor;

public class VillageState implements GameState {

    private final GameStateManager manager;
    private String subMenu;

    public VillageState(GameStateManager manager) {
        this.manager = manager;
        this.subMenu = "main";
    }

    @Override
    public void enter() {
        showMainMenu();
    }

    @Override
    public void exit() {
    }

    @Override
    public void handleAction(String actionLabel) {
        switch (subMenu) {
            case "main" -> handleMain(actionLabel);
            case "blacksmith" -> handleBlacksmith(actionLabel);
            case "church" -> handleChurch(actionLabel);
            case "inn" -> handleInn(actionLabel);
        }
    }

    private void showMainMenu() {
        subMenu = "main";
        WorldPanel world = manager.getWorldPanel();
        world.setLocation("Village of Briarwood");
        world.setDescription("A small village with cobblestone streets.\nSmoke rises from the chimneys.");
        world.clearLog();
        world.addLogEntry("> You arrive at the village.", TextColor.ANSI.WHITE);
        manager.setActions("Blacksmith", "Church", "Inn", "Leave");
    }

    private void handleMain(String actionLabel) {
        switch (actionLabel) {
            case "Blacksmith" -> showBlacksmith();
            case "Church" -> showChurch();
            case "Inn" -> showInn();
            case "Leave" -> manager.transitionTo(new ExploringState(manager));
        }
    }

    private void showBlacksmith() {
        subMenu = "blacksmith";
        WorldPanel world = manager.getWorldPanel();
        Player player = manager.getPlayer();
        int cost = 30 * player.getLevel();

        world.setLocation("Blacksmith");
        world.setDescription("The Blacksmith hammers away at his anvil.\n\n\"Want me to sharpen yer blade?\"\nCost: " + cost + " gold  (ATK +2)");
        world.clearLog();

        if (player.getGold() >= cost) {
            manager.setActions("Upgrade", "Back");
        } else {
            world.addLogEntry("> You don't have enough gold.", TextColor.ANSI.YELLOW);
            manager.setActions("Back");
        }
    }

    private void handleBlacksmith(String actionLabel) {
        if ("Upgrade".equals(actionLabel)) {
            Player player = manager.getPlayer();
            WorldPanel world = manager.getWorldPanel();
            int cost = 30 * player.getLevel();

            if (player.getGold() >= cost) {
                player.setGold(player.getGold() - cost);
                player.boostAtk(2);
                manager.syncStats();
                world.addLogEntry("> The Blacksmith sharpens your weapon! ATK +2.", TextColor.ANSI.GREEN_BRIGHT);
                showBlacksmith();
            }
        } else if ("Back".equals(actionLabel)) {
            showMainMenu();
        }
    }

    private void showChurch() {
        subMenu = "church";
        WorldPanel world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        world.setLocation("Church");
        world.clearLog();

        if (player.getHp() < player.getMaxHp() || player.getMp() < player.getMaxMp()) {
            world.setDescription("A serene church with stained glass windows.\n\nThe priest offers healing.\nCost: 10 gold");
            if (player.getGold() >= 10) {
                manager.setActions("Heal", "Back");
            } else {
                world.addLogEntry("> You don't have enough gold.", TextColor.ANSI.YELLOW);
                manager.setActions("Back");
            }
        } else {
            world.setDescription("A serene church with stained glass windows.\n\nThe priest smiles warmly.\n\"You look well, traveler.\"");
            world.addLogEntry("> You are already fully healed.", TextColor.ANSI.WHITE);
            manager.setActions("Back");
        }
    }

    private void handleChurch(String actionLabel) {
        if ("Heal".equals(actionLabel)) {
            Player player = manager.getPlayer();
            WorldPanel world = manager.getWorldPanel();

            if (player.getGold() >= 10) {
                player.setGold(player.getGold() - 10);
                player.setHp(player.getMaxHp());
                player.setMp(player.getMaxMp());
                manager.syncStats();
                world.addLogEntry("> The priest heals you fully!", TextColor.ANSI.GREEN_BRIGHT);
                showChurch();
            }
        } else if ("Back".equals(actionLabel)) {
            showMainMenu();
        }
    }

    private void showInn() {
        subMenu = "inn";
        WorldPanel world = manager.getWorldPanel();
        Player player = manager.getPlayer();
        int cost = 15;

        world.setLocation("Inn");
        world.setDescription("A cozy inn with a crackling fireplace.\n\n\"Rest for the night?\"\nCost: " + cost + " gold  (Full restore)");
        world.clearLog();

        if (player.getGold() >= cost) {
            manager.setActions("Rest", "Back");
        } else {
            world.addLogEntry("> You don't have enough gold.", TextColor.ANSI.YELLOW);
            manager.setActions("Back");
        }
    }

    private void handleInn(String actionLabel) {
        if ("Rest".equals(actionLabel)) {
            Player player = manager.getPlayer();
            WorldPanel world = manager.getWorldPanel();

            if (player.getGold() >= 15) {
                player.setGold(player.getGold() - 15);
                player.setHp(player.getMaxHp());
                player.setMp(player.getMaxMp());
                manager.syncStats();
                world.addLogEntry("> You sleep at the inn and wake up fully restored!", TextColor.ANSI.GREEN_BRIGHT);
                showInn();
            }
        } else if ("Back".equals(actionLabel)) {
            showMainMenu();
        }
    }
}
