package jraffic;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class LightTraffic {
    private Lane lane;
    private Color color;
    private Rectangle rectangle;

    private static final List<LightTraffic> lights = new ArrayList<>(Arrays.asList(
            new LightTraffic(Lane.SOUTH, 800, 800),
            new LightTraffic(Lane.NORTH, 800, 800),
            new LightTraffic(Lane.EAST, 800, 800),
            new LightTraffic(Lane.WEST, 800, 800)));

    public Color getColor() {
        return this.color;
    }

    public Lane getLane() {
        return lane;
    }

    public static List<LightTraffic> geLightTraffics() {
        return lights;
    }

    public LightTraffic(Lane lane, double width, double height) {
        rectangle = new Rectangle(50, 50);
        this.color = Color.RED;
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
        
        for (Car car : Car.getCars()) {
            if (car.getLane() == this.getLane() && this.color == Color.RED) {
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
