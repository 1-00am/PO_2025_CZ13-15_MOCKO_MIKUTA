package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {
    @Test
    void placeCorrect() {
        var map = new RectangularMap(5, 5);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(0, 0))));
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(4, 0))));
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(4, 4))));
    }

    @Test
    void placeOutOfBounds() {
        var map = new RectangularMap(4, 5);
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(new Vector2d(4, 6))));
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(new Vector2d(7, 0))));
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(new Vector2d(-1, 0))));
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(new Vector2d(0, -1))));
    }

    @Test
    void placeOccupied() {
        var map = new RectangularMap(4, 4);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(0, 0))));
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(3, 1))));
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(new Vector2d(0, 0))));
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(new Vector2d(3, 1))));
    }

    @Test
    void isOccupied() {
        var map = new RectangularMap(4, 4);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(0, 0))));
        assertTrue(map.isOccupied(new Vector2d(0, 0)));
    }

    @Test
    void canMoveToCorrect() {
        var map = new RectangularMap(5, 5);
        assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        assertTrue(map.canMoveTo(new Vector2d(4, 4)));
    }

    @Test
    void canMoveToOccupied() {
        var map = new RectangularMap(4, 4);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(1, 1))));
        assertFalse(map.canMoveTo(new Vector2d(1, 1)));
    }

    @Test
    void canMoveToOutOfBounds() {
        var map = new RectangularMap(2, 4);
        assertFalse(map.canMoveTo(new Vector2d(3, 4)));
        assertFalse(map.canMoveTo(new Vector2d(-1, 4)));
        assertFalse(map.canMoveTo(new Vector2d(4, 3)));
        assertFalse(map.canMoveTo(new Vector2d(4, -1)));
        assertFalse(map.canMoveTo(new Vector2d(2, 4)));
    }


    @Test
    void objectAt() {
        var map = new RectangularMap(4, 4);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(1, 1))));
        var animal = map.objectAt(new Vector2d(1, 1));
        assertNotNull(animal);
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        assertNull(map.objectAt(new Vector2d(0, 0)));
    }

    @Test
    void moveCorrect() {
        var map = new RectangularMap(4, 4);
        var animal = new Animal(new Vector2d(1, 1));
        assertDoesNotThrow(() -> map.place(animal));
        map.move(animal, MoveDirection.FORWARD);
        assertEquals(new Vector2d(1, 2), animal.getPosition());
        map.move(animal, MoveDirection.RIGHT);
        assertEquals(MapDirection.EAST, animal.getMapDirection());
    }

    @Test
    void moveOccupied() {
        var map = new RectangularMap(4, 4);
        var animal = new Animal(new Vector2d(1, 1));
        assertDoesNotThrow(() -> map.place(animal));
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(1, 2))));
        map.move(animal, MoveDirection.FORWARD);
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        map.move(animal, MoveDirection.RIGHT);
        assertEquals(MapDirection.EAST, animal.getMapDirection());
    }

    @Test
    void moveOutOfBounds() {
        var map = new RectangularMap(5, 5);
        var animal1 = new Animal(new Vector2d(0, 0));
        var animal2 = new Animal(new Vector2d(0, 4));
        var animal3 = new Animal(new Vector2d(4, 0));
        var animal4 = new Animal(new Vector2d(0, 0));
        assertDoesNotThrow(() -> map.place(animal1));
        assertDoesNotThrow(() -> map.place(animal2));
        assertDoesNotThrow(() -> map.place(animal3));
        assertThrows(IncorrectPositionException.class, () -> map.place(animal4));
        map.move(animal3, MoveDirection.RIGHT);
        map.move(animal4, MoveDirection.LEFT);
        map.move(animal1, MoveDirection.BACKWARD);
        map.move(animal2, MoveDirection.FORWARD);
        map.move(animal3, MoveDirection.FORWARD);
        map.move(animal4, MoveDirection.FORWARD);
        assertEquals(new Vector2d(0, 0), animal1.getPosition());
        assertEquals(new Vector2d(0, 4), animal2.getPosition());
        assertEquals(new Vector2d(4, 0), animal3.getPosition());
        assertEquals(new Vector2d(0, 0), animal4.getPosition());
    }
}