package agh.ics.oop.model;

import javafx.scene.canvas.GraphicsContext;

public interface WorldElement {
    Vector2d getPosition();
    boolean isBurning();
    void draw(GraphicsContext graphics, double x, double y);
}