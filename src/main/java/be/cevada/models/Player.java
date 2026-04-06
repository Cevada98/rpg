package be.cevada.models;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int hp;
    private int maxHp;
    private int mp;
    private int maxMp;
    private int atk;
    private int def;
    private int spd;
    private int gold;
    private int level;
    private int xp;
    private int xpToNext;
    private boolean defending;
    private int potions;
    private final List<Quest> quests = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        this.maxHp = 20;
        this.hp = maxHp;
        this.maxMp = 10;
        this.mp = maxMp;
        this.atk = 5;
        this.def = 3;
        this.spd = 4;
        this.gold = 10;
        this.level = 1;
        this.xp = 0;
        this.xpToNext = 50;
        this.defending = false;
        this.potions = 3;
    }

    public boolean addXp(int amount) {
        xp += amount;
        if (xp >= xpToNext) {
            xp -= xpToNext;
            level++;
            xpToNext = 50 * level;
            maxHp += 5;
            hp = maxHp;
            maxMp += 3;
            mp = maxMp;
            atk += 2;
            def += 1;
            spd += 1;
            return true;
        }
        return false;
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHp)); }
    public int getMaxHp() { return maxHp; }
    public int getMp() { return mp; }
    public void setMp(int mp) { this.mp = Math.max(0, Math.min(mp, maxMp)); }
    public int getMaxMp() { return maxMp; }
    public int getAtk() { return atk; }
    public void boostAtk(int amount) { this.atk += amount; }
    public int getDef() { return def; }
    public int getSpd() { return spd; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    public int getXpToNext() { return xpToNext; }
    public boolean isDefending() { return defending; }
    public void setDefending(boolean defending) { this.defending = defending; }
    public boolean isAlive() { return hp > 0; }

    public List<Quest> getQuests() { return quests; }

    public void addQuest(Quest quest) { quests.add(quest); }

    public Quest getQuestById(String id) {
        return quests.stream().filter(q -> q.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean hasQuest(String id) {
        return quests.stream().anyMatch(q -> q.getId().equals(id));
    }

    public int getPotions() { return potions; }
    public void setPotions(int potions) { this.potions = potions; }

    public boolean usePotion() {
        if (potions <= 0) return false;
        potions--;
        setHp(hp + 10);
        return true;
    }
}
