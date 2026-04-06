package be.cevada.state;

import be.cevada.combat.CombatEngine;
import be.cevada.data.EnemyRegistry;
import be.cevada.data.GameRandom;
import be.cevada.models.Enemy;
import be.cevada.models.Place;
import be.cevada.models.Player;
import be.cevada.models.Quest;
import be.cevada.panels.WorldView;
import be.cevada.state.services.ExplorationService;
import com.googlecode.lanterna.TextColor;

import java.util.ArrayList;
import java.util.List;

public class ExploringState implements GameState {

    private final ExploringDeps manager;
    private final ExplorationService explorationService;

    private static final String[] EXPLORE_EVENTS = {
            "You find a narrow path through the trees...",
            "You hear distant howling in the darkness.",
            "A cold wind blows through the forest.",
            "You discover old footprints in the mud.",
            "The trees seem to close in around you.",
            "You stumble upon a clearing bathed in pale moonlight.",
            "A strange fog rolls in from the valley.",
            "You notice strange runes carved into an ancient oak.",
            "A crow watches you silently from a branch above.",
            "You find the remains of an old campfire, still warm.",
            "A distant bell tolls somewhere deep in the forest.",
            "You spot the glint of something shiny buried under leaves.",
            "The wind carries the scent of smoke from far away.",
            "An eerie silence falls — even the insects go quiet.",
            "You pass a mossy stone that looks like a crouching figure.",
    };

    public ExploringState(ExploringDeps manager) {
        this.manager = manager;
        this.explorationService = new ExplorationService(GameRandom.get());
    }

    @Override
    public void enter() {
        WorldView world = manager.getWorldPanel();
        world.setLocation("Dark Forest");
        world.setDescription("Twisted trees surround you.\nThe air is thick with mystery.");
        world.clearLog();
        world.addLogEntry("> You look around cautiously.", TextColor.ANSI.WHITE);
        manager.syncStats();
        refreshActions();
    }

    @Override
    public void exit() {
    }

    @Override
    public void handleAction(GameAction action) {
        switch (action) {
            case EXPLORE -> explore();
            case REST -> rest();
            case INVENTORY -> inventory();
            case PLACES -> manager.transitionTo(manager.newPlacesState());
            case QUESTS -> manager.transitionTo(manager.newQuestState());
            case QUIT -> manager.getOnQuit().run();
            default -> {
            }
        }
    }

    private void refreshActions() {
        List<GameAction> actions = new ArrayList<>();
        actions.add(GameAction.EXPLORE);
        actions.add(GameAction.REST);
        actions.add(GameAction.INVENTORY);
        if (manager.getPlaceManager().hasDiscoveredPlaces()) {
            actions.add(GameAction.PLACES);
        }
        if (!manager.getPlayer().getQuests().isEmpty()) {
            actions.add(GameAction.QUESTS);
        }
        actions.add(GameAction.QUIT);
        manager.setActions(actions.toArray(new GameAction[0]));
    }

    private void explore() {
        WorldView world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        Place discovered = manager.getPlaceManager().tryDiscover();
        if (discovered != null) {
            world.addLogEntry("> " + discovered.getDiscoveryMessage(), TextColor.ANSI.GREEN_BRIGHT);
            world.addLogEntry("> \"" + discovered.getName() + "\" is now available in Places!", TextColor.ANSI.CYAN_BRIGHT);
            refreshActions();
            return;
        }

        Quest ratQuest = player.getQuestById(FarmState.RAT_QUEST_ID);
        boolean ratQuestActive = ratQuest != null && !ratQuest.isCompleted();

        if (ratQuestActive && explorationService.shouldSpawnQuestRat()) {
            Enemy enemy = EnemyRegistry.giantRat();
            world.addLogEntry("> A " + enemy.getName() + " scurries out of the bushes!", TextColor.ANSI.RED_BRIGHT);
            manager.transitionTo(manager.newCombatState(enemy, () -> {
                ratQuest.incrementProgress();
                WorldView w = manager.getWorldPanel();
                w.addLogEntry("> Quest progress: " + ratQuest.getProgressText() + " Giant Rats slain.",
                        TextColor.ANSI.CYAN_BRIGHT);
                if (ratQuest.isCompleted()) {
                    w.addLogEntry("> Quest complete! Return to the farmer for your reward.",
                            TextColor.ANSI.YELLOW_BRIGHT);
                }
            }));
            return;
        }

        if (CombatEngine.shouldEncounter()) {
            Enemy enemy = CombatEngine.randomEnemy();
            world.addLogEntry("> A " + enemy.getName() + " appears!", TextColor.ANSI.RED_BRIGHT);
            manager.transitionTo(manager.newCombatState(enemy));
        } else {
            String event = explorationService.randomExploreEvent(EXPLORE_EVENTS);
            world.addLogEntry("> " + event, TextColor.ANSI.WHITE);

            int goldFound = explorationService.rollGoldFound();
            if (goldFound > 0) {
                player.setGold(player.getGold() + goldFound);
                world.addLogEntry("> You found " + goldFound + " gold!", TextColor.ANSI.YELLOW);
                manager.syncStats();
            }
        }
    }

    private void rest() {
        Player player = manager.getPlayer();
        WorldView world = manager.getWorldPanel();

        int hpRestore = Math.min(5, player.getMaxHp() - player.getHp());
        int mpRestore = Math.min(3, player.getMaxMp() - player.getMp());
        player.setHp(player.getHp() + hpRestore);
        player.setMp(player.getMp() + mpRestore);
        manager.syncStats();

        if (hpRestore == 0 && mpRestore == 0) {
            world.addLogEntry("> You rest but you're already fully restored.", TextColor.ANSI.WHITE);
        } else {
            world.addLogEntry("> You rest and recover " + hpRestore + " HP and " + mpRestore + " MP.",
                    TextColor.ANSI.GREEN);
        }
    }

    private void inventory() {
        manager.getWorldPanel().addLogEntry("> Your bag is empty for now.", TextColor.ANSI.WHITE);
    }
}

