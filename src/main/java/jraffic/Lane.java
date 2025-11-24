package jraffic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Lane {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    private static final List<Lane> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Lane randomLane() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    // DirectionLane::South => (0, -1),
    // DirectionLane::West => (-1, 0),
    // DirectionLane::East => (1, 0),
    // DirectionLane::North => (0, 1),
    public static Pair<Double, Double> getPixelsToAdd(Lane lane) {
        return switch (lane) {
            case NORTH -> new Pair<>(0.0, 1.0);
            case SOUTH -> new Pair<>(0.0, -1.0);
            case WEST -> new Pair<>(-1.0, 0.0);
            case EAST -> new Pair<>(1.0, 0.0);
        };
    }
}