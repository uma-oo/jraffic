package jraffic;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.*;

/**
 * JavaFX App
 */
public class App extends Application {

    static final double WIDTH = 800;
    static final double HEIGHT = 800;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // create the lines based on the width and the height of the stage
        // stage.setWidth(800);

        Pane pane = new Pane();

        pane.getChildren().addAll(setupRoutes());
        scene = new Scene(pane, WIDTH, HEIGHT);
        List<TrafficLight> trafficLightsList = TrafficLight.setupTrafficLights(pane);


        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override

            public void handle(KeyEvent event) {

                switch (event.getCode()) {
                    case UP:
                    case DOWN:
                    case RIGHT:
                    case LEFT:
                    case R:
                        Car car = new Car(event.getCode(), HEIGHT, WIDTH, pane);
                        car.draw();
                        break;
                    case ESCAPE:

                        System.exit(0);

                    default:
                        break;
                }

                System.out.println(event.getCode());
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                Car.updateCars(pane);
            }
        }.start();
        

        stage.setScene(scene);
        // stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static List<Line> setupRoutes() {
        // we need to know the width and the height of the stage thing
        List<Line> lines = new ArrayList<>();
        Line horizontal = new Line(0, HEIGHT / 2, WIDTH, HEIGHT / 2);
        Line horizontalLeft = new Line(0, HEIGHT / 2 - 50, WIDTH, HEIGHT / 2 - 50);
        Line horizontaRight = new Line(0, HEIGHT / 2 + 50, WIDTH, HEIGHT / 2 + 50);
        Line vertical = new Line(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        Line verticalLeft = new Line(WIDTH / 2 - 50, 0, WIDTH / 2 - 50, HEIGHT);
        Line verticalRight = new Line(WIDTH / 2 + 50, 0, WIDTH / 2 + 50, HEIGHT);

        lines.addAll(Arrays.asList(horizontal, horizontaRight, horizontalLeft, vertical, verticalLeft, verticalRight));
        return lines;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}