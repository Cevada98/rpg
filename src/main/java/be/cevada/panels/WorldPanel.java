package be.cevada.panels;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.LinearLayout;

/**
 * Center/right panel showing location, description, and event log.
 */
public class WorldPanel {

    private final Panel panel;

    private final Label locationLabel;
    private final Label descLabel;
    private final Label logTitle;
    private final Panel logContent;

    public WorldPanel() {
        Panel content = new Panel();
        content.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        locationLabel = new Label("Dark Forest");
        locationLabel.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        locationLabel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        content.addComponent(locationLabel);

        content.addComponent(new Separator(Direction.HORIZONTAL)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill)));
        content.addComponent(new EmptySpace(TerminalSize.ONE));

        descLabel = new Label("Twisted trees block the moonlight. You hear\nsomething rustling in the undergrowth...");
        descLabel.setForegroundColor(TextColor.ANSI.WHITE);
        content.addComponent(descLabel);

        content.addComponent(new EmptySpace(TerminalSize.ONE));
        content.addComponent(new Separator(Direction.HORIZONTAL)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill)));
        content.addComponent(new EmptySpace(TerminalSize.ONE));

        logTitle = new Label("  ~ Event Log ~");
        logTitle.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT);
        content.addComponent(logTitle);

        content.addComponent(new EmptySpace(TerminalSize.ONE));

        // Log entries container — easy to clear and repopulate
        logContent = new Panel();
        logContent.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        content.addComponent(logContent);

        // Add some default log entries
        addLogEntry("> You entered the Dark Forest.", TextColor.ANSI.WHITE);
        addLogEntry("> A wolf appears!", TextColor.ANSI.RED_BRIGHT);
        addLogEntry("> You strike the wolf for 5 damage.", TextColor.ANSI.YELLOW);
        addLogEntry("> The wolf flees into the shadows.", TextColor.ANSI.WHITE);

        // Wrap in bordered panel — BorderLayout.CENTER fills remaining space
        panel = new Panel();
        panel.setLayoutManager(new BorderLayout());
        panel.addComponent(
                content.withBorder(Borders.doubleLine(" World ")),
                BorderLayout.Location.CENTER
        );
    }

    /** Returns the assembled panel, ready to be added to a layout. */
    public Panel getPanel() {
        return panel;
    }

    // --- Update methods for game logic ---

    public void setLocation(String location) {
        locationLabel.setText(location);
    }

    public void setDescription(String description) {
        descLabel.setText(description);
    }

    public void addLogEntry(String text, TextColor color) {
        Label entry = new Label("  " + text);
        entry.setForegroundColor(color);
        logContent.addComponent(entry);
    }

    public void clearLog() {
        logContent.removeAllComponents();
    }
}

