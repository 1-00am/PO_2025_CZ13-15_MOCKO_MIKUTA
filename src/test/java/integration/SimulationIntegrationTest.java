package integration;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SimulationIntegrationTest {
    @Test
    void noMoves() {
        Vector2d[] startingPositions = new Vector2d[] { new Vector2d(0, 0), new Vector2d(1, 0), new Vector2d(0, 1) };
        Simulation simulation = new Simulation(new RectangularMap(4, 4), Arrays.asList(startingPositions), OptionsParser.parseMoveDirections(new String[0]));
        simulation.run();
        var simulationAnimals = simulation.getAnimals();
        for (int i = 0; i < simulationAnimals.size(); i++) {
            assertEquals(startingPositions[i], simulationAnimals.get(i).getPosition());
            assertEquals(MapDirection.NORTH, simulationAnimals.get(i).getMapDirection());
        }
    }

    @Test
    void correctMoves() {
        String[] args = new String[] {"f", "b", "r", "l"};
        Vector2d[] startingPositions = new Vector2d[] { new Vector2d(0, 0), new Vector2d(1, 0), new Vector2d(0, 1), new Vector2d(0, 2) };
        Simulation simulation = new Simulation(new RectangularMap(4, 4), Arrays.asList(startingPositions), OptionsParser.parseMoveDirections(args));
        simulation.run();

        MapDirection[] expectedMapDirections = new MapDirection[] { MapDirection.NORTH, MapDirection.NORTH, MapDirection.EAST, MapDirection.WEST };
        Vector2d[] expectedPositions = new Vector2d[] { new Vector2d(0, 0), new Vector2d(1, 0), new Vector2d(0, 1), new Vector2d(0, 2) };

        var simulationAnimals = simulation.getAnimals();
        for (int i = 0; i < simulationAnimals.size(); i++) {
            assertEquals(expectedPositions[i], simulationAnimals.get(i).getPosition());
            assertEquals(expectedMapDirections[i], simulationAnimals.get(i).getMapDirection());
        }
    }

    @Test
    void incorrectMoves() {
        String[] args = new String[] {"f", "b", "r", "l", "aa", "bb", "", "f b"};
        Vector2d[] startingPositions = new Vector2d[] { new Vector2d(1, 1), new Vector2d(1, 0), new Vector2d(0, 1), new Vector2d(0, 2) };
        assertThrows(IllegalArgumentException.class, () -> new Simulation(new RectangularMap(4, 4), Arrays.asList(startingPositions), OptionsParser.parseMoveDirections(args)));
    }

    @Test
    void constrained() {
        String[] args = new String[] {"b", "f", "f", "f", "f", "f", "r", "f", "f", "f", "f", "f", "b", "b", "b", "b", "b"};
        Vector2d[] startingPositions = new Vector2d[] { new Vector2d(0, 0) };
        Simulation simulation = new Simulation(new RectangularMap(5, 5), Arrays.asList(startingPositions), OptionsParser.parseMoveDirections(args));
        simulation.run();

        assertEquals(new Vector2d(0, 4), simulation.getAnimals().getFirst().getPosition());
    }
}