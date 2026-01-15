package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement {
    private MapDirection mapDirection;
    private Vector2d position = new Vector2d(2, 2);

    private final List<Integer> genes;
    private final int birthDate;
    private final List<Animal> children = new ArrayList<>();

    private int energy;
    private int activeGeneIndex;

    private static Random rand = new Random();

    public Animal(Vector2d position, Config worldConfig, int day) {
        this.position = position;
        this.energy = worldConfig.startingEnergy();
        this.birthDate = day;
        this.mapDirection = MapDirection.values()[Animal.rand.nextInt(MapDirection.values().length)];

        this.genes = new ArrayList<>();
        for (int i = 0; i < worldConfig.geneCount(); i++) {
            this.genes.add(new Random().nextInt(8));
        }
        this.activeGeneIndex = Animal.rand.nextInt(genes.size());

    }

    public Animal(Animal parentDom, Animal parentSub, int childEnergy, int day) {
        this.position = parentDom.getPosition();
        this.energy = childEnergy;
        this.birthDate = day;
        this.mapDirection = MapDirection.values()[Animal.rand.nextInt(MapDirection.values().length)];


        int energyDom = parentDom.getEnergy();
        int energySub = parentSub.getEnergy();
        int totalEnergy = energyDom + energySub;

        int genomeLength = parentDom.getGenes().size();
        int domGenesCount = (int) Math.round(
                (double) energyDom / totalEnergy * genomeLength
        );

        boolean domOnLeft = Math.random() > 0.5;

        List<Integer> genes = new ArrayList<>(genomeLength);

        if (domOnLeft) {
            genes.addAll(parentDom.getGenes().subList(0, domGenesCount));
            genes.addAll(parentSub.getGenes().subList(domGenesCount, genomeLength));
        } else {
            genes.addAll(parentSub.getGenes().subList(0, genomeLength - domGenesCount));
            genes.addAll(parentDom.getGenes().subList(genomeLength - domGenesCount, genomeLength));
        }

        this.genes = genes;
        this.activeGeneIndex = Animal.rand.nextInt(genes.size());
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

    public int getBirthDate() {
        return this.birthDate;
    }

    public int getChildrenCount() {
        return this.children.size();
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public Animal reproduce(Animal other, int energyUsed, int day) {
        if (this.getEnergy() > other.getEnergy()) {
            return new Animal(this, other, 2*energyUsed, day);
        } else {
            return new Animal(other, this, 2*energyUsed, day);
        }

    }
}
