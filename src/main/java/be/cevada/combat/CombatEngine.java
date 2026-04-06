package be.cevada.combat;

import be.cevada.data.EnemyRegistry;
import be.cevada.data.GameRandom;
import be.cevada.models.Enemy;
import be.cevada.models.Player;

import java.util.Random;

public class CombatEngine {

    private static final Random RNG = GameRandom.get();

    public static final double CRIT_CHANCE   = 0.10;
    public static final double MISS_CHANCE   = 0.08;
    public static final double CRIT_MULTIPLIER = 1.75;

    public record CombatResult(int damage, boolean crit, boolean miss) {}

    public static int calcDamage(int atk, int def) {
        int base = Math.max(1, atk - def);
        int variance = Math.max(1, atk / 2);
        return base + RNG.nextInt(variance + 1);
    }

    public static int calcDefendedDamage(int atk, int def) {
        int base = Math.max(1, (atk - def) / 2);
        int variance = Math.max(1, atk / 4);
        return base + RNG.nextInt(variance + 1);
    }

    public static int calcSpecialDamage(int atk, int def) {
        int base = Math.max(1, atk * 2 - def);
        int variance = Math.max(1, atk);
        return base + RNG.nextInt(variance + 1);
    }

    public static boolean tryFlee(int playerSpd, int enemyAtk) {
        int chance = 40 + (playerSpd * 5) - (enemyAtk * 3);
        return RNG.nextInt(100) < Math.clamp(chance, 20, 80);
    }


    public static CombatResult playerAttack(Player player, Enemy enemy) {
        if (RNG.nextDouble() < MISS_CHANCE) {
            return new CombatResult(0, false, true);
        }
        boolean crit = RNG.nextDouble() < CRIT_CHANCE;
        int damage = calcDamage(player.getAtk(), enemy.getDef());
        if (crit) damage = (int) Math.round(damage * CRIT_MULTIPLIER);
        enemy.setHp(enemy.getHp() - damage);
        return new CombatResult(damage, crit, false);
    }

    public static CombatResult playerSpecial(Player player, Enemy enemy) {
        boolean crit = RNG.nextDouble() < CRIT_CHANCE;
        int damage = calcSpecialDamage(player.getAtk(), enemy.getDef());
        if (crit) damage = (int) Math.round(damage * CRIT_MULTIPLIER);
        enemy.setHp(enemy.getHp() - damage);
        int mpCost = 4;
        player.setMp(player.getMp() - mpCost);
        return new CombatResult(damage, crit, false);
    }

    public static CombatResult enemyAttack(Enemy enemy, Player player) {
        if (player.isDefending()) {
            int damage = calcDefendedDamage(enemy.getAtk(), player.getDef());
            player.setDefending(false);
            player.setHp(player.getHp() - damage);
            return new CombatResult(damage, false, false);
        }
        if (RNG.nextDouble() < MISS_CHANCE / 2.0) {
            return new CombatResult(0, false, true);
        }
        boolean crit = RNG.nextDouble() < CRIT_CHANCE / 2.0;
        int damage = calcDamage(enemy.getAtk(), player.getDef());
        if (crit) damage = (int) Math.round(damage * CRIT_MULTIPLIER);
        player.setHp(player.getHp() - damage);
        return new CombatResult(damage, crit, false);
    }


    public static Enemy randomEnemy() {
        return EnemyRegistry.random();
    }

    public static boolean shouldEncounter() {
        return RNG.nextInt(100) < 60;
    }
}
