package be.cevada.state;

import be.cevada.models.PlaceManager;
import be.cevada.models.Player;
import be.cevada.panels.ActionView;
import be.cevada.panels.WorldView;

public interface GameContext {
    void transitionTo(GameState next);

    void setActions(GameAction... actions);

    void syncStats();

    Player getPlayer();

    WorldView getWorldPanel();

    ActionView getActionsPanel();

    Runnable getOnQuit();

    PlaceManager getPlaceManager();
}

