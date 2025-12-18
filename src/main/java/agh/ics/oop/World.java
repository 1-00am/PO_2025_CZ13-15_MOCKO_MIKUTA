package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.List;

public class World {
    public static void main(String[] args) {
        var animal = new Animal(new Vector2d(0, 1));
        IO.println(animal);

        try {
            List<MoveDirection> directions = OptionsParser.parseMoveDirections(args);
            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
            var grassField = new GrassField(10);
            grassField.addObserver(new ConsoleMapDisplay());
            Simulation simulation = new Simulation(grassField, positions, directions);

            RectangularMap rectangularMap = new RectangularMap(5, 5);
            rectangularMap.addObserver(new ConsoleMapDisplay());
            Simulation simulation2 = new Simulation(rectangularMap, positions, directions);

            SimulationEngine engine = new SimulationEngine(List.of(simulation, simulation2));
            engine.runAsyncInThreadPool();
            try {
                engine.awaitSimulationsEnd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        IO.println("System zakończył działanie");
    }
}
