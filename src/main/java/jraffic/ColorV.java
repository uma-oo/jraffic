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
        Color newColor = Color.BLUE;
        switch (color) {
            case BLUE:
                break;
            case MAGENTA:
                newColor = Color.MAGENTA;
                break;
            case YELLOW:
                newColor = Color.YELLOW;
                break;
            default:
                break;
        }
        return newColor;

    }
}