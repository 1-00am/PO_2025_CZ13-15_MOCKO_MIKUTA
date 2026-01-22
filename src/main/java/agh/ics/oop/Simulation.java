package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final DarwinWorldMap worldMap;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private volatile int sleepDuration = 500;
    private int day = 0;
    private final boolean fires;

    public Simulation(DarwinWorldMap worldMap, List<Vector2d> startingPositions, Config worldConfig) {
        this.animals = new ArrayList<>();
        this.worldMap = worldMap;
        this.fires = worldConfig.firesEnabled();
        for (var position : startingPositions) {
            var animal = new Animal(position, worldConfig, this.day);
            try {
                this.worldMap.place(animal);
                this.animals.add(animal);
            } catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }
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
                this.day += 1;
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
