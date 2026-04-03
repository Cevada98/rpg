package be.cevada.state.services;

import java.util.Random;

public class ExplorationService {

    private final Random rng;

    public ExplorationService(Random rng) {
        this.rng = rng;
    }

    public boolean shouldSpawnQuestRat() {
        return rng.nextInt(100) < 40;
    }

    public String randomExploreEvent(String[] events) {
        return events[rng.nextInt(events.length)];
    }

    public int rollGoldFound() {
        if (rng.nextInt(100) < 25) {
            return rng.nextInt(5) + 1;
        }
        return 0;
    }
}

