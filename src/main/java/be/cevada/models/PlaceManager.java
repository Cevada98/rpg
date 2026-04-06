package be.cevada.models;

import be.cevada.data.GameRandom;
import be.cevada.data.PlaceRegistry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlaceManager {

    private static final Random RNG = GameRandom.get();

    private final Map<String, Place> discoveredPlaces = new LinkedHashMap<>();
    private final List<Place> undiscoveredPlaces = new ArrayList<>();

    public PlaceManager() {
        undiscoveredPlaces.addAll(PlaceRegistry.all());
    }

    public Place tryDiscover() {
        if (undiscoveredPlaces.isEmpty()) {
            return null;
        }
        if (RNG.nextInt(100) < 20) {
            Place place = undiscoveredPlaces.remove(RNG.nextInt(undiscoveredPlaces.size()));
            discoveredPlaces.put(place.getId(), place);
            return place;
        }
        return null;
    }

    public List<Place> getDiscoveredPlaces() {
        return new ArrayList<>(discoveredPlaces.values());
    }

    public boolean hasDiscoveredPlaces() {
        return !discoveredPlaces.isEmpty();
    }

    public boolean isDiscovered(String placeId) {
        return discoveredPlaces.containsKey(placeId);
    }
}

