package be.cevada.state;

import be.cevada.panels.WorldView;
import com.googlecode.lanterna.TextColor;

import java.util.List;

public class DialogState implements GameState {

    private final GameContext manager;
    private final String npcName;
    private final List<String> lines;
    private int currentLine;

    public DialogState(GameContext manager, String npcName, List<String> lines) {
        this.manager = manager;
        this.npcName = npcName;
        this.lines = lines;
        this.currentLine = 0;
    }

    @Override
    public void enter() {
        WorldView world = manager.getWorldPanel();
        world.setLocation("Talking to " + npcName);
        world.clearLog();
        showCurrentLine();
    }

    @Override
    public void exit() {
    }

    @Override
    public void handleAction(GameAction action) {
        if (action == GameAction.CONTINUE) {
            currentLine++;
            if (currentLine < lines.size()) {
                showCurrentLine();
            } else {
                manager.getWorldPanel().addLogEntry("> " + npcName + " walks away.", TextColor.ANSI.WHITE);
                manager.transitionTo(new ExploringState(manager));
            }
        } else if (action == GameAction.LEAVE) {
            manager.transitionTo(new ExploringState(manager));
        }
    }

    private void showCurrentLine() {
        WorldView world = manager.getWorldPanel();
        world.setDescription(npcName + ": \"" + lines.get(currentLine) + "\"");

        if (currentLine < lines.size() - 1) {
            manager.setActions(GameAction.CONTINUE, GameAction.LEAVE);
        } else {
            manager.setActions(GameAction.LEAVE);
        }
    }
}

