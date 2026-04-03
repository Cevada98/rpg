package be.cevada;

import be.cevada.screens.GameScreen;
import be.cevada.screens.MainMenuScreen;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.Font;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        boolean forceSwing = false;
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--swing")) {
                forceSwing = true;
            }
        }

        try {
            Screen screen = createScreen(forceSwing);

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

    private static Screen createScreen(boolean forceSwing) throws IOException {
        Font terminalFont = new Font("Monospaced", Font.PLAIN, 18);
        SwingTerminalFontConfiguration fontConfig =
                SwingTerminalFontConfiguration.newInstance(terminalFont);

        DefaultTerminalFactory factory = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(120, 35))
                .setTerminalEmulatorFontConfiguration(fontConfig);

        Terminal terminal;

        if (forceSwing) {
            terminal = factory.createTerminalEmulator();
        } else {
            terminal = factory.createTerminal();
        }

        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        return screen;
    }
}
