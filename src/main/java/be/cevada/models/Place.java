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

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDiscoveryMessage() { return discoveryMessage; }
}
