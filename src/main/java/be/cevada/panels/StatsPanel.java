package be.cevada.panels;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.LinearLayout;

public class StatsPanel implements StatsView {

    private final Panel panel;

    private final Label nameLabel;
    private final Label hpLabel;
    private final Label mpLabel;
    private final Label atkLabel;
    private final Label defLabel;
    private final Label spdLabel;
    private final Label goldLabel;
    private final Label lvlLabel;
    private final Label xpLabel;

    public StatsPanel() {
        Panel content = new Panel();
        content.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        content.addComponent(new EmptySpace(TerminalSize.ONE));

        nameLabel = new Label("  Hero  ");
        nameLabel.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        content.addComponent(nameLabel);

        content.addComponent(new EmptySpace(TerminalSize.ONE));
        content.addComponent(new Label("---------------"));
        content.addComponent(new EmptySpace(TerminalSize.ONE));

        hpLabel = new Label(" HP:   20 / 20");
        hpLabel.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        content.addComponent(hpLabel);

        mpLabel = new Label(" MP:   10 / 10");
        mpLabel.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
        content.addComponent(mpLabel);

        content.addComponent(new EmptySpace(TerminalSize.ONE));
        content.addComponent(new Label("---------------"));
        content.addComponent(new EmptySpace(TerminalSize.ONE));

        atkLabel = new Label(" ATK:  5");
        atkLabel.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        content.addComponent(atkLabel);

        defLabel = new Label(" DEF:  3");
        defLabel.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        content.addComponent(defLabel);

        spdLabel = new Label(" SPD:  4");
        spdLabel.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        content.addComponent(spdLabel);

        content.addComponent(new EmptySpace(TerminalSize.ONE));
        content.addComponent(new Label("---------------"));
        content.addComponent(new EmptySpace(TerminalSize.ONE));

        goldLabel = new Label(" Gold:  10");
        goldLabel.setForegroundColor(TextColor.ANSI.YELLOW);
        content.addComponent(goldLabel);

        lvlLabel = new Label(" Level: 1");
        lvlLabel.setForegroundColor(TextColor.ANSI.GREEN);
        content.addComponent(lvlLabel);

        xpLabel = new Label(" XP:    0 / 50");
        xpLabel.setForegroundColor(TextColor.ANSI.GREEN);
        content.addComponent(xpLabel);

        panel = new Panel();
        panel.setLayoutManager(new BorderLayout());
        panel.setPreferredSize(new TerminalSize(22, 1));
        panel.addComponent(
                content.withBorder(Borders.doubleLine(" Stats ")),
                BorderLayout.Location.CENTER
        );
    }

    public Panel getPanel() {
        return panel;
    }

    public void setName(String name) {
        nameLabel.setText("  " + name + "  ");
    }

    public void setHp(int current, int max) {
        hpLabel.setText(String.format(" HP:   %d / %d", current, max));
    }

    public void setMp(int current, int max) {
        mpLabel.setText(String.format(" MP:   %d / %d", current, max));
    }

    public void setAtk(int value) {
        atkLabel.setText(String.format(" ATK:  %d", value));
    }

    public void setDef(int value) {
        defLabel.setText(String.format(" DEF:  %d", value));
    }

    public void setSpd(int value) {
        spdLabel.setText(String.format(" SPD:  %d", value));
    }

    public void setGold(int value) {
        goldLabel.setText(String.format(" Gold:  %d", value));
    }

    public void setLevel(int value) {
        lvlLabel.setText(String.format(" Level: %d", value));
    }

    public void setXp(int current, int max) {
        xpLabel.setText(String.format(" XP:    %d / %d", current, max));
    }
}
