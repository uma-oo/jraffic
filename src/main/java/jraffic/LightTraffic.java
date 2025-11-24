package jraffic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LightTraffic {
    private Lane lane;
    private Color color;
    private Rectangle rectangle;
    private final double width = 1000;
    private final double height = 1000;

    private static final List<LightTraffic> lights = new ArrayList<>(Arrays.asList(
            new LightTraffic(Lane.SOUTH),
            new LightTraffic(Lane.NORTH),
            new LightTraffic(Lane.EAST),
            new LightTraffic(Lane.WEST)));

    public final Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Lane getLane() {
        return lane;
    }

    public static List<LightTraffic> geLightTraffics() {
        return lights;
    }

    public LightTraffic(Lane lane) {
        rectangle = new Rectangle(50, 50);
        this.color = Color.GREEN;
        this.lane = lane;
        switch (lane) {
            case SOUTH:
                rectangle.setTranslateX(width / 2 + 50);
                rectangle.setTranslateY(height / 2 + 50);
                break;
            case NORTH:
                rectangle.setTranslateX(width / 2 - (2 * 50));
                rectangle.setTranslateY(height / 2 - (2 * 50));
                break;
            case EAST:
                rectangle.setTranslateX(width / 2 - (2 * 50));
                rectangle.setTranslateY(height / 2 + (50));

                break;
            case WEST:
                rectangle.setTranslateX(width / 2 + 50);
                rectangle.setTranslateY(height / 2 - (2 * 50));
                break;
            default:
                break;
        }
    }

    // update the lights based on what
    public void update() {
        if (TrafficLightController.isInGrace()) {
            return;
        }
        for (Car car : Car.getCars()) {
            if (car.getLane() == this.getLane() && this.color == Color.RED && !car.getHadCrossed()) {
                car.setHasToStop(true);
            } else if (car.getLane() == this.getLane() && this.color == Color.GREEN) {
                car.setHasToStop(false);
            }
        }
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    public static void UpdateLights(Pane pane) {
        for (LightTraffic light : lights) {
            light.update();
            light.draw(pane);
        }
    }

    public void draw(Pane pane) {
        rectangle.setStroke(this.color);
        rectangle.setFill(Color.TRANSPARENT);
    }

}
