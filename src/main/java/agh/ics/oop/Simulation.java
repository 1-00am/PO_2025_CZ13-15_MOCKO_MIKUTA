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

    public Simulation(DarwinWorldMap worldMap, List<Vector2d> startingPositions, Config worldConfig) {
        this.animals = new ArrayList<>();
        this.worldMap = worldMap;
        for (var position : startingPositions) {
            var animal = new Animal(position, worldConfig);
            try {
                this.worldMap.place(animal);
                this.animals.add(animal);
            } catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (this.running) {
            if (!this.paused) {
                this.worldMap.step();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // ingore
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
}
