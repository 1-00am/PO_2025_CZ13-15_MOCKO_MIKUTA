package agh.ics.oop.model;

public class Animal implements WorldElement {
    private MapDirection mapDirection = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2, 2);

    public Animal() {}

    public Animal(Vector2d position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return switch (this.mapDirection) {
            case MapDirection.NORTH -> "^";
            case MapDirection.SOUTH -> "v";
            case MapDirection.EAST -> ">";
            case MapDirection.WEST -> "<";
        };
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveDirection direction, MoveValidator moveValidator) {
        switch (direction) {
            case MoveDirection.RIGHT -> this.mapDirection = this.mapDirection.next();
            case MoveDirection.LEFT -> this.mapDirection = this.mapDirection.previous();
            case MoveDirection.FORWARD -> {
                var newPosition = this.position.add(this.mapDirection.toUnitVector());
                if (moveValidator.canMoveTo(newPosition)) {
                    this.position = newPosition;
                }
            }
            case MoveDirection.BACKWARD -> {
                var newPosition = this.position.subtract(this.mapDirection.toUnitVector());
                if (moveValidator.canMoveTo(newPosition)) {
                    this.position = newPosition;
                }
            }
        }
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getMapDirection() {
        return this.mapDirection;
    }
}
