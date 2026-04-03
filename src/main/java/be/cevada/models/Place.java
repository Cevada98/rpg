package be.cevada.models;

public class Place {

    private final String id;
    private final String name;
    private final String description;
    private final String discoveryMessage;

    public Place(String id, String name, String description, String discoveryMessage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.discoveryMessage = discoveryMessage;
    }

    public static Place village() {
        return new Place("village", "Village of Briarwood",
                "A small village with cobblestone streets.\nSmoke rises from the chimneys.",
                "You discover a path leading to a small village!");
    }

    public static Place farm() {
        return new Place("farm", "Old Miller's Farm",
                "A weathered farmstead surrounded by fields.\nA grizzled farmer watches you approach.",
                "You stumble upon a farmstead at the edge of the forest!");
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDiscoveryMessage() { return discoveryMessage; }
}

