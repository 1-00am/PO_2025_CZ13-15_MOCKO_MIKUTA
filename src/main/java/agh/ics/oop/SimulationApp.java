package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.presenter.SimulationLauncherPresenter;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getClassLoader().getResource("simulation_launcher.fxml"));

        BorderPane viewRoot = loader.load();
        SimulationLauncherPresenter presenter = loader.getController();
        presenter.init();
        this.configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);

        primaryStage.setScene(scene);

        primaryStage.setTitle("Simulation launcher");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
