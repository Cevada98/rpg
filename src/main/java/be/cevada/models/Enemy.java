package be.cevada.models;

public class Enemy {

    public enum BehaviorType { BEAST, UNDEAD, HUMANOID, VERMIN }

    private final String name;
    private int hp;
    private final int maxHp;
    private final int atk;
    private final int def;
    private final int xpReward;
    private final int goldReward;
    private final BehaviorType behaviorType;
    private int weakenedTurns = 0;

    public Enemy(String name, int maxHp, int atk, int def, int xpReward, int goldReward, BehaviorType behaviorType) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.atk = atk;
        this.def = def;
        this.xpReward = xpReward;
        this.goldReward = goldReward;
        this.behaviorType = behaviorType;
    }

    public void weaken(int turns) {
        this.weakenedTurns = Math.max(this.weakenedTurns, turns);
    }

    public boolean consumeWeakened() {
        if (weakenedTurns > 0) {
            weakenedTurns--;
            return true;
        }
        return false;
    }

    public boolean isWeakened() { return weakenedTurns > 0; }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }
    public int getMaxHp() { return maxHp; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getXpReward() { return xpReward; }
    public int getGoldReward() { return goldReward; }
    public boolean isAlive() { return hp > 0; }
    public BehaviorType getBehaviorType() { return behaviorType; }
}
