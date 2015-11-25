package javaFXServer2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by user on 20/11/2015.
 */
public class ThermometerApplication extends Application {
    private final TemperatureSensor sensor = new TemperatureSensor();
    private final Label temperatureLabel = new Label(String.valueOf(sensor.getCurrentReading()));

    public static void main (String [] args){
        launch(args);
    }


    public ThermometerApplication() {
        super();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Thermometer");
        HBox root = new HBox();

        root.getChildren().add(new Label("current temperature: "));
        root.getChildren().add(temperatureLabel);

        primaryStage.setScene(new Scene(root, 300, 50));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                sensor.shutdown();
            }
        });
        primaryStage.show();

        sensor.addListener(new TemperatureSensorListener() {
            @Override
            public void onReadingChange() {
                updateTemperature();
            }
        });
    }//start

    public void updateTemperature(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                temperatureLabel.setText(String.valueOf(sensor.getCurrentReading()));
            }
        });
    }//updateTemperature
}
