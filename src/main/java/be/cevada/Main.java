package be.cevada;

import be.cevada.screens.GameScreen;
import be.cevada.screens.MainMenuScreen;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.Font;
import java.io.IOException;


public class Main {

    static void main() {
        try {
            Screen screen = createScreen();

            screen.startScreen();

            ThemeManager themeManager = new ThemeManager();

            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen,
                    new DefaultWindowManager(),
                    new EmptySpace());

            themeManager.applyTheme(gui);

            boolean running = true;
            while (running) {
                MainMenuScreen menu = new MainMenuScreen(gui, themeManager);
                boolean startGame = menu.showAndGetResult();

                if (startGame) {
                    GameScreen game = new GameScreen(gui);
                    game.show();
                } else {
                    running = false;
                }
            }

            screen.stopScreen();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Screen createScreen() throws IOException {
        Font terminalFont = new Font("Cascadia Mono", Font.PLAIN, 18);
        if (!terminalFont.getFamily().equals("Cascadia Mono")) {
            terminalFont = new Font("Consolas", Font.PLAIN, 18);
        }

        SwingTerminalFontConfiguration fontConfig =
                SwingTerminalFontConfiguration.newInstance(terminalFont);

        return new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(120, 35))
                .setTerminalEmulatorFontConfiguration(fontConfig)
                .createScreen();
    }
}
