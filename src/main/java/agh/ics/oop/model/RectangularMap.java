package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap {
    private final Boundary boundary;

    public RectangularMap(int width, int height) {
        this.boundary = new Boundary(Vector2d.ZERO, new Vector2d(width - 1, height - 1));
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && position.follows(Vector2d.ZERO) && position.precedes(this.boundary.upperRight());
    }

    @Override
    public Boundary getCurrentBounds() {
        return this.boundary;
    }
}


