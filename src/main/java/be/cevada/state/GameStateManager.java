package be.cevada.state;

import be.cevada.models.PlaceManager;
import be.cevada.models.Player;
import be.cevada.panels.ActionsPanel;
import be.cevada.panels.StatsPanel;
import be.cevada.panels.WorldPanel;

public class GameStateManager {

    private final Player player;
    private final StatsPanel statsPanel;
    private final WorldPanel worldPanel;
    private final ActionsPanel actionsPanel;
    private final Runnable onQuit;
    private final PlaceManager placeManager;
    private GameState currentState;

    public GameStateManager(Player player, StatsPanel statsPanel, WorldPanel worldPanel,
                            ActionsPanel actionsPanel, Runnable onQuit) {
        this.player = player;
        this.statsPanel = statsPanel;
        this.worldPanel = worldPanel;
        this.actionsPanel = actionsPanel;
        this.onQuit = onQuit;
        this.placeManager = new PlaceManager();
    }

    public void transitionTo(GameState next) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = next;
        currentState.enter();
    }

    public void setActions(String... labels) {
        actionsPanel.clearActions();
        for (String label : labels) {
            actionsPanel.addButton(label, () -> {
                if (currentState != null) {
                    currentState.handleAction(label);
                }
            });
        }
    }

    public void syncStats() {
        player.syncToPanel(statsPanel);
    }

    public Player getPlayer() { return player; }
    public StatsPanel getStatsPanel() { return statsPanel; }
    public WorldPanel getWorldPanel() { return worldPanel; }
    public ActionsPanel getActionsPanel() { return actionsPanel; }
    public Runnable getOnQuit() { return onQuit; }
    public PlaceManager getPlaceManager() { return placeManager; }
}

