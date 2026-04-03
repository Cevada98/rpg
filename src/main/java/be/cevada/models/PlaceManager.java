package be.cevada.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlaceManager {

    private static final Random RNG = new Random();

    private final Map<String, Place> discoveredPlaces = new LinkedHashMap<>();
    private final List<Place> undiscoveredPlaces = new ArrayList<>();

    public PlaceManager() {
        undiscoveredPlaces.add(Place.village());
        undiscoveredPlaces.add(Place.farm());
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

