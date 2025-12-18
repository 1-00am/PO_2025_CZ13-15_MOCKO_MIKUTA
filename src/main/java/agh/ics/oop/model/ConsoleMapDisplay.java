package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int changeCounter = 0;
    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {
            IO.println("Map id: %s".formatted(worldMap.getId()));
            IO.println(message);
            IO.println(worldMap);
            IO.println("Change count: %d".formatted(++changeCounter));
        }
    }
}
