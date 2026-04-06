package be.cevada.state;

public interface StateNavigator {
    void transitionTo(GameState next);
}

