package be.cevada.state;

import be.cevada.models.Enemy;
import be.cevada.models.PlaceManager;
import be.cevada.models.Player;
import be.cevada.panels.ActionView;
import be.cevada.panels.StatsView;
import be.cevada.panels.WorldView;
import be.cevada.presenters.PlayerStatsPresenter;

import java.util.List;

public class GameStateManager implements ExploringDeps,
        CombatDeps,
        PlacesDeps,
        VillageDeps,
        FarmDeps,
        DialogDeps,
        QuestDeps {

    private final Player player;
    private final StatsView statsPanel;
    private final WorldView worldPanel;
    private final ActionView actionsPanel;
    private final Runnable onQuit;
    private final PlaceManager placeManager;
    private GameState currentState;

    public GameStateManager(Player player, StatsView statsPanel, WorldView worldPanel,
                            ActionView actionsPanel, Runnable onQuit, PlaceManager placeManager) {
        this.player = player;
        this.statsPanel = statsPanel;
        this.worldPanel = worldPanel;
        this.actionsPanel = actionsPanel;
        this.onQuit = onQuit;
        this.placeManager = placeManager;
    }

    @Override
    public void transitionTo(GameState next) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = next;
        currentState.enter();
    }

    @Override
    public void setActions(GameAction... actions) {
        actionsPanel.clearActions();
        for (GameAction action : actions) {
            actionsPanel.addButton(action.label(), () -> {
                if (currentState != null) {
                    currentState.handleAction(action);
                }
            });
        }
        actionsPanel.focusFirst();
    }

    @Override
    public void syncStats() {
        PlayerStatsPresenter.sync(player, statsPanel);
    }

    @Override
    public Player getPlayer() { return player; }

    @Override
    public WorldView getWorldPanel() { return worldPanel; }

    @Override
    public ActionView getActionsPanel() { return actionsPanel; }

    @Override
    public Runnable getOnQuit() { return onQuit; }

    @Override
    public PlaceManager getPlaceManager() { return placeManager; }

    @Override
    public ExploringState newExploringState() {
        return new ExploringState(this);
    }

    @Override
    public CombatState newCombatState(Enemy enemy) {
        return new CombatState(this, enemy);
    }

    @Override
    public CombatState newCombatState(Enemy enemy, Runnable onDefeat) {
        return new CombatState(this, enemy, onDefeat);
    }

    @Override
    public PlacesState newPlacesState() {
        return new PlacesState(this);
    }

    @Override
    public VillageState newVillageState() {
        return new VillageState(this);
    }

    @Override
    public FarmState newFarmState() {
        return new FarmState(this);
    }

    @Override
    public QuestState newQuestState() {
        return new QuestState(this);
    }

    @Override
    public DialogState newDialogState(String npcName, List<String> lines) {
        return new DialogState(this, npcName, lines);
    }
}
