package jraffic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    static final double WIDTH = 1000;
    static final double HEIGHT = 1000;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // create the lines based on the width and the height of the stage
        // stage.setWidth(800);

        // Bakcground
        Background background = new Background(
                new BackgroundImage(
                        new Image(getClass().getResource("/assets/background.png").toString()),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT));

        Pane pane = new Pane();
        // fps label
        Label label = new Label();
        label.setStyle("-fx-background-color: WHITE;");
        pane.getChildren().add(label);
        pane.setBackground(background);
        pane.getChildren().addAll(setupRoutes());
        scene = new Scene(pane, WIDTH, HEIGHT);

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

            }
        });

        AnimationTimer timer = new AnimationTimer() {
            private final int targetFps = 60;
            private final long frameInterval = 1_000_000_000L / targetFps;

            private long lastUpdate = 0;
            private long lastFpsTime = 0;
            private int frames = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    lastFpsTime = now;
                    return;
                }

                while (now - lastUpdate >= frameInterval) {
                    Car.updateCars(pane);

                    frames++;
                    lastUpdate += frameInterval;
                }

                if (now - lastFpsTime >= 1_000_000_000L) {
                    label.setText("FPS: " + frames + " ");
                    frames = 0;
                    lastFpsTime = now;
                }
            }
        };

        timer.start();
        stage.setScene(scene);
        stage.setResizable(false);
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