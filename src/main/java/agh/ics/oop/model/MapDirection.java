package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    private final static MapDirection[] values = MapDirection.values();

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case NORTH_EAST -> "Północny wschód";
            case EAST -> "Wschód";
            case SOUTH_EAST -> "Południowy wschód";
            case SOUTH -> "Południe";
            case SOUTH_WEST -> "Południowy zachód";
            case WEST -> "Zachód";
            case NORTH_WEST -> "Północny zachód";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTH_WEST;
            case NORTH_WEST -> WEST;
            case WEST -> SOUTH_WEST;
            case SOUTH_WEST -> SOUTH;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_EAST -> EAST;
            case EAST -> NORTH_EAST;
            case NORTH_EAST -> NORTH;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }

    public MapDirection rotate(int gene) {
        int discriminant = (gene + this.ordinal()) % 8;
        return MapDirection.values[discriminant];
    }

    public MapDirection mirrorVertically() {
        return switch (this) {
            case NORTH -> SOUTH;
            case NORTH_EAST -> SOUTH_EAST;
            case EAST -> EAST;
            case SOUTH_EAST -> NORTH_EAST;
            case SOUTH -> NORTH;
            case SOUTH_WEST -> NORTH_WEST;
            case WEST -> WEST;
            case NORTH_WEST -> SOUTH_WEST;
        };
    }
}
