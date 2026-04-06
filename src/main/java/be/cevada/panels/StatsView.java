package be.cevada.panels;

public interface StatsView {
    void setName(String name);

    void setHp(int current, int max);

    void setMp(int current, int max);

    void setAtk(int value);

    void setDef(int value);

    void setSpd(int value);

    void setGold(int value);

    void setLevel(int value);

    void setXp(int current, int max);

    void setPotions(int value);
}
