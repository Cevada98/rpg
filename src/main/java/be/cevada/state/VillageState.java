package be.cevada.state;

import be.cevada.models.Player;
import be.cevada.panels.WorldView;
import be.cevada.state.services.VillageService;
import com.googlecode.lanterna.TextColor;

public class VillageState implements GameState {

    private final VillageDeps manager;
    private String subMenu;
    private final VillageService villageService;

    public VillageState(VillageDeps manager) {
        this.manager = manager;
        this.subMenu = "main";
        this.villageService = new VillageService();
    }

    @Override
    public void enter() {
        showMainMenu();
    }

    @Override
    public void exit() {
    }

    @Override
    public void handleAction(GameAction action) {
        switch (subMenu) {
            case "main" -> handleMain(action);
            case "blacksmith" -> handleBlacksmith(action);
            case "church" -> handleChurch(action);
            case "inn" -> handleInn(action);
        }
    }

    private void showMainMenu() {
        subMenu = "main";
        WorldView world = manager.getWorldPanel();
        world.setLocation("Village of Briarwood");
        world.setDescription("A small village with cobblestone streets.\nSmoke rises from the chimneys.");
        world.clearLog();
        world.addLogEntry("> You arrive at the village.", TextColor.ANSI.WHITE);
        manager.setActions(GameAction.BLACKSMITH, GameAction.CHURCH, GameAction.INN, GameAction.LEAVE);
    }

    private void handleMain(GameAction action) {
        switch (action) {
            case BLACKSMITH -> showBlacksmith();
            case CHURCH -> showChurch();
            case INN -> showInn();
            case LEAVE -> manager.transitionTo(manager.newExploringState());
            default -> {
            }
        }
    }

    private void showBlacksmith() {
        subMenu = "blacksmith";
        WorldView world = manager.getWorldPanel();
        Player player = manager.getPlayer();
        int cost = villageService.blacksmithUpgradeCost(player.getLevel());

        world.setLocation("Blacksmith");
        world.setDescription("The Blacksmith hammers away at his anvil.\n\n\"Want me to sharpen yer blade?\"\nCost: " + cost + " gold  (ATK +2)");
        world.clearLog();

        if (villageService.canAfford(player.getGold(), cost)) {
            manager.setActions(GameAction.UPGRADE, GameAction.BACK);
        } else {
            world.addLogEntry("> You don't have enough gold.", TextColor.ANSI.YELLOW);
            manager.setActions(GameAction.BACK);
        }
    }

    private void handleBlacksmith(GameAction action) {
        if (action == GameAction.UPGRADE) {
            Player player = manager.getPlayer();
            WorldView world = manager.getWorldPanel();
            int cost = villageService.blacksmithUpgradeCost(player.getLevel());

            if (villageService.canAfford(player.getGold(), cost)) {
                player.setGold(player.getGold() - cost);
                player.boostAtk(2);
                manager.syncStats();
                world.addLogEntry("> The Blacksmith sharpens your weapon! ATK +2.", TextColor.ANSI.GREEN_BRIGHT);
                showBlacksmith();
            }
        } else if (action == GameAction.BACK) {
            showMainMenu();
        }
    }

    private void showChurch() {
        subMenu = "church";
        WorldView world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        world.setLocation("Church");
        world.clearLog();

        if (player.getHp() < player.getMaxHp() || player.getMp() < player.getMaxMp()) {
            world.setDescription("A serene church with stained glass windows.\n\nThe priest offers healing.\nCost: " + VillageService.CHURCH_HEAL_COST + " gold");
            if (villageService.canAfford(player.getGold(), VillageService.CHURCH_HEAL_COST)) {
                manager.setActions(GameAction.HEAL, GameAction.BACK);
            } else {
                world.addLogEntry("> You don't have enough gold.", TextColor.ANSI.YELLOW);
                manager.setActions(GameAction.BACK);
            }
        } else {
            world.setDescription("A serene church with stained glass windows.\n\nThe priest smiles warmly.\n\"You look well, traveler.\"");
            world.addLogEntry("> You are already fully healed.", TextColor.ANSI.WHITE);
            manager.setActions(GameAction.BACK);
        }
    }

    private void handleChurch(GameAction action) {
        if (action == GameAction.HEAL) {
            Player player = manager.getPlayer();
            WorldView world = manager.getWorldPanel();

            if (villageService.canAfford(player.getGold(), VillageService.CHURCH_HEAL_COST)) {
                player.setGold(player.getGold() - VillageService.CHURCH_HEAL_COST);
                player.setHp(player.getMaxHp());
                player.setMp(player.getMaxMp());
                manager.syncStats();
                world.addLogEntry("> The priest heals you fully!", TextColor.ANSI.GREEN_BRIGHT);
                showChurch();
            }
        } else if (action == GameAction.BACK) {
            showMainMenu();
        }
    }

    private void showInn() {
        subMenu = "inn";
        WorldView world = manager.getWorldPanel();
        Player player = manager.getPlayer();
        int cost = VillageService.INN_REST_COST;

        world.setLocation("Inn");
        world.setDescription("A cozy inn with a crackling fireplace.\n\n\"Rest for the night?\"\nCost: " + cost + " gold  (Full restore)");
        world.clearLog();

        if (villageService.canAfford(player.getGold(), cost)) {
            manager.setActions(GameAction.REST, GameAction.BACK);
        } else {
            world.addLogEntry("> You don't have enough gold.", TextColor.ANSI.YELLOW);
            manager.setActions(GameAction.BACK);
        }
    }

    private void handleInn(GameAction action) {
        if (action == GameAction.REST) {
            Player player = manager.getPlayer();
            WorldView world = manager.getWorldPanel();

            if (villageService.canAfford(player.getGold(), VillageService.INN_REST_COST)) {
                player.setGold(player.getGold() - VillageService.INN_REST_COST);
                player.setHp(player.getMaxHp());
                player.setMp(player.getMaxMp());
                manager.syncStats();
                world.addLogEntry("> You sleep at the inn and wake up fully restored!", TextColor.ANSI.GREEN_BRIGHT);
                showInn();
            }
        } else if (action == GameAction.BACK) {
            showMainMenu();
        }
    }
}
