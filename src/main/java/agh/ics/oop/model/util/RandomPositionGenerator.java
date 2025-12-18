package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final List<Vector2d> positions = new ArrayList<>();
    private final int count;

    public RandomPositionGenerator(int width, int height, int count) {
        this.count = count;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.positions.add(new Vector2d(i, j));
            }
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        Collections.shuffle(positions);
        return new Iterator<>() {
            private int current = 0;
            private final List<Vector2d> remainingPositions = new ArrayList<>(positions);

            @Override
            public boolean hasNext() {
                return this.current < count && !this.remainingPositions.isEmpty();
            }

            @Override
            public Vector2d next() {
                return this.remainingPositions.get(this.current++);
            }
        };
    }
}