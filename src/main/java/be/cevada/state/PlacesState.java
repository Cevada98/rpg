package be.cevada.state;

import be.cevada.models.Place;
import be.cevada.panels.WorldView;

import java.util.ArrayList;
import java.util.List;

public class PlacesState implements GameState {

    private final PlacesDeps manager;
    private final List<Place> places;

    public PlacesState(PlacesDeps manager) {
        this.manager = manager;
        this.places = manager.getPlaceManager().getDiscoveredPlaces();
    }

    @Override
    public void enter() {
        WorldView world = manager.getWorldPanel();
        world.setLocation("Known Places");
        world.clearLog();

        if (places.isEmpty()) {
            world.setDescription("You haven't discovered any places yet.\nKeep exploring!");
            manager.setActions(GameAction.BACK);
            return;
        }

        StringBuilder sb = new StringBuilder("Choose a destination:\n");
        for (Place place : places) {
            sb.append("\n  ").append(place.getName());
        }
        world.setDescription(sb.toString());

        List<GameAction> actions = new ArrayList<>();
        for (Place place : places) {
            actions.add(placeAction(place));
        }
        actions.add(GameAction.BACK);
        manager.setActions(actions.toArray(new GameAction[0]));
    }

    @Override
    public void exit() {
    }

    @Override
    public void handleAction(GameAction action) {
        if (action == GameAction.BACK) {
            manager.transitionTo(manager.newExploringState());
            return;
        }
        switch (action) {
            case PLACE_VILLAGE -> manager.transitionTo(manager.newVillageState());
            case PLACE_FARM -> manager.transitionTo(manager.newFarmState());
            default -> {
            }
        }
    }

    private GameAction placeAction(Place place) {
        return switch (place.getId()) {
            case "village" -> GameAction.PLACE_VILLAGE;
            case "farm" -> GameAction.PLACE_FARM;
            default -> throw new IllegalArgumentException("Unsupported place id: " + place.getId());
        };
    }
}



