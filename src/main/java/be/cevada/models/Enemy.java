package be.cevada.models;

public class Enemy {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int atk;
    private final int def;
    private final int xpReward;
    private final int goldReward;

    public Enemy(String name, int maxHp, int atk, int def, int xpReward, int goldReward) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.atk = atk;
        this.def = def;
        this.xpReward = xpReward;
        this.goldReward = goldReward;
    }

    public static Enemy wolf() {
        return new Enemy("Wolf", 12, 4, 1, 15, 5);
    }

    public static Enemy goblin() {
        return new Enemy("Goblin", 18, 5, 2, 25, 12);
    }

    public static Enemy skeleton() {
        return new Enemy("Skeleton", 18, 5, 2, 35, 18);
    }

    public static Enemy bandit() {
        return new Enemy("Bandit", 24, 6, 3, 50, 25);
    }

    public static Enemy giantRat() {
        return new Enemy("Giant Rat", 10, 3, 1, 10, 3);
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }
    public int getMaxHp() { return maxHp; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getXpReward() { return xpReward; }
    public int getGoldReward() { return goldReward; }
    public boolean isAlive() { return hp > 0; }
}

