package be.cevada.screens;

import be.cevada.ThemeManager;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

public class MainMenuScreen extends AbstractScreen {

    private final ThemeManager themeManager;
    private boolean startGame = false;

    public MainMenuScreen(MultiWindowTextGUI gui, ThemeManager themeManager) {
        super(gui);
        this.themeManager = themeManager;
    }

    public boolean showAndGetResult() {
        startGame = false;
        show();
        return startGame;
    }

    @Override
    protected Component buildContent() {
        Panel root = new Panel();
        root.setLayoutManager(new BorderLayout());

        Panel menuPanel = new Panel();
        menuPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 3)));

        Label titleLabel = new Label("=== TERMINAL RPG ===");
        titleLabel.setForegroundColor(TextColor.ANSI.YELLOW);
        titleLabel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        menuPanel.addComponent(titleLabel);

        menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Label subtitleLabel = new Label("A text-based adventure");
        subtitleLabel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        menuPanel.addComponent(subtitleLabel);

        menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 3)));

        Button newGameBtn = new Button("  New Game  ", () -> {
            startGame = true;
            close();
        });
        newGameBtn.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        menuPanel.addComponent(newGameBtn);

        menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Button loadGameBtn = new Button("  Load Game  ", () ->
                MessageDialog.showMessageDialog(gui, "Load Game",
                        "Save/Load is not available yet.",
                        MessageDialogButton.OK));
        loadGameBtn.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        menuPanel.addComponent(loadGameBtn);

        menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Button settingsBtn = new Button("  Settings  ", () -> {
            SettingsScreen settings = new SettingsScreen(gui, themeManager);
            settings.show();
        });
        settingsBtn.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        menuPanel.addComponent(settingsBtn);

        menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Button quitBtn = new Button("    Quit    ", () -> {
            startGame = false;
            close();
        });
        quitBtn.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        menuPanel.addComponent(quitBtn);

        root.addComponent(menuPanel, BorderLayout.Location.CENTER);
        return root;
    }
}
