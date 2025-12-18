package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.Collection;
import java.util.HashMap;

public class GrassField extends AbstractWorldMap {
    private final HashMap<Vector2d, Grass> grasses = new HashMap<>();

    public GrassField(int grassCount) {
        int bound = (int) Math.ceil(Math.sqrt(10*grassCount));
        var generator = new RandomPositionGenerator(bound, bound, grassCount);
        for (var position : generator) {
            this.grasses.put(position, new Grass(position));
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || this.grasses.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement element = super.objectAt(position);
        return (element != null ? element : this.grasses.get(position));
    }

    @Override
    public Collection<WorldElement> getElements() {
        var elements = super.getElements();
        elements.addAll(this.grasses.values());
        return elements;
    }

    @Override
    public Boundary getCurrentBounds() {
        var elements = this.getElements();
        if (elements.isEmpty()) {
            return new Boundary(new Vector2d(0, 0), new Vector2d(0, 0));
        }
        var upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        var lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (var element : this.getElements()) {
            var position = element.getPosition();
            upperRight = upperRight.upperRight(position);
            lowerLeft = lowerLeft.lowerLeft(position);
        }
        return new Boundary(lowerLeft, upperRight);
    }
}