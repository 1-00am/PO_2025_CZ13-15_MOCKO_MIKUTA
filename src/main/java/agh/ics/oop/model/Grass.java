package agh.ics.oop.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Grass implements WorldElement {
    private static Image IMAGE;
    private static Image getImage() {
        if (IMAGE == null) {
            IMAGE = new Image("grass.png");
        }
        return IMAGE;
    }
    private static Image FIRE;
    private static Image getFire() {
        if (FIRE == null) {
            FIRE = new Image("fire.png");
        }
        return FIRE;
    }
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
    public void draw(GraphicsContext graphics, double x, double y) {
        graphics.drawImage(getImage(), x, y);
        if (this.isBurning) {
            graphics.drawImage(getFire(), x, y);
        }
    }
}
