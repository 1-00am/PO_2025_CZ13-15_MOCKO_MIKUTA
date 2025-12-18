package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.exceptions.IncorrectPositionException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final List<MoveDirection> moves;
    private final WorldMap worldMap;

    public Simulation(WorldMap worldMap, List<Vector2d> startingPositions, List<MoveDirection> moves) {
        this.animals = new ArrayList<>();
        this.moves = moves;
        this.worldMap = worldMap;
        for (var position : startingPositions) {
            var animal = new Animal(position);
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
        var i = 0;
        for (var direction : this.moves) {
            this.worldMap.move(this.animals.get(i), direction);
            i = (i+1) % this.animals.size();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // no big deal, ignore
            }
        }
    }

    public List<Animal> getAnimals() {
        return this.animals;
    }
}
