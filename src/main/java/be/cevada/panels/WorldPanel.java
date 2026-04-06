package be.cevada.panels;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.LinearLayout;

public class WorldPanel implements WorldView {

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

        logContent = new Panel();
        logContent.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        content.addComponent(logContent);

        addLogEntry("> You entered the Dark Forest.", TextColor.ANSI.WHITE);
        addLogEntry("> A wolf appears!", TextColor.ANSI.RED_BRIGHT);
        addLogEntry("> You strike the wolf for 5 damage.", TextColor.ANSI.YELLOW);
        addLogEntry("> The wolf flees into the shadows.", TextColor.ANSI.WHITE);

        panel = new Panel();
        panel.setLayoutManager(new BorderLayout());
        panel.addComponent(
                content.withBorder(Borders.doubleLine(" World ")),
                BorderLayout.Location.CENTER
        );
    }

    public Panel getPanel() {
        return panel;
    }

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

