package jraffic;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    private static final double GAP = 60;
    private Rectangle rectangle;
    private boolean hasToStop = false;

    private static List<Car> cars = new ArrayList<>();

    private Point position;

    public Car(KeyCode key, double height, double width, Pane pane) {
        heightScene = height;
        widthScene = width;
        setLane(key);
        setColor(ColorV.randomColor());
        setDirection(Direction.getDirection(getColor()));
        setPosition(getLane());
        if (!isSafe(lane, getPosition())) {
            return;
        }

        rectangle = new Rectangle(WIDTH, WIDTH);
        rectangle.setTranslateX(this.getPosition().getX());
        rectangle.setTranslateY(this.getPosition().getY());
        rectangle.setFill(ColorV.getColor(this.color));
        cars.add(this);
        this.draw();
        pane.getChildren().add(rectangle);
    }

    public static List<Car> getCars() {
        return cars;
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

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    // setters
    public void setLane(Lane lane) {
        this.lane = lane;

    }

    public void setColor(ColorV color) {
        this.color = color;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setHasToStop(boolean hasToStop) {
        this.hasToStop = hasToStop;
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
                setPosition(new Point(widthScene, heightScene / 2 - 50));
                break;
            case NORTH:
                setPosition(new Point(widthScene / 2 - 50, 0));
                break;
            case EAST:
                setPosition(new Point(0, heightScene / 2));
                break;
            case SOUTH:
                setPosition(new Point(widthScene / 2, heightScene - 50));
                break;

        }
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void draw() {
        if (rectangle == null) {
            return;
        }
        rectangle.setTranslateX(this.getPosition().getX());
        rectangle.setTranslateY(this.getPosition().getY());
    }

    public void update() {
        if (hasToStop && isNotSafe()) {
            return;
        }

        Pair<Double, Double> delta = Lane.getPixelsToAdd(lane);
        Point current = this.getPosition();
        double newX = current.getX() + delta.getFirst();
        double newY = current.getY() + delta.getSecond();
        this.setPosition(new Point(newX, newY));

        switch (this.direction) {
            case LEFT:
                if (this.position.getX() == widthScene / 2
                        && this.position.getY() == heightScene / 2 - 50 && this.getLane() == Lane.SOUTH) {
                    this.lane = Lane.WEST;
                    this.direction = Direction.STRAIGHT;
                } else if (this.position.getX() == widthScene / 2 - 50
                        && this.position.getY() == heightScene / 2 && this.getLane() == Lane.NORTH) {
                    this.lane = Lane.EAST;
                    this.direction = Direction.STRAIGHT;
                } else if (this.position.getX() == widthScene / 2
                        && this.position.getY() == heightScene / 2 && this.getLane() == Lane.EAST) {
                    this.lane = Lane.SOUTH;
                    this.direction = Direction.STRAIGHT;
                } else if (this.position.getX() == widthScene / 2 - 50
                        && this.position.getY() == heightScene / 2 - 50 && this.getLane() == Lane.WEST) {
                    this.lane = Lane.NORTH;
                    this.direction = Direction.STRAIGHT;
                }
                break;
            case RIGHT:
                if (this.position.getX() == widthScene / 2
                        && this.position.getY() == heightScene / 2 && this.getLane() == Lane.SOUTH) {
                    this.lane = Lane.EAST;
                    this.direction = Direction.STRAIGHT;
                } else if (this.position.getX() == widthScene / 2 - 50
                        && this.position.getY() == heightScene / 2 - 50 && this.getLane() == Lane.NORTH) {
                    this.lane = Lane.WEST;
                    this.direction = Direction.STRAIGHT;
                } else if (this.position.getX() == widthScene / 2 - 50
                        && this.position.getY() == heightScene / 2 && this.getLane() == Lane.EAST) {
                    this.lane = Lane.NORTH;
                    this.direction = Direction.STRAIGHT;
                } else if (this.position.getX() == widthScene / 2
                        && this.position.getY() == heightScene / 2 - 50 && this.getLane() == Lane.WEST) {
                    this.lane = Lane.SOUTH;
                    this.direction = Direction.STRAIGHT;
                }
                break;

            default:
                break;
        }

    }

    private boolean isOutOfBounds() {
        double x = position.getX();
        double y = position.getY();
        return x < -50 || x > widthScene + 50 || y < -50 || y > heightScene + 50;
    }

    private void checkCollision() {

    }

    public static void updateCars(Pane pane) {
        Iterator<Car> iteratorCars = cars.iterator();

        while (iteratorCars.hasNext()) {
            Car car = iteratorCars.next();
            car.update();
            car.draw();
            if (car.isOutOfBounds()) {
                // the iterator modifies the cars instead :)
                iteratorCars.remove();
                int index = pane.getChildren().indexOf(car.getRectangle());
                if (index != -1) {
                    pane.getChildren().remove(pane.getChildren().indexOf(car.getRectangle()));
                }
            }
        }

    }

    public static boolean isSafe(Lane lane, Point position) {
        if (lane == null || position == null) {
            return false;
        }

        for (Car car : cars) {
            if (lane == Lane.SOUTH && Math.abs(position.getY() - car.getPosition().getY()) <= GAP) {
                return false;
            } else if (lane == Lane.NORTH && Math.abs(car.getPosition().getY() - position.getY()) <= GAP) {
                return false;
            } else if (lane == Lane.WEST && Math.abs(position.getX() - car.getPosition().getX()) <= GAP) {
                return false;
            } else if (lane == Lane.EAST && Math.abs(position.getX() - car.getPosition().getX()) <= GAP) {
                return false;
            }
        }

        return true;

    }

    /// checks collision of the car with the line of the lane
    public boolean isNotSafe() {

        // filter cars based on which lane the cars belongs to !

        List<Car> CARS_SOUTH = cars.stream().filter(car -> car.getLane() == Lane.SOUTH && car != this)
                .collect(Collectors.toList());
        List<Car> CARS_NORTH = cars.stream().filter(car -> car.getLane() == Lane.NORTH && car != this)
                .collect(Collectors.toList());
        List<Car> CARS_EAST = cars.stream().filter(car -> car.getLane() == Lane.EAST && car != this)
                .collect(Collectors.toList());
        List<Car> CARS_WEST = cars.stream().filter(car -> car.getLane() == Lane.WEST && car != this)
                .collect(Collectors.toList());
        // compare if one car has to stop if it reaches the line or if the distance of
        // them is less than 10px

        switch (getLane()) {
            case SOUTH:
                System.out.println("inside the south");
                for (Car c : CARS_SOUTH) {
                    if (Math.abs(c.getPosition().getY() - this.getPosition().getY()) < GAP) {
                        System.out.println("inside the if hereee");
                        return true;
                    }
                }
                return (this.getPosition().getY() == heightScene / 2 + 50);

            case NORTH:
                for (Car c : CARS_NORTH) {
                    if (Math.abs(c.getPosition().getY() - this.getPosition().getY()) < GAP) {
                        return true;
                    }
                }
                return (this.getPosition().getY() == heightScene / 2 - 100);
            case EAST:
                for (Car c : CARS_EAST) {
                    if (Math.abs(c.getPosition().getX() - this.getPosition().getX()) < GAP) {
                        return true;
                    }
                }
                return (this.getPosition().getX() == widthScene / 2 - 100);
            case WEST:
                for (Car c : CARS_WEST) {
                    if (Math.abs(c.getPosition().getX() - this.getPosition().getX()) < GAP) {
                        return true;
                    }
                }
                return (this.getPosition().getX() == widthScene / 2 + 50);
            default:
                break;
        }

        return false;
    }

}
