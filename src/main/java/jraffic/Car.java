package jraffic;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
// BLUE -> straight 
// MAGENTA -> left
// YELLOW -> right 

public class Car {
    
    private ColorV color;
    private Direction direction;
    private Lane lane;
    private double heightScene;
    private double widthScene;
    private static final double WIDTH = 48;
    private Rectangle rectangle;
    private static List<Car> cars = new ArrayList<>();

    private Point position;

    Sprites sprites = Sprites.load();

    public Car(KeyCode key, double height, double width, Pane pane) {
        heightScene = height;
        widthScene = width;
        setLane(key);
        setColor(ColorV.randomColor());
        setPosition(getLane());
        rectangle = new Rectangle(WIDTH, WIDTH);
        rectangle.setTranslateX(this.getPosition().getX());
        rectangle.setTranslateY(this.getPosition().getY());
        rectangle.setFill(ColorV.getColor(this.color));
        cars.add(this);
        this.draw();
        pane.getChildren().add(rectangle);
    }

    // constructor

    public Direction getDirection() {
        return this.direction;
    }

    public ColorV getColor() {
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

    public void setColor(ColorV color) {
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
                setPosition(new Point(widthScene - 50, heightScene / 2 - 50));
                break;
            case NORTH:
                setPosition(new Point(widthScene / 2 - 50, 0));
                break;
            case EAST:
                setPosition(new Point(0, heightScene / 2));
                break;
            case SOUTH:
                System.out.println(heightScene);
                setPosition(new Point(widthScene / 2, heightScene - 50));
                break;

        }
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void draw() {
        rectangle.setTranslateX(this.getPosition().getX());
        rectangle.setTranslateY(this.getPosition().getY());
    }

    public void update() {
        // if (position.getX() == 0 || position.getX() == widthScene || position.getX()
        // == -widthScene
        // || position.getY() == 0 || position.getY() == widthScene || position.getY()
        // == -widthScene) {
        // cars.remove(cars.indexOf(this));
        // }
        Pair<Double, Double> delta = Lane.getPixelsToAdd(lane);
        Point current = this.getPosition();
        double newX = current.getX() + delta.getFirst();
        double newY = current.getY() + delta.getSecond();
        this.setPosition(new Point(newX, newY));
        System.out.println("inside the update");
        System.out.println(rectangle.getX());

    }

    public static void updateCars(Pane pane) {
        for (Car car : cars) {
            car.update();
            car.draw();
        }
    }

}
