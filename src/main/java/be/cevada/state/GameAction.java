package be.cevada.state;

public enum GameAction {
    EXPLORE("Explore"),
    REST("Rest"),
    INVENTORY("Inventory"),
    PLACES("Places"),
    QUIT("Quit"),

    ATTACK("Attack"),
    SPECIAL("Special"),
    DEFEND("Defend"),
    RUN("Run"),
    RETURN_TO_MENU("Return to Menu"),

    BACK("Back"),
    LEAVE("Leave"),
    CONTINUE("Continue"),

    PLACE_VILLAGE("Village of Briarwood"),
    PLACE_FARM("Old Miller's Farm"),

    BLACKSMITH("Blacksmith"),
    CHURCH("Church"),
    INN("Inn"),
    UPGRADE("Upgrade"),
    HEAL("Heal"),

    TALK_TO_FARMER("Talk to Farmer"),
    ACCEPT_QUEST("Accept Quest"),
    CLAIM_REWARD("Claim Reward");

    private final String label;

    GameAction(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}

