package be.cevada.data;

import java.util.Random;

public final class GameRandom {

    private static final Random INSTANCE = new Random();

    private GameRandom() {}

    public static Random get() {
        return INSTANCE;
    }
}

