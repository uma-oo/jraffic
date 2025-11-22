package jraffic;

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

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // create the lines based on the width and the height of the stage
        stage.setWidth(800);
        stage.setHeight(800);
        Pane pane = new Pane();

        pane.getChildren().addAll(setupRoutes(stage));

        scene = new Scene(pane, stage.getHeight(), stage.getWidth());

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    System.exit(0);
                }
                System.out.println(event.getCode());
            }
        });


        
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static List<Line> setupRoutes(Stage stage) {
        // we need to know the width and the height of the stage thing
        List<Line> lines = new ArrayList<>();
        Line horizontal = new Line(0, stage.getHeight() / 2, stage.getWidth(), stage.getHeight() / 2);
        Line horizontalLeft = new Line(0, stage.getHeight() / 2 - 50, stage.getWidth(), stage.getHeight() / 2 - 50);
        Line horizontaRight = new Line(0, stage.getHeight() / 2 + 50, stage.getWidth(), stage.getHeight() / 2 + 50);
        Line vertical = new Line(stage.getWidth() / 2, 0, stage.getWidth() / 2, stage.getHeight());
        Line verticalLeft = new Line(stage.getWidth() / 2 - 50, 0, stage.getWidth() / 2 - 50, stage.getHeight());
        Line verticalRight = new Line(stage.getWidth() / 2 + 50, 0, stage.getWidth() / 2 + 50, stage.getHeight());

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