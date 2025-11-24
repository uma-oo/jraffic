package jraffic;

// BLUE -> straight 
// MAGENTA -> left
// YELLOW -> right 

public enum Direction {
    STRAIGHT,
    LEFT,
    RIGHT;

    public static Direction getDirection(ColorV color) {
        return switch (color) {
            case BLUE -> Direction.STRAIGHT;
            case YELLOW -> Direction.RIGHT;
            case MAGENTA -> Direction.LEFT;
        };
    }

}