package be.cevada.screens;

import be.cevada.ThemeManager;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.LinearLayout;

import java.util.List;

public class SettingsScreen extends AbstractScreen {

    private final ThemeManager themeManager;

    public SettingsScreen(MultiWindowTextGUI gui, ThemeManager themeManager) {
        super(gui);
        this.themeManager = themeManager;
    }

    @Override
    protected String getTitle() {
        return "Settings";
    }

    @Override
    protected List<Window.Hint> getHints() {
        return List.of(Window.Hint.CENTERED);
    }

    @Override
    protected Component buildContent() {
        Panel content = new Panel();
        content.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Label themeTitle = new Label("Theme");
        themeTitle.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        themeTitle.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        content.addComponent(themeTitle);

        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        content.addComponent(new Separator(Direction.HORIZONTAL)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill)));

        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Label currentLabel = new Label(themeManager.isDarkMode() ? "Current: Dark Mode" : "Current: Light Mode");
        currentLabel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        content.addComponent(currentLabel);

        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Panel buttonRow = new Panel();
        buttonRow.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        buttonRow.addComponent(new Button(" Dark Mode ", () -> {
            themeManager.setDarkMode(true);
            themeManager.applyTheme(gui);
            currentLabel.setText("Current: Dark Mode");
        }));

        buttonRow.addComponent(new EmptySpace(new TerminalSize(2, 1)));

        buttonRow.addComponent(new Button(" Light Mode ", () -> {
            themeManager.setDarkMode(false);
            themeManager.applyTheme(gui);
            currentLabel.setText("Current: Light Mode");
        }));

        buttonRow.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        content.addComponent(buttonRow);

        content.addComponent(new EmptySpace(new TerminalSize(0, 2)));

        content.addComponent(new Separator(Direction.HORIZONTAL)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill)));

        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Button backBtn = new Button("   Back   ", this::close);
        backBtn.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        content.addComponent(backBtn);

        content.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        return content;
    }
}
