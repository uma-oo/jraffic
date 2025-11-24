package jraffic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;

public enum ColorV {
    BLUE,
    YELLOW,
    MAGENTA;

    private static final List<ColorV> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static ColorV randomColor() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static Color getColor(ColorV color) {
        return switch (color) {
            case BLUE -> Color.BLUE;
            case YELLOW -> Color.YELLOW;
            case MAGENTA -> Color.MAGENTA;
        };
    }
}