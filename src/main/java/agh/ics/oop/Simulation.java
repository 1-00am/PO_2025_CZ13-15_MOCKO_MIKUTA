package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.StatsCSVWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

public class Simulation implements Runnable {
    private final DarwinWorldMap worldMap;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private volatile int sleepDuration = 500;
    private final boolean fires;
    private final boolean writeToCSV;
    private final UUID uuid = UUID.randomUUID();
    private final StatsCSVWriter statsCSVWriter;

    public Simulation(DarwinWorldMap worldMap, Config worldConfig) {
        this.worldMap = worldMap;
        this.fires = worldConfig.firesEnabled();
        this.writeToCSV = worldConfig.writeToCSV();
        StatsCSVWriter writer = null;
        if (this.writeToCSV) {
            try {
                writer = new StatsCSVWriter(Path.of("./%s.csv".formatted(this.uuid)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.statsCSVWriter = writer;
    }

    public void step() {
        this.worldMap.clearGrid();
        this.worldMap.removeDeadAnimals();
        this.worldMap.handleMovement();
        this.worldMap.handleEating(fires);
        this.worldMap.handleReproduction();
        if (this.fires) {this.worldMap.handleFiresGrass();}
        this.worldMap.spawnGrasses();
        if (this.fires) {this.worldMap.handleFiresAnimals();}
        this.worldMap.advanceDay();
        if (this.writeToCSV && this.statsCSVWriter != null) {
            try {
                this.statsCSVWriter.writeStats(this.worldMap.getStats());
                this.statsCSVWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                if (this.paused) {
                    Thread.sleep(300);
                    continue;
                }
                this.step();
                Thread.sleep(this.sleepDuration);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void resume() {
        this.paused = false;
    }

    public void pause() {
        this.paused = true;
    }

    public void exit() {
        this.running = false;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setSleepDuration(int sleepDuration) {
        this.sleepDuration = sleepDuration;
    }
}
