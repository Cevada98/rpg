package be.cevada.state;

import be.cevada.models.PlaceManager;
import be.cevada.models.Player;
import be.cevada.panels.ActionView;
import be.cevada.panels.StatsView;
import be.cevada.panels.WorldView;
import be.cevada.presenters.PlayerStatsPresenter;

public class GameStateManager implements GameContext {

    private final Player player;
    private final StatsView statsPanel;
    private final WorldView worldPanel;
    private final ActionView actionsPanel;
    private final Runnable onQuit;
    private final PlaceManager placeManager;
    private GameState currentState;

    public GameStateManager(Player player, StatsView statsPanel, WorldView worldPanel,
                            ActionView actionsPanel, Runnable onQuit) {
        this.player = player;
        this.statsPanel = statsPanel;
        this.worldPanel = worldPanel;
        this.actionsPanel = actionsPanel;
        this.onQuit = onQuit;
        this.placeManager = new PlaceManager();
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
}

