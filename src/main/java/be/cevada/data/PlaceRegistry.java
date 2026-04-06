package be.cevada.data;

import be.cevada.models.Place;

import java.util.List;

public final class PlaceRegistry {

    private PlaceRegistry() {}

    public static List<Place> all() {
        return List.of(village(), farm());
    }

    public static Place village() {
        return new Place(
                "village",
                "Village of Briarwood",
                "A small village with cobblestone streets.\nSmoke rises from the chimneys.",
                "You discover a path leading to a small village!"
        );
    }

    public static Place farm() {
        return new Place(
                "farm",
                "Old Miller's Farm",
                "A weathered farmstead surrounded by fields.\nA grizzled farmer watches you approach.",
                "You stumble upon a farmstead at the edge of the forest!"
        );
    }
}

