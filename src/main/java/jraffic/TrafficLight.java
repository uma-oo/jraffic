package jraffic;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

public class TrafficLight {
    public Circle light;
    public double x, y;

    // Constructeur
    public TrafficLight(double x, double y, boolean green) {
        this.x = x;
        this.y = y;
        light = new Circle(x, y, 25); // rayon = 10
        light.setFill(green ? Color.GREEN : Color.RED);
    }

    // Changer la couleur du feu
    public void setGreen(boolean green) {
        light.setFill(green ? Color.GREEN : Color.RED);
    }

    // Créer les feux et les ajouter au Pane
    public static List<TrafficLight> setupTrafficLights(Pane pane) {
        List<TrafficLight> lights = new ArrayList<>();

        // Feux pour chaque direction
 TrafficLight topLight = new TrafficLight(479, 322, false);    // haut
 TrafficLight downLight = new TrafficLight(479, 478, false);   // bas
 TrafficLight leftLight = new TrafficLight(320, 360, false);   // gauche
// TrafficLight rightLight = new TrafficLight(300, 360, false);  // droite
        // Ajouter les cercles au Pane
        pane.getChildren().addAll(topLight.light,downLight.light,leftLight.light);

        // Ajouter à la liste pour pouvoir gérer facilement
        lights.add(topLight);
        // lights.add(downLight);
        // lights.add(leftLight);
        // lights.add(rightLight);

        return lights;
    }
}
