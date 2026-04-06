package be.cevada.state;

import be.cevada.combat.CombatEngine.CombatResult;
import be.cevada.models.Enemy;
import be.cevada.models.Enemy.BehaviorType;
import be.cevada.models.Player;
import be.cevada.panels.WorldView;
import be.cevada.state.services.CombatLogFormatter;
import be.cevada.state.services.CombatLogFormatter.LogLine;
import be.cevada.state.services.CombatResolver;
import be.cevada.state.services.CombatResolver.ActOutcome;
import be.cevada.state.services.CombatResolver.AttackOutcome;
import com.googlecode.lanterna.TextColor;

import java.util.ArrayList;
import java.util.List;

public class CombatState implements GameState {

    private final CombatDeps manager;
    private final Enemy enemy;
    private final Runnable onDefeat;
    private final CombatResolver resolver;
    private final CombatLogFormatter logFormatter;

    public CombatState(CombatDeps manager, Enemy enemy) {
        this(manager, enemy, null);
    }

    public CombatState(CombatDeps manager, Enemy enemy, Runnable onDefeat) {
        this.manager = manager;
        this.enemy = enemy;
        this.onDefeat = onDefeat;
        this.resolver = new CombatResolver();
        this.logFormatter = new CombatLogFormatter();
    }

    @Override
    public void enter() {
        WorldView world = manager.getWorldPanel();
        world.setLocation("Combat - " + enemy.getName());
        updateEnemyDescription();
        refreshCombatActions();
    }

    @Override
    public void exit() {
        manager.getPlayer().setDefending(false);
    }

    @Override
    public void handleAction(GameAction action) {
        switch (action) {
            case ATTACK -> doAttack();
            case SPECIAL -> doSpecial();
            case DEFEND -> doDefend();
            case USE_POTION -> doPotion();
            case INVENTORY -> doInventory();
            case RUN -> doRun();
            case ACT -> doInteraction();
            default -> {
            }
        }
    }

    private void doAttack() {
        Player player = manager.getPlayer();
        AttackOutcome outcome = resolver.playerAttack(player, enemy);
        applyLogs(logFormatter.playerAttack(enemy, outcome));

        if (checkEnemyDead()) return;
        enemyTurn();
        checkPlayerDead();
    }

    private void doSpecial() {
        Player player = manager.getPlayer();

        if (player.getMp() < 4) {
            applyLog(logFormatter.notEnoughMp());
            return;
        }

        CombatResult result = resolver.playerSpecial(player, enemy);
        applyLogs(logFormatter.playerSpecial(enemy, result));
        manager.syncStats();

        if (checkEnemyDead()) return;
        enemyTurn();
        checkPlayerDead();
    }

    private void doDefend() {
        Player player = manager.getPlayer();
        player.setDefending(true);
        applyLog(logFormatter.braceForAttack());

        enemyTurn();
        checkPlayerDead();
    }

    private void doPotion() {
        Player player = manager.getPlayer();

        if (player.usePotion()) {
            applyLog(logFormatter.potionUsed(player.getPotions()));
            manager.syncStats();
            refreshCombatActions();
            enemyTurn();
            checkPlayerDead();
        } else {
            applyLog(logFormatter.noPotions());
        }
    }

    private void doInventory() {
        applyLog(logFormatter.inventoryPotions(manager.getPlayer().getPotions()));
    }

    private void doRun() {
        Player player = manager.getPlayer();

        if (resolver.tryFlee(player, enemy)) {
            applyLog(logFormatter.fleeSuccess(enemy));
            manager.transitionTo(manager.newExploringState());
        } else {
            applyLog(logFormatter.fleeFail());
            enemyTurn();
            checkPlayerDead();
        }
    }

    private void doInteraction() {
        ActOutcome outcome = resolver.act(enemy);
        applyLogs(logFormatter.act(enemy, outcome));

        enemyTurn();
        checkPlayerDead();
    }

    private void enemyTurn() {
        Player player = manager.getPlayer();

        boolean wasDefending = player.isDefending();
        CombatResult result = resolver.enemyAttack(enemy, player);
        applyLogs(logFormatter.enemyTurn(enemy, result, wasDefending));

        manager.syncStats();
        updateEnemyDescription();
    }

    private boolean checkEnemyDead() {
        if (!enemy.isAlive()) {
            WorldView world = manager.getWorldPanel();
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
            manager.transitionTo(manager.newExploringState());
            return true;
        }
        return false;
    }

    private boolean checkPlayerDead() {
        if (!manager.getPlayer().isAlive()) {
            WorldView world = manager.getWorldPanel();
            world.addLogEntry("> You have been slain by the " + enemy.getName() + "...",
                    TextColor.ANSI.RED_BRIGHT);
            world.addLogEntry("> GAME OVER", TextColor.ANSI.RED_BRIGHT);
            manager.getActionsPanel().clearActions();
            manager.getActionsPanel().addButton(GameAction.RETURN_TO_MENU.label(), manager.getOnQuit());
            return true;
        }
        return false;
    }

    private void refreshCombatActions() {
        Player player = manager.getPlayer();
        BehaviorType type = enemy.getBehaviorType();

        List<GameAction> actions = new ArrayList<>();
        actions.add(GameAction.ATTACK);
        actions.add(GameAction.SPECIAL);
        actions.add(GameAction.DEFEND);

        if (type != BehaviorType.VERMIN) {
            actions.add(GameAction.ACT);
        }

        if (player.getPotions() > 0) {
            actions.add(GameAction.USE_POTION);
        }
        actions.add(GameAction.RUN);

        manager.setActions(actions.toArray(new GameAction[0]));
    }

    private void updateEnemyDescription() {
        String weakenedNote = enemy.isWeakened() ? "  [GUARD DOWN]" : "";
        manager.getWorldPanel().setDescription(
                enemy.getName() + "  HP: " + enemy.getHp() + " / " + enemy.getMaxHp() +
                "\nATK: " + enemy.getAtk() + "  DEF: " + enemy.getDef() + weakenedNote
        );
    }

    private void applyLog(LogLine line) {
        manager.getWorldPanel().addLogEntry(line.text(), line.color());
    }

    private void applyLogs(List<LogLine> lines) {
        for (LogLine line : lines) {
            applyLog(line);
        }
    }
}
