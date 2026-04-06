package be.cevada.state;

import be.cevada.models.Enemy;

import java.util.List;

public interface StateFactory {
    ExploringState newExploringState();

    CombatState newCombatState(Enemy enemy);

    CombatState newCombatState(Enemy enemy, Runnable onDefeat);

    PlacesState newPlacesState();

    VillageState newVillageState();

    FarmState newFarmState();

    QuestState newQuestState();

    DialogState newDialogState(String npcName, List<String> lines);
}

