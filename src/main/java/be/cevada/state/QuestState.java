package be.cevada.state;

import be.cevada.models.Quest;
import be.cevada.panels.WorldView;
import com.googlecode.lanterna.TextColor;

import java.util.List;

public class QuestState implements GameState {

    private final QuestDeps manager;

    public QuestState(QuestDeps manager) {
        this.manager = manager;
    }

    @Override
    public void enter() {
        WorldView world = manager.getWorldPanel();
        world.setLocation("Quest Journal");
        world.clearLog();

        List<Quest> quests = manager.getPlayer().getQuests();

        if (quests.isEmpty()) {
            world.setDescription("Your quest journal is empty.\nSpeak to NPCs to find quests.");
        } else {
            StringBuilder sb = new StringBuilder("Quest Journal\n");
            sb.append("─────────────────────────────\n");

            for (Quest quest : quests) {
                if (!quest.isCompleted()) {
                    sb.append("\n[IN PROGRESS] ").append(quest.getName());
                    sb.append("\n  ").append(quest.getDescription());
                    sb.append("\n  Progress: ").append(quest.getProgressText());
                } else if (!quest.isRewarded()) {
                    sb.append("\n[COMPLETE] ").append(quest.getName()).append(" — Claim your reward!");
                } else {
                    sb.append("\n[DONE] ").append(quest.getName());
                }
                sb.append("\n");
            }

            world.setDescription(sb.toString().trim());
        }

        for (Quest quest : quests) {
            if (!quest.isCompleted()) {
                world.addLogEntry("> " + quest.getName() + ": " + quest.getProgressText(),
                        TextColor.ANSI.CYAN_BRIGHT);
            } else if (!quest.isRewarded()) {
                world.addLogEntry("> " + quest.getName() + ": COMPLETE — collect reward!",
                        TextColor.ANSI.YELLOW_BRIGHT);
            } else {
                world.addLogEntry("> " + quest.getName() + ": Done.",
                        TextColor.ANSI.WHITE);
            }
        }

        manager.setActions(GameAction.BACK);
    }

    @Override
    public void exit() { }

    @Override
    public void handleAction(GameAction action) {
        if (action == GameAction.BACK) {
            manager.transitionTo(manager.newExploringState());
        }
    }
}

