package jraffic;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.*;

public class TrafficLight {

    public enum Directionl { TOP, DOWN, LEFT, RIGHT }

    public Circle light;
    public double x, y;
    public Directionl direction;

    private static Directionl currentGreen = Directionl.TOP;

    // Durée en nanosecondes (3 secondes)
    private static final long SWITCH_INTERVAL = 3_000_000_000L;
    private static long lastSwitchTime = 0;

    public TrafficLight(double x, double y, Directionl dir, boolean green) {
        this.x = x;
        this.y = y;
        this.direction = dir;
        light = new Circle(x, y, 25);
        light.setFill(green ? Color.GREEN : Color.RED);
    }

    public void setGreen(boolean green) {
        light.setFill(green ? Color.GREEN : Color.RED);
    }

    // Création des 4 feux autour du carrefour
    public static List<TrafficLight> setupTrafficLights(Pane pane) {
        List<TrafficLight> lights = new ArrayList<>();

        TrafficLight topLight = new TrafficLight(479, 322, Directionl.TOP, true);   // vert au début
        TrafficLight downLight = new TrafficLight(479, 478, Directionl.DOWN, false);
        TrafficLight leftLight = new TrafficLight(320, 322, Directionl.LEFT, false);
        TrafficLight rightLight = new TrafficLight(320, 478, Directionl.RIGHT, false);

        pane.getChildren().addAll(topLight.light, downLight.light, leftLight.light, rightLight.light);

        lights.add(topLight);
        lights.add(downLight);
        lights.add(leftLight);
        lights.add(rightLight);

        currentGreen = Directionl.TOP;

        return lights;
    }

    // Comptage correct des voitures selon les lanes
    private static int countCars(Directionl dir, List<Car> cars) {
        int count = 0;
        for (Car c : cars) {
            switch (dir) {
                case TOP:
                    if (c.getLane() == Lane.NORTH) count++;
                    break;
                case DOWN:
                    if (c.getLane() == Lane.SOUTH) count++;
                    break;
                case LEFT:
                    if (c.getLane() == Lane.WEST) count++;
                    break;
                case RIGHT:
                    if (c.getLane() == Lane.EAST) count++;
                    break;
            }
        }
        return count;
    }

    // Mise à jour des feux à chaque frame
    public static void updateTrafficLights(List<TrafficLight> lights, List<Car> cars) {
        long now = System.nanoTime();

        // Changement automatique toutes les 3 secondes
        if (now - lastSwitchTime > SWITCH_INTERVAL) {
            cycleLights(lights);
            lastSwitchTime = now;
        }
    }

    // Cycle : TOP → DOWN → LEFT → RIGHT → TOP
    private static void cycleLights(List<TrafficLight> lights) {
        Directionl next;

        switch (currentGreen) {
            case TOP -> next = Directionl.DOWN;
            case DOWN -> next = Directionl.LEFT;
            case LEFT -> next = Directionl.RIGHT;
            default -> next = Directionl.TOP;
        }

        switchGreen(lights, next);
    }

    // Change le feu vert
    private static void switchGreen(List<TrafficLight> lights, Directionl dir) {
        currentGreen = dir;

        for (TrafficLight light : lights) {
            light.setGreen(light.direction == dir);
        }
    }


    public static Directionl getCurrentGreen() {
    return currentGreen;
  }
}
