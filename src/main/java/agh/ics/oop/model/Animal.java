package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement {
    private MapDirection mapDirection;
    private Vector2d position = new Vector2d(2, 2);

    private final Genome genome;
    private final int birthDate;
    private int deathDate;
    private final List<Animal> children = new ArrayList<>();

    private int energy;
    private int burnDaysLeft = 0;
    private int burningDailyPenalty;

    private final static Random rand = new Random();

    public Animal(Vector2d position, Config worldConfig, int day) {
        this.position = position;
        this.energy = worldConfig.startingEnergy();
        this.birthDate = day;
        this.mapDirection = MapDirection.values()[Animal.rand.nextInt(MapDirection.values().length)];
        this.genome = new Genome(worldConfig.geneCount());
        this.burningDailyPenalty = worldConfig.burningDailyPenalty();
    }

    public Animal(Animal parentDom, Animal parentSub, int childEnergy, int day) {
        this.position = parentDom.getPosition();
        this.energy = childEnergy;
        this.birthDate = day;
        this.mapDirection = MapDirection.values()[Animal.rand.nextInt(MapDirection.values().length)];
        this.burningDailyPenalty = parentDom.getBurningDailyPenalty();

        int energyDom = parentDom.getEnergy();
        int energySub = parentSub.getEnergy();
        int totalEnergy = energyDom + energySub;

        this.genome = new Genome(parentDom.getGenome(), parentSub.getGenome(), (float)energyDom / (float)totalEnergy);
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
        this.mapDirection = this.mapDirection.rotate(this.genome.getActiveGene());
    }

    public void advanceDay(Config config) {
        this.subtractEnergy(config.energyLossPerDay());
        this.genome.advanceGene();
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

    public int getBirthDate() {
        return this.birthDate;
    }

    public int getDeathDate() {
        return this.deathDate;
    }

    public void setDeathDate(int deathDate) {
        this.deathDate = deathDate;
    }

    public void burn() {
        if (this.burnDaysLeft > 0) {
            this.burnDaysLeft--;
            subtractEnergy(this.burningDailyPenalty);
        }
    }

    public int getBurningDailyPenalty() {
        return this.burningDailyPenalty;
    }

    public void startBurning(int days) {
        this.burnDaysLeft = days;
    }

    public int getChildrenCount() {
        return this.children.size();
    }

    public Genome getGenome() {
        return this.genome;
    }

    public Animal reproduce(Animal other, int energyUsed, int day) {
        Animal child;
        if (this.getEnergy() > other.getEnergy()) {
            child = new Animal(this, other, 2 * energyUsed, day);
        } else {
            child = new Animal(other, this, 2 * energyUsed, day);
        }
        this.children.add(child);
        return child;
    }
}
