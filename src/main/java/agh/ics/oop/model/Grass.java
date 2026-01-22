package agh.ics.oop.model;

import javafx.scene.image.Image;

public class Grass implements WorldElement {
    private static final Image IMAGE = new Image("grass.png");
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
        return "*";
    }

    public boolean isJungle() {
        return this.isJungle;
    }

    @Override
    public boolean isBurning() { return this.isBurning; }

    public void startFire() { this.isBurning = true; }

    @Override
    public Image getImage() {
        return Grass.IMAGE;
    }
}
