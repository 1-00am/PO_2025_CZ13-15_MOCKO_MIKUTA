package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private Config config;
    private DarwinWorldMap map;
    private Simulation simulation;

    @FXML
    private Canvas worldCanvas;

    @FXML
    private Button pauseButton;

    @FXML
    private Slider iterationsSlider;

    @FXML
    private Label iterationsLabel;

    private static final int CELL_SIZE = 32;
    private static final int BORDER = 2;
    private static final int BORDER_OFFSET = BORDER / 2;
    private static final List<Vector2d> START_POSITIONS = List.of(new Vector2d(0, 0), new Vector2d(0, 0));
    private static final Image FIRE_IMAGE = new Image("fire.png");

    public void init() {
        this.iterationsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double iterations = this.iterationsSlider.getValue();
            this.iterationsLabel.setText("%.2f its/s".formatted(iterations));
            this.simulation.setSleepDuration((int)(1000.0 / iterations));
        });
    }

    public void setConfig(Config newConfig) {
        this.config = newConfig;
    }

    public void onPauseButtonClick() {
        if (simulation.isPaused()) {
            simulation.resume();
            this.pauseButton.setText("Pause");
        } else {
            simulation.pause();
            this.pauseButton.setText("Resume");
        }
    }

    public void drawMap() {
        var bounds = this.map.getCurrentBounds();

        int mapWidth = bounds.upperRight().getX() - bounds.lowerLeft().getX() + 2;
        int mapHeight = bounds.upperRight().getY() - bounds.lowerLeft().getY() + 2;

        int canvasWidth = mapWidth * CELL_SIZE + BORDER;
        int canvasHeight = mapHeight * CELL_SIZE + BORDER;

        this.worldCanvas.setWidth(canvasWidth);
        this.worldCanvas.setHeight(canvasHeight);

        GraphicsContext graphics = this.worldCanvas.getGraphicsContext2D();
        this.clearGrid(graphics);
        this.drawGrid(graphics);

        this.configureFont(graphics, 16, Color.BLACK);
        this.drawCoordinates(graphics, mapWidth, mapHeight, bounds.lowerLeft());

        this.configureFont(graphics, 32, Color.RED);
        this.drawMapElements(graphics, bounds.lowerLeft());
    }

    private void clearGrid(GraphicsContext graphics) {
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, this.worldCanvas.getWidth(), this.worldCanvas.getHeight());
    }

    private void drawGrid(GraphicsContext graphics) {
        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(BORDER);
        for (int x = 0; x < this.worldCanvas.getWidth() + 1; x += CELL_SIZE) {
            graphics.strokeLine(x + BORDER_OFFSET, 0, x + BORDER_OFFSET, this.worldCanvas.getHeight());
        }
        for (int y = 0; y < this.worldCanvas.getHeight() + 1; y += CELL_SIZE) {
            graphics.strokeLine(0, y + BORDER_OFFSET, this.worldCanvas.getWidth(), y + BORDER_OFFSET);
        }
    }

    private void configureFont(GraphicsContext graphics, int size, Color color) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(new Font("Arial", size));
        graphics.setFill(color);
    }

    private void drawTextCell(GraphicsContext graphics, String text, int x, int y) {
        graphics.fillText(
                text,
                x * CELL_SIZE + CELL_SIZE / 2 + BORDER_OFFSET,
                this.worldCanvas.getHeight() - (y * CELL_SIZE + CELL_SIZE / 2 + BORDER_OFFSET)
        );
    }

    private void drawWorldElement(GraphicsContext graphics, WorldElement element, int x, int y) {
        graphics.drawImage(
            element.getImage(),
            x * CELL_SIZE + BORDER_OFFSET,
            this.worldCanvas.getHeight() - (y * CELL_SIZE + CELL_SIZE + BORDER_OFFSET)
        );
        if (element.isBurning()) {
            graphics.drawImage(
                    FIRE_IMAGE,
                    x * CELL_SIZE + BORDER_OFFSET,
                    this.worldCanvas.getHeight() - (y * CELL_SIZE + CELL_SIZE + BORDER_OFFSET)
            );
        }
    }

    private void drawCoordinates(GraphicsContext graphics, int mapWidth, int mapHeight, Vector2d lowerLeft) {
        this.drawTextCell(graphics, "y/x", 0, mapHeight-1);
        int cellX = lowerLeft.getX();
        for (int x = 1; x < mapWidth; x += 1) {
            this.drawTextCell(graphics, String.valueOf(cellX), x, mapHeight - 1);
            cellX += 1;
        }
        int cellY = lowerLeft.getY();
        for (int y = 0; y < mapHeight - 1; y += 1) {
            this.drawTextCell(graphics, String.valueOf(cellY), 0, y);
            cellY += 1;
        }
    }

    private void drawMapElements(GraphicsContext graphics, Vector2d lowerLeft) {
        for (var element : this.map.getElements()) {
            // if object is not the primary one, then skip
            if (this.map.objectAt(element.getPosition()) != element) {
                continue;
            }
            var position = element.getPosition();
            var cellX = position.getX() - lowerLeft.getX() + 1;
            var cellY = position.getY() - lowerLeft.getY();
            this.drawWorldElement(graphics, element, cellX, cellY);
        }
    }

    @Override
    public void mapChanged(DarwinWorldMap worldMap) {
        Platform.runLater(() -> {
            this.drawMap();
        });
    }

    public void startSimulation() {
        this.map = new DarwinWorldMap(this.config);
        map.addObserver(this);
        try {
            this.simulation = new Simulation(this.map, START_POSITIONS, this.config);
            var simulationThread = new Thread(this.simulation);
            simulationThread.start();
        } catch (RuntimeException e) {
            // ignore
        }
    }

    public void exitSimulation() {
        this.simulation.exit();
    }
}
