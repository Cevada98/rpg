package be.cevada.state;

public interface GameState {

    void enter();

    void exit();

    void handleAction(GameAction action);
}

