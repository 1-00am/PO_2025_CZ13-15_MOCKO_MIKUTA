package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationLauncherPresenter {
    public void onSimulationStartClicked() throws Exception {
        Stage newWindow = new Stage();
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));

        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        newWindow.setOnCloseRequest((event) -> {
            presenter.exitSimulation();
        });
        presenter.startSimulation();
        this.configureStage(newWindow, viewRoot);
        newWindow.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);

        primaryStage.setScene(scene);

        primaryStage.setTitle("Simulation");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
