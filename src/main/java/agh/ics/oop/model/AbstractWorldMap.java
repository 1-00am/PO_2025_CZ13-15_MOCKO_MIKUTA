package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    private final HashMap<Vector2d, Animal> animals = new HashMap<>();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (!this.canMoveTo(animal.getPosition())) {
            throw new IncorrectPositionException(animal.getPosition());
        }
        this.animals.put(animal.getPosition(), animal);
        this.mapChanged("Animal placed at %s".formatted(animal.getPosition()));
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        var oldPosition = animal.getPosition();
        var oldDirection = animal.getMapDirection();
        if (!this.animals.containsKey(oldPosition)) {
            return;
        }
        animal.move(direction, this);
        if (!animal.getPosition().equals(oldPosition)) {
            this.animals.remove(oldPosition);
            this.animals.put(animal.getPosition(), animal);
            this.mapChanged("Animal moved from %s to %s".formatted(oldPosition, animal.getPosition()));
        } else if (!animal.getMapDirection().equals(oldDirection)) {
            this.mapChanged("Animal rotated from %s to %s".formatted(oldDirection, animal.getMapDirection()));
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.animals.containsKey(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !this.animals.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return this.animals.get(position);
    }

    @Override
    public Collection<WorldElement> getElements() {
        return new ArrayList<>(this.animals.values());
    }

    @Override
    public String toString() {
        var boundary = this.getCurrentBounds();
        return this.mapVisualizer.draw(boundary.lowerLeft(), boundary.upperRight());
    }

    public void addObserver(MapChangeListener observer) {
        if (this.observers.contains(observer)) {
            return;
        }
        this.observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        this.observers.remove(observer);
    }

    private void mapChanged(String message) {
        for (var observer : this.observers) {
            observer.mapChanged(this, message);
        }
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
