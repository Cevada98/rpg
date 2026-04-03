package be.cevada.models;

public class Quest {

    private final String id;
    private final String name;
    private final String description;
    private final int targetCount;
    private int currentCount;
    private boolean completed;
    private boolean rewarded;

    public Quest(String id, String name, String description, int targetCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.targetCount = targetCount;
        this.currentCount = 0;
        this.completed = false;
        this.rewarded = false;
    }

    public void incrementProgress() {
        if (!completed) {
            currentCount++;
            if (currentCount >= targetCount) {
                completed = true;
            }
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getTargetCount() { return targetCount; }
    public int getCurrentCount() { return currentCount; }
    public boolean isCompleted() { return completed; }
    public boolean isRewarded() { return rewarded; }
    public void setRewarded(boolean rewarded) { this.rewarded = rewarded; }

    public String getProgressText() {
        return currentCount + " / " + targetCount;
    }
}

