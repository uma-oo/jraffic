package jraffic;

import javafx.scene.input.KeyCode;

// BLUE -> straight 
// MAGENTA -> left
// YELLOW -> right 

public class Car {

    private Color color;
    private Direction direction;
    private Lane lane;
    private double heightScene;
    private double widthScene;
    private static final double WIDTH = 50;

    private Point position;

    public Car(KeyCode key, double height, double width) {
        heightScene = height;
        widthScene = width;
        setLane(key);
        setColor(Color.randomColor());
    }

    // constructor

    public Direction getDirection() {
        return this.direction;
    }

    public Color getColor() {
        return this.color;
    }

    public Lane getLane() {
        return this.lane;
    }

    public Point getPosition() {
        return this.position;
    }

    // setters
    public void setLane(Lane lane) {
        this.lane = lane;

    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setLane(KeyCode key) {
        switch (key) {
            case UP:
                setLane(Lane.SOUTH);
                break;
            case DOWN:
                setLane(Lane.NORTH);
                break;
            case LEFT:
                setLane(Lane.WEST);
                break;
            case RIGHT:
                setLane(Lane.EAST);
                break;
            case R:
                setLane(Lane.randomLane());
                break;

        }
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setPosition(Lane lane) {
        switch (lane) {
            case WEST:
                setPosition(new Point(0, 0));
                break;
            case NORTH:
                setPosition(new Point(0, 0));
                break;
            case EAST:
                setPosition(new Point(0, 0));
                break;
            case SOUTH:
                setPosition(new Point(0, 0));
                break;

        }
    }

}
