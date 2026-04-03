package be.cevada.state.services;

public class VillageService {

    public static final int CHURCH_HEAL_COST = 10;
    public static final int INN_REST_COST = 15;

    public int blacksmithUpgradeCost(int level) {
        return 30 * level;
    }

    public boolean canAfford(int gold, int cost) {
        return gold >= cost;
    }
}

