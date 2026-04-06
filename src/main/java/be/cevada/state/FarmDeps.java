package be.cevada.state;

public interface FarmDeps extends
        PlayerAccess,
        WorldAccess,
        ActionSetter,
        StatsSync,
        StateNavigator,
        StateFactory {
}

