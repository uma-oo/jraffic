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
}