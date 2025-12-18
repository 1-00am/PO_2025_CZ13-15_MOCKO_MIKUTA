package integration;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapIntegrationTest {
    @Test
    void place() {
        var map = new RectangularMap(5, 5);

        var pos1 = new Vector2d(0, 0);
        assertDoesNotThrow(() -> map.place(new Animal(pos1)));
        assertTrue(map.isOccupied(pos1));
        assertFalse(map.canMoveTo(pos1));
        assertNotNull(map.objectAt(pos1));

        var pos2 = new Vector2d(1, 4);
        assertDoesNotThrow(() -> map.place(new Animal(pos2)));
        assertTrue(map.isOccupied(pos2));
        assertFalse(map.canMoveTo(pos2));
        assertNotNull(map.objectAt(pos2));

        var pos3 = new Vector2d(4, 4);
        assertDoesNotThrow(() -> map.place(new Animal(pos3)));
        assertTrue(map.isOccupied(pos3));
        assertFalse(map.canMoveTo(pos3));
        assertNotNull(map.objectAt(pos3));

        var pos4 = new Vector2d(0, 0);
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(pos4)));
        assertTrue(map.isOccupied(pos4));
        assertFalse(map.canMoveTo(pos4));
        assertNotNull(map.objectAt(pos4));

        var pos5 = new Vector2d(4, 4);
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(pos5)));
        assertTrue(map.isOccupied(pos5));
        assertFalse(map.canMoveTo(pos5));
        assertNotNull(map.objectAt(pos5));

        var pos6 = new Vector2d(5, 0);
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(pos6)));
        assertFalse(map.isOccupied(pos6));
        assertFalse(map.canMoveTo(pos6));
        assertNull(map.objectAt(pos6));

        var pos7 = new Vector2d(-1, 0);
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(pos7)));
        assertFalse(map.isOccupied(pos7));
        assertFalse(map.canMoveTo(pos7));
        assertNull(map.objectAt(pos7));
    }

    @Test
    void move1() {
        var map = new RectangularMap(5, 5);
        var animals = new ArrayList<Animal>();
        var moves = new MoveDirection[] { MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.BACKWARD, MoveDirection.RIGHT, MoveDirection.LEFT };
        var expectedPositions = new Vector2d[] { new Vector2d(0, 1), new Vector2d(2, 0), new Vector2d(3, 0), new Vector2d(3, 3), new Vector2d(4, 4) };
        var expectedDirections = new MapDirection[] { MapDirection.NORTH, MapDirection.NORTH, MapDirection.NORTH, MapDirection.EAST, MapDirection.WEST };
        animals.add(new Animal(new Vector2d(0, 0)));
        animals.add(new Animal(new Vector2d(2, 0)));
        animals.add(new Animal(new Vector2d(3, 1)));
        animals.add(new Animal(new Vector2d(3, 3)));
        animals.add(new Animal(new Vector2d(4, 4)));

        for (var animal : animals) {
            assertDoesNotThrow(() -> map.place(animal));
        }

        for (var i = 0; i < animals.size(); i++) {
            map.move(animals.get(i), moves[i]);
            assertEquals(expectedPositions[i], animals.get(i).getPosition());
            assertEquals(expectedDirections[i], animals.get(i).getMapDirection());
        }
    }

    @Test
    void move2() {
        var map = new RectangularMap(5, 5);
        var animals = new ArrayList<Animal>();
        var moves = new MoveDirection[] { MoveDirection.BACKWARD, MoveDirection.LEFT, MoveDirection.RIGHT, MoveDirection.FORWARD, MoveDirection.FORWARD };
        var expectedPositions = new Vector2d[] { new Vector2d(4, 3), new Vector2d(1, 3), new Vector2d(2, 2), new Vector2d(0, 1), new Vector2d(1, 1) };
        var expectedDirections = new MapDirection[] { MapDirection.NORTH, MapDirection.WEST, MapDirection.EAST, MapDirection.NORTH, MapDirection.NORTH };
        animals.add(new Animal(new Vector2d(4, 4)));
        animals.add(new Animal(new Vector2d(1, 3)));
        animals.add(new Animal(new Vector2d(2, 2)));
        animals.add(new Animal(new Vector2d(0, 0)));
        animals.add(new Animal(new Vector2d(1, 0)));

        for (var animal : animals) {
            assertDoesNotThrow(() -> map.place(animal));
        }

        for (var i = 0; i < animals.size(); i++) {
            map.move(animals.get(i), moves[i]);
            assertEquals(expectedPositions[i], animals.get(i).getPosition());
            assertEquals(expectedDirections[i], animals.get(i).getMapDirection());
        }
    }
}