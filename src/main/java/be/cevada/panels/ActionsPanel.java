package be.cevada.panels;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.LinearLayout;

public class ActionsPanel implements ActionView {

    private final Panel panel;
    private final Panel actionsContent;
    private Button firstButton;

    public ActionsPanel() {
        actionsContent = new Panel();
        actionsContent.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        panel = new Panel();
        panel.setLayoutManager(new BorderLayout());
        panel.addComponent(
                actionsContent.withBorder(Borders.doubleLine(" Actions ")),
                BorderLayout.Location.CENTER
        );
    }

    public Panel getPanel() {
        return panel;
    }

    @Override
    public void clearActions() {
        actionsContent.removeAllComponents();
        firstButton = null;
    }

    @Override
    public void addButton(String label, Runnable action) {
        if (actionsContent.getChildCount() > 0) {
            actionsContent.addComponent(new EmptySpace(new TerminalSize(2, 1)));
        } else {
            actionsContent.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        }
        Button btn = new Button(label, action);
        if (firstButton == null) {
            firstButton = btn;
        }
        actionsContent.addComponent(btn);
    }

    @Override
    public void focusFirst() {
        if (firstButton != null) {
            firstButton.takeFocus();
        }
    }
}
