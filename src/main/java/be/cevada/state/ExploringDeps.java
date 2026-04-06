package be.cevada.state;

public interface ExploringDeps extends
        PlayerAccess,
        WorldAccess,
        ActionSetter,
        StatsSync,
        PlaceAccess,
        QuitAccess,
        StateNavigator,
        StateFactory {
}

