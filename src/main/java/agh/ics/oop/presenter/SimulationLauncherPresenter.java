package agh.ics.oop.presenter;

import agh.ics.oop.model.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class SimulationLauncherPresenter {
    @FXML
    private Spinner<Integer> widthField;
    @FXML
    private Spinner<Integer> heightField;
    @FXML
    private Spinner<Double> jungleWorldSizePercentageField;
    @FXML
    private Spinner<Double> jungleGrassGrowthChanceField;
    @FXML
    private CheckBox firesEnabledField;
    @FXML
    private Spinner<Integer> burnTimeField;
    @FXML
    private Spinner<Integer> burningDailyPenaltyField;
    @FXML
    private Spinner<Double> fireStartChanceField;
    @FXML
    private Spinner<Integer> startingGrassCountField;
    @FXML
    private Spinner<Integer> newGrassesPerDayField;
    @FXML
    private Spinner<Integer> grassEnergyValueField;
    @FXML
    private Spinner<Integer> startingEnergyField;
    @FXML
    private Spinner<Integer> energyLossPerDayField;
    @FXML
    private Spinner<Integer> breedingEnergyNeededField;
    @FXML
    private Spinner<Integer> breedingEnergyUsedField;
    @FXML
    private Spinner<Integer> minMutationCountField;
    @FXML
    private Spinner<Integer> maxMutationCountField;
    @FXML
    private Spinner<Integer> geneCountField;
    @FXML
    private Spinner<Integer> startingAnimalCountField;
    @FXML
    private CheckBox writeToCSVField;

    static void initIntSpinner(Spinner<Integer> spinner, int min, int max, int initVal) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initVal));
    }

    static void initDoubleSpinner(Spinner<Double> spinner, double min, double max, double initVal, double step) {
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initVal, step));
    }

    public void init() {
        Config config = Config.DEFAULT;
        initIntSpinner(this.widthField, 2, 20, config.width());
        initIntSpinner(this.heightField, 2, 20, config.height());
        initDoubleSpinner(this.jungleWorldSizePercentageField, 0.0, 1.0, config.jungleWorldSizePercentage(), 0.1);
        initDoubleSpinner(this.jungleGrassGrowthChanceField, 0.0, 1.0, config.jungleGrassGrowthChance(), 0.1);
        initIntSpinner(this.burnTimeField, 1, Integer.MAX_VALUE, config.burnTime());
        initIntSpinner(this.burningDailyPenaltyField, 1, Integer.MAX_VALUE, config.burningDailyPenalty());
        initDoubleSpinner(this.fireStartChanceField, 0.0, 1.0, config.fireStartChance(), 0.1);
        initIntSpinner(this.startingGrassCountField, 0, 50, config.startingGrassCount());
        initIntSpinner(this.newGrassesPerDayField, 0, 10, config.newGrassesPerDay());
        initIntSpinner(this.grassEnergyValueField, 0, Integer.MAX_VALUE, config.grassEnergyValue());
        initIntSpinner(this.startingEnergyField, 0, Integer.MAX_VALUE, config.startingEnergy());
        initIntSpinner(this.energyLossPerDayField, 0, Integer.MAX_VALUE, config.energyLossPerDay());
        initIntSpinner(this.breedingEnergyNeededField, 0, Integer.MAX_VALUE, config.breedingEnergyNeeded());
        initIntSpinner(this.breedingEnergyUsedField, 0, Integer.MAX_VALUE, config.breedingEnergyUsed());
        initIntSpinner(this.minMutationCountField, 0, Integer.MAX_VALUE, config.minMutationCount());
        initIntSpinner(this.maxMutationCountField, 0, Integer.MAX_VALUE, config.maxMutationCount());
        initIntSpinner(this.geneCountField, 0, Integer.MAX_VALUE, config.geneCount());
        initIntSpinner(this.startingAnimalCountField, 0, Integer.MAX_VALUE, config.startingAnimalCount());
    }

    Config createConfigFromFields() {
        return new Config(
                this.widthField.getValue(),
                this.heightField.getValue(),
                (float)(double)this.jungleWorldSizePercentageField.getValue(),
                (float)(double)this.jungleGrassGrowthChanceField.getValue(),
                this.firesEnabledField.isSelected(),
                this.burnTimeField.getValue(),
                this.burningDailyPenaltyField.getValue(),
                (float)(double)this.fireStartChanceField.getValue(),
                this.startingGrassCountField.getValue(),
                this.newGrassesPerDayField.getValue(),
                this.grassEnergyValueField.getValue(),
                this.startingEnergyField.getValue(),
                this.energyLossPerDayField.getValue(),
                this.breedingEnergyNeededField.getValue(),
                this.breedingEnergyUsedField.getValue(),
                this.minMutationCountField.getValue(),
                this.maxMutationCountField.getValue(),
                this.geneCountField.getValue(),
                this.startingAnimalCountField.getValue(),
                this.writeToCSVField.isSelected()
        );
    }

    public void onSimulationStartClicked() throws Exception {
        Stage newWindow = new Stage();
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));

        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        Config config = createConfigFromFields();
        //IO.println(config);
        presenter.setConfig(config);
        presenter.init();
        newWindow.setOnCloseRequest(e -> {
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
