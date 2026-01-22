package agh.ics.oop.model;

public class Grass implements WorldElement {
    private final Vector2d position;
    private final boolean isJungle;
    private boolean isBurning = false;

    public Grass(Vector2d position,  boolean isJungle) {
        this.position = position;
        this.isJungle = isJungle;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        if (this.isBurning) {
            return "\uD83D\uDD25";
        } else {
            return "*";
        }
    }

    public boolean isJungle() {
        return this.isJungle;
    }

    public boolean isBurning() { return this.isBurning; }

    public void startFire() { this.isBurning = true; }
}
