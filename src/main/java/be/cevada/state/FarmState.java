package be.cevada.state;

import be.cevada.models.Player;
import be.cevada.models.Quest;
import be.cevada.panels.WorldView;
import com.googlecode.lanterna.TextColor;

public class FarmState implements GameState {

    private final GameContext manager;
    private String subMenu;

    public static final String RAT_QUEST_ID = "hunt_giant_rats";

    public FarmState(GameContext manager) {
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
    public void handleAction(GameAction action) {
        switch (subMenu) {
            case "main" -> handleMain(action);
            case "farmer" -> handleFarmer(action);
        }
    }

    private void showMainMenu() {
        subMenu = "main";
        WorldView world = manager.getWorldPanel();
        world.setLocation("Old Miller's Farm");
        world.setDescription("A weathered farmstead surrounded by fields.\nChickens peck at the ground nearby.");
        world.clearLog();
        world.addLogEntry("> You arrive at the farm.", TextColor.ANSI.WHITE);
        manager.setActions(GameAction.TALK_TO_FARMER, GameAction.LEAVE);
    }

    private void handleMain(GameAction action) {
        switch (action) {
            case TALK_TO_FARMER -> showFarmer();
            case LEAVE -> manager.transitionTo(new ExploringState(manager));
            default -> {
            }
        }
    }

    private void showFarmer() {
        subMenu = "farmer";
        WorldView world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        world.setLocation("Farmer Miller");
        world.clearLog();

        Quest quest = player.getQuestById(RAT_QUEST_ID);

        if (quest == null) {
            world.setDescription("A grizzled old farmer leans on his pitchfork.\n\n\"Giant rats have been eatin' my crops!\nCould ye hunt down 5 of 'em for me?\nI'll pay ye well for the trouble.\"");
            manager.setActions(GameAction.ACCEPT_QUEST, GameAction.BACK);
        } else if (quest.isCompleted() && !quest.isRewarded()) {
            world.setDescription("The farmer's eyes light up.\n\n\"Ye did it! All five of 'em, gone!\nHere's yer reward, as promised.\"");
            manager.setActions(GameAction.CLAIM_REWARD, GameAction.BACK);
        } else if (quest.isCompleted() && quest.isRewarded()) {
            world.setDescription("The farmer tips his hat.\n\n\"Thanks again for the help, traveler.\nThe crops are safe now.\"");
            manager.setActions(GameAction.BACK);
        } else {
            world.setDescription("The farmer looks at you expectantly.\n\n\"Still got " + (quest.getTargetCount() - quest.getCurrentCount()) +
                    " giant rats to go!\nProgress: " + quest.getProgressText() + "\"");
            manager.setActions(GameAction.BACK);
        }
    }

    private void handleFarmer(GameAction action) {
        WorldView world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        switch (action) {
            case ACCEPT_QUEST -> {
                Quest quest = new Quest(RAT_QUEST_ID, "Hunt Giant Rats",
                        "Kill 5 Giant Rats for Farmer Miller.", 5);
                player.addQuest(quest);
                world.addLogEntry("> Quest accepted: Hunt Giant Rats!", TextColor.ANSI.YELLOW_BRIGHT);
                world.addLogEntry("> Kill 5 Giant Rats and return to the farmer.", TextColor.ANSI.WHITE);
                showFarmer();
            }
            case CLAIM_REWARD -> {
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
            case BACK -> showMainMenu();
            default -> {
            }
        }
    }
}

