package jraffic;

// BLUE -> straight 
// MAGENTA -> left
// YELLOW -> right 

public enum Direction {
    STRAIGHT,
    LEFT,
    RIGHT;

    public static Direction getDirection(ColorV color) {
        Direction direction = Direction.STRAIGHT;
        switch (color) {
            case BLUE:
                break;
            case YELLOW:
                direction = Direction.RIGHT;
                break;
            case MAGENTA:
                direction = Direction.LEFT;
                break;
            default:
                break;
        }
        return direction;
    }

    

}