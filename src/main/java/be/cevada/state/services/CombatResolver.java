package be.cevada.state.services;

import be.cevada.combat.CombatEngine;
import be.cevada.combat.CombatEngine.CombatResult;
import be.cevada.models.Enemy;
import be.cevada.models.Enemy.BehaviorType;
import be.cevada.models.Player;

public class CombatResolver {

    public record AttackOutcome(CombatResult result, int bonusDamage) {
        public int totalDamage() {
            return result.damage() + bonusDamage;
        }
    }

    public record ActOutcome(boolean effective) {
    }

    public AttackOutcome playerAttack(Player player, Enemy enemy) {
        boolean wasWeakened = enemy.consumeWeakened();
        CombatResult result = CombatEngine.playerAttack(player, enemy);
        int bonusDamage = 0;
        if (wasWeakened && !result.miss()) {
            bonusDamage = Math.max(1, result.damage() / 2);
            enemy.setHp(enemy.getHp() - bonusDamage);
        }
        return new AttackOutcome(result, bonusDamage);
    }

    public CombatResult playerSpecial(Player player, Enemy enemy) {
        return CombatEngine.playerSpecial(player, enemy);
    }

    public CombatResult enemyAttack(Enemy enemy, Player player) {
        return CombatEngine.enemyAttack(enemy, player);
    }

    public boolean tryFlee(Player player, Enemy enemy) {
        return CombatEngine.tryFlee(player.getSpd(), enemy.getAtk());
    }

    public ActOutcome act(Enemy enemy) {
        BehaviorType type = enemy.getBehaviorType();
        boolean effective = (type != BehaviorType.VERMIN);
        if (effective) {
            enemy.weaken(1);
        }
        return new ActOutcome(effective);
    }
}

