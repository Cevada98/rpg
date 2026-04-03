package be.cevada.state;

import be.cevada.combat.CombatEngine;
import be.cevada.models.Enemy;
import be.cevada.models.Player;
import be.cevada.panels.WorldPanel;
import com.googlecode.lanterna.TextColor;

public class CombatState implements GameState {

    private final GameStateManager manager;
    private final Enemy enemy;
    private final Runnable onDefeat;

    public CombatState(GameStateManager manager, Enemy enemy) {
        this(manager, enemy, null);
    }

    public CombatState(GameStateManager manager, Enemy enemy, Runnable onDefeat) {
        this.manager = manager;
        this.enemy = enemy;
        this.onDefeat = onDefeat;
    }

    @Override
    public void enter() {
        WorldPanel world = manager.getWorldPanel();
        world.setLocation("Combat - " + enemy.getName());
        updateEnemyDescription();
        manager.setActions("Attack", "Special", "Defend", "Inventory", "Run");
    }

    @Override
    public void exit() {
        manager.getPlayer().setDefending(false);
    }

    @Override
    public void handleAction(String actionLabel) {
        switch (actionLabel) {
            case "Attack" -> doAttack();
            case "Special" -> doSpecial();
            case "Defend" -> doDefend();
            case "Inventory" -> doInventory();
            case "Run" -> doRun();
        }
    }

    private void doAttack() {
        WorldPanel world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        int damage = CombatEngine.playerAttack(player, enemy);
        world.addLogEntry("> You attack the " + enemy.getName() + " for " + damage + " damage!",
                TextColor.ANSI.YELLOW);

        if (checkEnemyDead()) return;
        enemyTurn();
        checkPlayerDead();
    }

    private void doSpecial() {
        WorldPanel world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        if (player.getMp() < 4) {
            world.addLogEntry("> Not enough MP!", TextColor.ANSI.BLUE_BRIGHT);
            return;
        }

        int damage = CombatEngine.playerSpecial(player, enemy);
        world.addLogEntry("> You unleash a special attack for " + damage + " damage! (-4 MP)",
                TextColor.ANSI.CYAN_BRIGHT);
        manager.syncStats();

        if (checkEnemyDead()) return;
        enemyTurn();
        checkPlayerDead();
    }

    private void doDefend() {
        WorldPanel world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        player.setDefending(true);
        world.addLogEntry("> You brace yourself for the next attack.", TextColor.ANSI.WHITE_BRIGHT);

        enemyTurn();
        checkPlayerDead();
    }

    private void doInventory() {
        manager.getWorldPanel().addLogEntry("> No items to use.", TextColor.ANSI.WHITE);
    }

    private void doRun() {
        WorldPanel world = manager.getWorldPanel();
        Player player = manager.getPlayer();

        if (CombatEngine.tryFlee(player.getSpd(), enemy.getAtk())) {
            world.addLogEntry("> You fled from the " + enemy.getName() + "!", TextColor.ANSI.GREEN);
            manager.transitionTo(new ExploringState(manager));
        } else {
            world.addLogEntry("> You failed to escape!", TextColor.ANSI.RED_BRIGHT);
            enemyTurn();
            checkPlayerDead();
        }
    }

    private void enemyTurn() {
        Player player = manager.getPlayer();
        WorldPanel world = manager.getWorldPanel();

        int damage = CombatEngine.enemyAttack(enemy, player);
        if (player.isDefending()) {
            world.addLogEntry("> The " + enemy.getName() + " attacks but you block some! " + damage + " damage.",
                    TextColor.ANSI.RED);
        } else {
            world.addLogEntry("> The " + enemy.getName() + " attacks you for " + damage + " damage!",
                    TextColor.ANSI.RED);
        }
        manager.syncStats();
        updateEnemyDescription();
    }

    private boolean checkEnemyDead() {
        if (!enemy.isAlive()) {
            WorldPanel world = manager.getWorldPanel();
            Player player = manager.getPlayer();

            world.addLogEntry("> The " + enemy.getName() + " has been defeated!", TextColor.ANSI.GREEN_BRIGHT);

            player.setGold(player.getGold() + enemy.getGoldReward());
            world.addLogEntry("> You gained " + enemy.getGoldReward() + " gold.", TextColor.ANSI.YELLOW);

            boolean leveled = player.addXp(enemy.getXpReward());
            world.addLogEntry("> You gained " + enemy.getXpReward() + " XP.", TextColor.ANSI.CYAN);

            if (leveled) {
                world.addLogEntry("> LEVEL UP! You are now level " + player.getLevel() + "!",
                        TextColor.ANSI.YELLOW_BRIGHT);
            }

            if (onDefeat != null) {
                onDefeat.run();
            }

            manager.syncStats();
            manager.transitionTo(new ExploringState(manager));
            return true;
        }
        return false;
    }

    private boolean checkPlayerDead() {
        if (!manager.getPlayer().isAlive()) {
            WorldPanel world = manager.getWorldPanel();
            world.addLogEntry("> You have been slain by the " + enemy.getName() + "...",
                    TextColor.ANSI.RED_BRIGHT);
            world.addLogEntry("> GAME OVER", TextColor.ANSI.RED_BRIGHT);
            manager.getActionsPanel().clearActions();
            manager.getActionsPanel().addButton("Return to Menu", manager.getOnQuit());
            return true;
        }
        return false;
    }

    private void updateEnemyDescription() {
        manager.getWorldPanel().setDescription(
                enemy.getName() + "  HP: " + enemy.getHp() + " / " + enemy.getMaxHp() +
                "\nATK: " + enemy.getAtk() + "  DEF: " + enemy.getDef()
        );
    }
}


