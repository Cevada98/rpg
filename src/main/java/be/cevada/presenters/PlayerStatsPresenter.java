package be.cevada.presenters;

import be.cevada.models.Player;
import be.cevada.panels.StatsView;

public final class PlayerStatsPresenter {

    private PlayerStatsPresenter() {
    }

    public static void sync(Player player, StatsView view) {
        view.setName(player.getName());
        view.setHp(player.getHp(), player.getMaxHp());
        view.setMp(player.getMp(), player.getMaxMp());
        view.setAtk(player.getAtk());
        view.setDef(player.getDef());
        view.setSpd(player.getSpd());
        view.setGold(player.getGold());
        view.setLevel(player.getLevel());
        view.setXp(player.getXp(), player.getXpToNext());
    }
}

