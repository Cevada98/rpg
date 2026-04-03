package be.cevada.state;

import be.cevada.models.Place;
import be.cevada.panels.WorldPanel;

import java.util.ArrayList;
import java.util.List;

public class PlacesState implements GameState {

    private final GameStateManager manager;
    private final List<Place> places;

    public PlacesState(GameStateManager manager) {
        this.manager = manager;
        this.places = manager.getPlaceManager().getDiscoveredPlaces();
    }

    @Override
    public void enter() {
        WorldPanel world = manager.getWorldPanel();
        world.setLocation("Known Places");
        world.clearLog();

        if (places.isEmpty()) {
            world.setDescription("You haven't discovered any places yet.\nKeep exploring!");
            manager.setActions("Back");
            return;
        }

        StringBuilder sb = new StringBuilder("Choose a destination:\n");
        for (Place place : places) {
            sb.append("\n  ").append(place.getName());
        }
        world.setDescription(sb.toString());

        List<String> actions = new ArrayList<>();
        for (Place place : places) {
            actions.add(place.getName());
        }
        actions.add("Back");
        manager.setActions(actions.toArray(new String[0]));
    }

    @Override
    public void exit() {
    }

    @Override
    public void handleAction(String actionLabel) {
        if ("Back".equals(actionLabel)) {
            manager.transitionTo(new ExploringState(manager));
            return;
        }
        for (Place place : places) {
            if (place.getName().equals(actionLabel)) {
                switch (place.getId()) {
                    case "village" -> manager.transitionTo(new VillageState(manager));
                    case "farm" -> manager.transitionTo(new FarmState(manager));
                    default -> manager.transitionTo(new ExploringState(manager));
                }
                return;
            }
        }
    }
}



