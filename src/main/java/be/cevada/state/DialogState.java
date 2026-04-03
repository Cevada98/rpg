package be.cevada.state;

import be.cevada.panels.WorldPanel;
import com.googlecode.lanterna.TextColor;

import java.util.List;

public class DialogState implements GameState {

    private final GameStateManager manager;
    private final String npcName;
    private final List<String> lines;
    private int currentLine;

    public DialogState(GameStateManager manager, String npcName, List<String> lines) {
        this.manager = manager;
        this.npcName = npcName;
        this.lines = lines;
        this.currentLine = 0;
    }

    @Override
    public void enter() {
        WorldPanel world = manager.getWorldPanel();
        world.setLocation("Talking to " + npcName);
        world.clearLog();
        showCurrentLine();
    }

    @Override
    public void exit() {
    }

    @Override
    public void handleAction(String actionLabel) {
        if ("Continue".equals(actionLabel)) {
            currentLine++;
            if (currentLine < lines.size()) {
                showCurrentLine();
            } else {
                manager.getWorldPanel().addLogEntry("> " + npcName + " walks away.", TextColor.ANSI.WHITE);
                manager.transitionTo(new ExploringState(manager));
            }
        } else if ("Leave".equals(actionLabel)) {
            manager.transitionTo(new ExploringState(manager));
        }
    }

    private void showCurrentLine() {
        WorldPanel world = manager.getWorldPanel();
        world.setDescription(npcName + ": \"" + lines.get(currentLine) + "\"");

        if (currentLine < lines.size() - 1) {
            manager.setActions("Continue", "Leave");
        } else {
            manager.setActions("Leave");
        }
    }
}

