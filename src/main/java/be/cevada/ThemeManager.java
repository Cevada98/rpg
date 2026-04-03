package be.cevada;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.graphics.SimpleTheme;

public class ThemeManager {

    private boolean darkMode;
    private final SimpleTheme darkTheme;
    private final SimpleTheme lightTheme;

    public ThemeManager() {
        this.darkMode = true;

        darkTheme = SimpleTheme.makeTheme(
                false,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLACK
        );

        lightTheme = SimpleTheme.makeTheme(
                false,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE
        );
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public SimpleTheme getCurrentTheme() {
        return darkMode ? darkTheme : lightTheme;
    }

    public void applyTheme(MultiWindowTextGUI gui) {
        gui.setTheme(getCurrentTheme());
    }

    public void toggleTheme(MultiWindowTextGUI gui) {
        darkMode = !darkMode;
        applyTheme(gui);
    }
}
