package be.cevada.combat;

import be.cevada.models.Enemy;
import be.cevada.models.Player;

import java.util.Random;

public class CombatEngine {

    private static final Random RNG = new Random();

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
        return RNG.nextInt(100) < Math.max(20, Math.min(80, chance));
    }

    public static int playerAttack(Player player, Enemy enemy) {
        int damage = calcDamage(player.getAtk(), enemy.getDef());
        enemy.setHp(enemy.getHp() - damage);
        return damage;
    }

    public static int playerSpecial(Player player, Enemy enemy) {
        int damage = calcSpecialDamage(player.getAtk(), enemy.getDef());
        enemy.setHp(enemy.getHp() - damage);
        int mpCost = 4;
        player.setMp(player.getMp() - mpCost);
        return damage;
    }

    public static int enemyAttack(Enemy enemy, Player player) {
        int damage;
        if (player.isDefending()) {
            damage = calcDefendedDamage(enemy.getAtk(), player.getDef());
            player.setDefending(false);
        } else {
            damage = calcDamage(enemy.getAtk(), player.getDef());
        }
        player.setHp(player.getHp() - damage);
        return damage;
    }

    public static Enemy randomEnemy() {
        return switch (RNG.nextInt(4)) {
            case 0 -> Enemy.wolf();
            case 1 -> Enemy.goblin();
            case 2 -> Enemy.skeleton();
            default -> Enemy.bandit();
        };
    }

    public static boolean shouldEncounter() {
        return RNG.nextInt(100) < 60;
    }
}

