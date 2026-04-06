package be.cevada.screens;

import be.cevada.models.Player;
import be.cevada.models.PlaceManager;
import be.cevada.panels.ActionsPanel;
import be.cevada.panels.StatsPanel;
import be.cevada.panels.WorldPanel;
import be.cevada.presenters.PlayerStatsPresenter;
import be.cevada.state.ExploringState;
import be.cevada.state.GameStateManager;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.BorderLayout;

public class GameScreen extends AbstractScreen {

    private StatsPanel statsPanel;
    private WorldPanel worldPanel;
    private ActionsPanel actionsPanel;
    private GameStateManager stateManager;

    public GameScreen(MultiWindowTextGUI gui) {
        super(gui);
    }

    @Override
    protected Component buildContent() {
        statsPanel = new StatsPanel();
        worldPanel = new WorldPanel();
        actionsPanel = new ActionsPanel();

        Player player = new Player("Hero");
        PlaceManager placeManager = new PlaceManager();

        stateManager = new GameStateManager(player, statsPanel, worldPanel, actionsPanel, this::close, placeManager);
        PlayerStatsPresenter.sync(player, statsPanel);
        stateManager.transitionTo(new ExploringState(stateManager));

        Panel topArea = new Panel();
        topArea.setLayoutManager(new BorderLayout());
        topArea.addComponent(statsPanel.getPanel(), BorderLayout.Location.LEFT);
        topArea.addComponent(worldPanel.getPanel(), BorderLayout.Location.CENTER);

        Panel root = new Panel();
        root.setLayoutManager(new BorderLayout());
        root.addComponent(topArea, BorderLayout.Location.CENTER);
        root.addComponent(actionsPanel.getPanel(), BorderLayout.Location.BOTTOM);

        return root;
    }

    public StatsPanel getStatsPanel() { return statsPanel; }
    public WorldPanel getWorldPanel() { return worldPanel; }
    public ActionsPanel getActionsPanel() { return actionsPanel; }
    public GameStateManager getStateManager() { return stateManager; }
}
