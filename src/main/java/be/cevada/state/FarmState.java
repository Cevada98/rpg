package be.cevada.state;

import be.cevada.models.Player;
import be.cevada.models.Quest;
import be.cevada.panels.WorldPanel;
import com.googlecode.lanterna.TextColor;

public class FarmState implements GameState {

    private final GameStateManager manager;
    private String subMenu;

    public static final String RAT_QUEST_ID = "hunt_giant_rats";

    public FarmState(GameStateManager manager) {
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
            case "farmer" -> handleFarmer(actionLabel);
        }
    }

    private void showMainMenu() {
        subMenu = "main";
        WorldPanel world = manager.getWorldPanel();
        world.setLocation("Old Miller's Farm");
        world.setDescription("A weathered farmstead surrounded by fields.\nChickens peck at the ground nearby.");
        world.clearLog();
        world.addLogEntry("> You arrive at the farm.", TextColor.ANSI.WHITE);
        manager.setActions("Talk to Farmer", "Leave");
    }

    private void handleMain(String actionLabel) {
        switch (actionLabel) {
            case "Talk to Farmer" -> showFarmer();
            case "Leave" -> manager.transitionTo(new ExploringState(manager));
        }
    }

    private void showFarmer() {
        subMenu = "farmer";
        WorldPanel world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        world.setLocation("Farmer Miller");
        world.clearLog();

        Quest quest = player.getQuestById(RAT_QUEST_ID);

        if (quest == null) {
            world.setDescription("A grizzled old farmer leans on his pitchfork.\n\n\"Giant rats have been eatin' my crops!\nCould ye hunt down 5 of 'em for me?\nI'll pay ye well for the trouble.\"");
            manager.setActions("Accept Quest", "Back");
        } else if (quest.isCompleted() && !quest.isRewarded()) {
            world.setDescription("The farmer's eyes light up.\n\n\"Ye did it! All five of 'em, gone!\nHere's yer reward, as promised.\"");
            manager.setActions("Claim Reward", "Back");
        } else if (quest.isCompleted() && quest.isRewarded()) {
            world.setDescription("The farmer tips his hat.\n\n\"Thanks again for the help, traveler.\nThe crops are safe now.\"");
            manager.setActions("Back");
        } else {
            world.setDescription("The farmer looks at you expectantly.\n\n\"Still got " + (quest.getTargetCount() - quest.getCurrentCount()) +
                    " giant rats to go!\nProgress: " + quest.getProgressText() + "\"");
            manager.setActions("Back");
        }
    }

    private void handleFarmer(String actionLabel) {
        WorldPanel world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        switch (actionLabel) {
            case "Accept Quest" -> {
                Quest quest = new Quest(RAT_QUEST_ID, "Hunt Giant Rats",
                        "Kill 5 Giant Rats for Farmer Miller.", 5);
                player.addQuest(quest);
                world.addLogEntry("> Quest accepted: Hunt Giant Rats!", TextColor.ANSI.YELLOW_BRIGHT);
                world.addLogEntry("> Kill 5 Giant Rats and return to the farmer.", TextColor.ANSI.WHITE);
                showFarmer();
            }
            case "Claim Reward" -> {
                Quest quest = player.getQuestById(RAT_QUEST_ID);
                if (quest != null && quest.isCompleted() && !quest.isRewarded()) {
                    quest.setRewarded(true);
                    int goldReward = 50;
                    int xpReward = 100;
                    player.setGold(player.getGold() + goldReward);
                    boolean leveled = player.addXp(xpReward);
                    manager.syncStats();
                    world.addLogEntry("> Quest complete: Hunt Giant Rats!", TextColor.ANSI.GREEN_BRIGHT);
                    world.addLogEntry("> Received " + goldReward + " gold and " + xpReward + " XP!", TextColor.ANSI.YELLOW);
                    if (leveled) {
                        world.addLogEntry("> LEVEL UP! You are now level " + player.getLevel() + "!", TextColor.ANSI.YELLOW_BRIGHT);
                    }
                    showFarmer();
                }
            }
            case "Back" -> showMainMenu();
        }
    }
}

