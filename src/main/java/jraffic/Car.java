package jraffic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
    private final double heightScene;
    private final double widthScene;
    private static final double WIDTH = 48;
    private static final double GAP = 60;
    private Rectangle rectangle;
    private boolean hasToStop = false;
    private boolean hadCrossed;

    private static List<Car> cars = new ArrayList<>();

    private Point position;

    public Car(KeyCode key, double height, double width, Pane pane) {
        heightScene = height;
        widthScene = width;
        this.hadCrossed = false;
        setLane(key);
        this.color = ColorV.randomColor();
        this.direction = (Direction.getDirection(getColor()));
        setPosition(this.lane);
        if (!isSafe(lane, getPosition())) {
            return;
        }
        rectangle = new Rectangle(WIDTH, WIDTH);
        rectangle.setTranslateX(this.getPosition().getX());
        rectangle.setTranslateY(this.getPosition().getY());
        rectangle.setFill(ColorV.getColor(this.color));
        cars.add(this);

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

    public boolean getHadCrossed() {
        return this.hadCrossed;
    }

    public void setHadCrossed() {
        this.hadCrossed = true;
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
            case UP -> {
                setLane(Lane.SOUTH);
            }
            case DOWN -> {
                setLane(Lane.NORTH);
            }
            case LEFT -> {
                setLane(Lane.WEST);
            }
            case RIGHT -> {
                setLane(Lane.EAST);
            }
            case R -> {
                setLane(Lane.randomLane());
            }
        }
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setPosition(Lane lane) {
        switch (lane) {
            case WEST -> {
                setPosition(new Point(widthScene, heightScene / 2 - 50));
            }
            case NORTH -> {
                setPosition(new Point(widthScene / 2 - 50, 0));
            }
            case EAST -> {
                setPosition(new Point(0, heightScene / 2));
            }
            case SOUTH -> {
                setPosition(new Point(widthScene / 2, heightScene - 50));
            }
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
        // keep original safety check
        if (!hadCrossed && hasToStop && isNotSafe()) {
            return;
        }

        Pair<Double, Double> delta = Lane.getPixelsToAdd(lane);
        Point current = this.getPosition();
        double newX = current.getX() + delta.getFirst();
        double newY = current.getY() + delta.getSecond();
        this.setPosition(new Point(newX, newY));

        // precomputed intersection anchors
        final double cx = widthScene / 2.0;
        final double cy = heightScene / 2.0;

        // read current pos once
        Point pos = this.position;

        switch (this.direction) {
            case LEFT -> {
                // SOUTH -> WEST when at (cx, cy - 50)
                if (this.lane == Lane.SOUTH && near(pos.getX(), cx) && near(pos.getY(), cy - 50)) {
                    turnTo(Lane.WEST);
                }
                // NORTH -> EAST when at (cx - 50, cy)
                else if (this.lane == Lane.NORTH && near(pos.getX(), cx - 50) && near(pos.getY(), cy)) {
                    turnTo(Lane.EAST);
                }
                // EAST -> SOUTH when at (cx, cy)
                else if (this.lane == Lane.EAST && near(pos.getX(), cx) && near(pos.getY(), cy)) {
                    turnTo(Lane.SOUTH);
                }
                // WEST -> NORTH when at (cx - 50, cy - 50)
                else if (this.lane == Lane.WEST && near(pos.getX(), cx - 50) && near(pos.getY(), cy - 50)) {
                    turnTo(Lane.NORTH);
                }
            }

            case RIGHT -> {
                // SOUTH -> EAST at (cx, cy)
                if (this.lane == Lane.SOUTH && near(pos.getX(), cx) && near(pos.getY(), cy)) {
                    turnTo(Lane.EAST);
                }
                // NORTH -> WEST at (cx - 50, cy - 50)
                else if (this.lane == Lane.NORTH && near(pos.getX(), cx - 50) && near(pos.getY(), cy - 50)) {
                    turnTo(Lane.WEST);
                }
                // EAST -> NORTH at (cx - 50, cy)
                else if (this.lane == Lane.EAST && near(pos.getX(), cx - 50) && near(pos.getY(), cy)) {
                    turnTo(Lane.NORTH);
                }
                // WEST -> SOUTH at (cx, cy - 50)
                else if (this.lane == Lane.WEST && near(pos.getX(), cx) && near(pos.getY(), cy - 50)) {
                    turnTo(Lane.SOUTH);
                }
            }

            case STRAIGHT -> {
                // mark crossed when reaching the straight crossing points
                if ((this.lane == Lane.SOUTH && near(pos.getX(), cx) && near(pos.getY(), cy - 50))
                        || (this.lane == Lane.NORTH && near(pos.getX(), cx - 50) && near(pos.getY(), cy))
                        || (this.lane == Lane.EAST && near(pos.getX(), cx) && near(pos.getY(), cy))
                        || (this.lane == Lane.WEST && near(pos.getX(), cx - 50) && near(pos.getY(), cy - 50))) {
                    this.setHadCrossed();
                }
            }
        }
    }

    private boolean near(double a, double b) {
        return Math.abs(a - b) <= 0.9;
    }

    // helper extracted to reduce duplication
    private void turnTo(Lane newLane) {
        this.lane = newLane;
        this.direction = Direction.STRAIGHT;
        this.setHadCrossed();
    }

    private boolean isOutOfBounds() {
        double x = position.getX();
        double y = position.getY();
        return x < -50 || x > widthScene + 50 || y < -50 || y > heightScene + 50;
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
            case SOUTH -> {
                // System.out.println("inside the south");
                for (Car c : CARS_SOUTH) {
                    if (Math.abs(c.getPosition().getY() - this.getPosition().getY()) < GAP) {
                        return true;
                    }
                }
                return (this.getPosition().getY() == heightScene / 2 + 50);
            }
            case NORTH -> {
                // System.out.println("inside the north");
                for (Car c : CARS_NORTH) {
                    if (Math.abs(c.getPosition().getY() - this.getPosition().getY()) < GAP) {
                        return true;
                    }
                }
                return (this.getPosition().getY() == heightScene / 2 - 100);
            }
            case EAST -> {
                for (Car c : CARS_EAST) {
                    if (Math.abs(c.getPosition().getX() - this.getPosition().getX()) < GAP) {
                        return true;
                    }
                }
                return (this.getPosition().getX() == widthScene / 2 - 100);
            }
            case WEST -> {
                for (Car c : CARS_WEST) {
                    if (Math.abs(c.getPosition().getX() - this.getPosition().getX()) < GAP) {
                        return true;
                    }
                }
                return (this.getPosition().getX() == widthScene / 2 + 50);
            }
        }
        return false;
    }

}
