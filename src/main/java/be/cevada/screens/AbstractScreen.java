package be.cevada.screens;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;

import java.util.List;

public abstract class AbstractScreen {

    protected final MultiWindowTextGUI gui;
    protected BasicWindow window;

    protected AbstractScreen(MultiWindowTextGUI gui) {
        this.gui = gui;
    }

    public void show() {
        window = new BasicWindow(getTitle());
        window.setHints(getHints());
        window.setComponent(buildContent());
        gui.addWindowAndWait(window);
    }

    public void close() {
        if (window != null) {
            window.close();
        }
    }

    protected String getTitle() {
        return "";
    }

    protected List<Window.Hint> getHints() {
        return List.of(Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS);
    }

    protected abstract Component buildContent();
}

