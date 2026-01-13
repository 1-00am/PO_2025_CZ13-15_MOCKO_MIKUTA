package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement {
    private MapDirection mapDirection = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2, 2);
    private final List<Integer> genes;
    private int energy;
    private int age = 0;
    private int activeGeneIndex;

    public Animal(Vector2d position, Config worldConfig) {
        this.position = position;
        this.genes = new ArrayList<>();
        this.energy = worldConfig.startingEnergy();
        this.activeGeneIndex = new Random().nextInt(worldConfig.geneCount());

        for (int i = 0; i < worldConfig.geneCount(); i++) {
            this.genes.add(new Random().nextInt(8));
        }
    }

    @Override
    public String toString() {
        return switch (this.mapDirection) {
            case MapDirection.NORTH -> "⬆️";
            case MapDirection.NORTH_EAST -> "↗️";
            case MapDirection.EAST -> "➡️";
            case MapDirection.SOUTH_EAST -> "↘️";
            case MapDirection.SOUTH -> "⬇️";
            case MapDirection.SOUTH_WEST -> "↙️";
            case MapDirection.WEST -> "⬅️";
            case MapDirection.NORTH_WEST -> "↖️";
        };
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void rotate() {
        this.mapDirection = this.mapDirection.rotate(this.genes.get(this.activeGeneIndex));
    }

    public void advanceDay(Config config) {
        this.subtractEnergy(config.energyLossPerDay());
        this.age += 1;
        this.activeGeneIndex = (this.activeGeneIndex + 1) % this.genes.size();
    }

    public void move(Config config) {
        var newPosition = this.getPosition().add(this.mapDirection.toUnitVector());
        var newX = newPosition.getX();
        var newY = newPosition.getY();
        var maxX = config.width() - 1;
        var maxY = config.height() - 1;

        // loop around x
        if (newX < 0) {
            newX = maxX;
        } else if (newX > maxX) {
            newX = 0;
        }

        // bounce from y
        if (newY < 0) {
            newY = -newY;
            this.mapDirection = this.mapDirection.mirrorVertically();
        } else if (newY > maxY) {
            newY = 2 * maxY - newY;
            this.mapDirection = this.mapDirection.mirrorVertically();
        }

        this.position = new Vector2d(newX, newY);
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getMapDirection() {
        return this.mapDirection;
    }

    public boolean isDead() {
        return this.energy == 0;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
    }

    public void subtractEnergy(int energy) {
        this.energy -= energy;
        this.energy = Math.max(0, this.energy);
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getAge() {
        return this.age;
    }
}
