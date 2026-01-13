package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private DarwinWorldMap map;

    @FXML
    private Label moveInfoLabel;

    @FXML
    private Canvas worldCanvas;

    private static final int CELL_SIZE = 32;
    private static final int BORDER = 2;
    private static final int BORDER_OFFSET = BORDER / 2;
    private static final List<Vector2d> START_POSITIONS = List.of(new Vector2d(2, 3), new Vector2d(4, 4));

    public void setWorldMap(DarwinWorldMap map) {
        this.map = map;
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

    private void drawCell(GraphicsContext graphics, String text, int x, int y) {
        graphics.fillText(
                text,
                x * CELL_SIZE + CELL_SIZE / 2 + BORDER_OFFSET,
                this.worldCanvas.getHeight() - (y * CELL_SIZE + CELL_SIZE / 2 + BORDER_OFFSET)
        );
    }

    private void drawCoordinates(GraphicsContext graphics, int mapWidth, int mapHeight, Vector2d lowerLeft) {
        this.drawCell(graphics, "y/x", 0, mapHeight-1);
        int cellX = lowerLeft.getX();
        for (int x = 1; x < mapWidth; x += 1) {
            this.drawCell(graphics, String.valueOf(cellX), x, mapHeight - 1);
            cellX += 1;
        }
        int cellY = lowerLeft.getY();
        for (int y = 0; y < mapHeight - 1; y += 1) {
            this.drawCell(graphics, String.valueOf(cellY), 0, y);
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
            this.drawCell(graphics, element.toString(), cellX, cellY);
        }
    }

    @Override
    public void mapChanged(DarwinWorldMap worldMap, String message) {
        Platform.runLater(() -> {
            this.drawMap();
            this.moveInfoLabel.setText(message);
        });
    }

    public void startSimulation(String movesText) {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);
        this.setWorldMap(map);
        map.addObserver(this);
        try {
            Simulation simulation = new Simulation(this.map, START_POSITIONS, Config.DEFAULT);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            engine.runAsync();
        } catch (RuntimeException e) {
            this.moveInfoLabel.setText(e.getMessage());
        }
    }
}
