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
        Pair<Double, Double> pixels = new Pair<Double, Double>(0.0, 0.0);
        switch (lane) {
            case NORTH:
                pixels = new Pair<Double, Double>(0.0, 1.0);
                break;
            case SOUTH:
                pixels = new Pair<Double, Double>(0.0, -1.0);
                break;
            case WEST:
                pixels = new Pair<Double, Double>(-1.0, 0.0);
                break;
            case EAST:
                pixels = new Pair<Double, Double>(1.0, 0.0);
                break;
            default:
                break;
        }

        return pixels;
    }
}