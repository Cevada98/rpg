package be.cevada.state.services;

import be.cevada.combat.CombatEngine.CombatResult;
import be.cevada.combat.CombatText;
import be.cevada.models.Enemy;
import be.cevada.models.Enemy.BehaviorType;
import com.googlecode.lanterna.TextColor;

import java.util.ArrayList;
import java.util.List;

public class CombatLogFormatter {

    public record LogLine(String text, TextColor color) {
    }

    public List<LogLine> playerAttack(Enemy enemy, CombatResolver.AttackOutcome outcome) {
        CombatResult result = outcome.result();
        List<LogLine> logs = new ArrayList<>();
        if (result.miss()) {
            logs.add(new LogLine("> You swing at the " + enemy.getName() + "... and miss!", TextColor.ANSI.WHITE));
            return logs;
        }

        String attackText = CombatText.playerAttackText(enemy.getBehaviorType());
        if (result.crit()) {
            logs.add(new LogLine("> ★ CRITICAL HIT! " + attackText + " (" + outcome.totalDamage() + " dmg!)",
                    TextColor.ANSI.YELLOW_BRIGHT));
        } else {
            logs.add(new LogLine("> " + attackText + " " + outcome.totalDamage() + " damage.", TextColor.ANSI.YELLOW));
        }
        logs.add(new LogLine("> " + CombatText.enemyReactionText(enemy.getBehaviorType()), TextColor.ANSI.WHITE));
        return logs;
    }

    public List<LogLine> playerSpecial(Enemy enemy, CombatResult result) {
        List<LogLine> logs = new ArrayList<>();
        String specialText = CombatText.playerSpecialText(enemy.getBehaviorType());
        if (result.crit()) {
            logs.add(new LogLine("> ★ CRITICAL! " + specialText + " (" + result.damage() + " dmg! -4 MP)",
                    TextColor.ANSI.CYAN_BRIGHT));
        } else {
            logs.add(new LogLine("> " + specialText + " " + result.damage() + " damage. (-4 MP)",
                    TextColor.ANSI.CYAN_BRIGHT));
        }
        logs.add(new LogLine("> " + CombatText.enemyReactionText(enemy.getBehaviorType()), TextColor.ANSI.WHITE));
        return logs;
    }

    public LogLine notEnoughMp() {
        return new LogLine("> Not enough MP!", TextColor.ANSI.BLUE_BRIGHT);
    }

    public LogLine braceForAttack() {
        return new LogLine("> You brace yourself for the next attack.", TextColor.ANSI.WHITE_BRIGHT);
    }

    public LogLine potionUsed(int left) {
        return new LogLine("> You drink a Health Potion. Restored 10 HP! (" + left + " left)", TextColor.ANSI.GREEN_BRIGHT);
    }

    public LogLine noPotions() {
        return new LogLine("> You have no potions left!", TextColor.ANSI.WHITE);
    }

    public LogLine inventoryPotions(int count) {
        return new LogLine("> Potions: " + count + " remaining.", TextColor.ANSI.WHITE);
    }

    public LogLine fleeSuccess(Enemy enemy) {
        return new LogLine("> You flee from the " + enemy.getName() + "!", TextColor.ANSI.GREEN);
    }

    public LogLine fleeFail() {
        return new LogLine("> You failed to escape!", TextColor.ANSI.RED_BRIGHT);
    }

    public List<LogLine> act(Enemy enemy, CombatResolver.ActOutcome actOutcome) {
        BehaviorType type = enemy.getBehaviorType();
        List<LogLine> logs = new ArrayList<>();
        logs.add(new LogLine("> " + CombatText.actText(type), TextColor.ANSI.WHITE));
        if (actOutcome.effective()) {
            String outcomeText = switch (type) {
                case UNDEAD -> "> It clacks its teeth... and its guard drops! (next attack bonus)";
                case BEAST -> "> The " + enemy.getName() + " hesitates, lowering its guard! (next attack bonus)";
                case HUMANOID -> "> The " + enemy.getName() + " is enraged and attacks recklessly! (next attack bonus)";
                default -> "";
            };
            logs.add(new LogLine(outcomeText, TextColor.ANSI.CYAN_BRIGHT));
        }
        return logs;
    }

    public List<LogLine> enemyTurn(Enemy enemy, CombatResult result, boolean wasDefending) {
        List<LogLine> logs = new ArrayList<>();
        String attackText = CombatText.enemyAttackText(enemy.getBehaviorType());
        if (result.miss()) {
            logs.add(new LogLine("> " + attackText + " — but misses you!", TextColor.ANSI.GREEN));
        } else if (wasDefending) {
            logs.add(new LogLine("> " + attackText + " You block! " + result.damage() + " damage.", TextColor.ANSI.RED));
        } else if (result.crit()) {
            logs.add(new LogLine("> ★ " + attackText + " CRITICAL! " + result.damage() + " damage!", TextColor.ANSI.RED_BRIGHT));
        } else {
            logs.add(new LogLine("> " + attackText + " " + result.damage() + " damage.", TextColor.ANSI.RED));
        }
        return logs;
    }
}

