package be.cevada.data;

import be.cevada.models.Enemy;
import be.cevada.models.Enemy.BehaviorType;

import java.util.List;
import java.util.Random;

public final class EnemyRegistry {

    private static final Random RNG = GameRandom.get();

    private static final List<Enemy> RANDOM_POOL = List.of(
            wolf(), goblin(), skeleton(), bandit()
    );

    private EnemyRegistry() {}

    public static Enemy wolf() {
        return new Enemy("Wolf", 12, 4, 1, 15, 5, BehaviorType.BEAST);
    }

    public static Enemy goblin() {
        return new Enemy("Goblin", 18, 5, 2, 25, 12, BehaviorType.HUMANOID);
    }

    public static Enemy skeleton() {
        return new Enemy("Skeleton", 18, 5, 2, 35, 18, BehaviorType.UNDEAD);
    }

    public static Enemy bandit() {
        return new Enemy("Bandit", 24, 6, 3, 50, 25, BehaviorType.HUMANOID);
    }

    public static Enemy giantRat() {
        return new Enemy("Giant Rat", 10, 3, 1, 10, 3, BehaviorType.VERMIN);
    }

    public static Enemy random() {
        return copyOf(RANDOM_POOL.get(RNG.nextInt(RANDOM_POOL.size())));
    }

    private static Enemy copyOf(Enemy template) {
        return new Enemy(
                template.getName(),
                template.getMaxHp(),
                template.getAtk(),
                template.getDef(),
                template.getXpReward(),
                template.getGoldReward(),
                template.getBehaviorType()
        );
    }
}

