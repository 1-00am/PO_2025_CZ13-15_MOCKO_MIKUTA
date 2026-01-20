package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final DarwinWorldMap worldMap;
    private boolean running = true;
    private boolean paused = false;
    private int day = 0;

    public Simulation(DarwinWorldMap worldMap, List<Vector2d> startingPositions, Config worldConfig) {
        this.animals = new ArrayList<>();
        this.worldMap = worldMap;
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
        //if (day > 1) {return;}
        this.worldMap.clearGrid();
        this.worldMap.removeDeadAnimals();
        this.worldMap.handleMovement();
        this.worldMap.handleEating();
        this.worldMap.handleReproduction();
        this.worldMap.spawnGrasses();
        this.worldMap.advanceDay();
    }

    @Override
    public void run() {
        while (this.running) {
            if (!this.paused) {
                this.step();
                this.day += 1;
            }
            try {
                Thread.sleep(500);
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
}
