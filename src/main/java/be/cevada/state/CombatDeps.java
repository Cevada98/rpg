package be.cevada.state;

public interface CombatDeps extends
        PlayerAccess,
        WorldAccess,
        ActionSetter,
        StatsSync,
        ActionPanelAccess,
        QuitAccess,
        StateNavigator,
        StateFactory {
}

