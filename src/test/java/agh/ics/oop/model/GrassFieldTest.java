package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {
    @Test
    void placeCorrect() {
        var map = new GrassField(10);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(0, 0))));
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(4, 0))));
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(4, 4))));
    }

    @Test
    void placeOccupied() {
        var map = new GrassField(10);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(0, 0))));
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(3, 1))));
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(new Vector2d(0, 0))));
        assertThrows(IncorrectPositionException.class, () -> map.place(new Animal(new Vector2d(3, 1))));
    }

    @Test
    void isOccupied() {
        var map = new GrassField(10);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(0, 0))));
        assertTrue(map.isOccupied(new Vector2d(0, 0)));
    }

    @Test
    void canMoveToCorrect() {
        var map = new GrassField(10);
        map.canMoveTo(new Vector2d(0, 0));
        map.canMoveTo(new Vector2d(4, 4));
    }

    @Test
    void canMoveToOccupied() {
        var map = new GrassField(10);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(1, 1))));
        assertFalse(map.canMoveTo(new Vector2d(1, 1)));
    }

    @Test
    void objectAt() {
        var map = new GrassField(10);
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(1, 1))));
        var animal = map.objectAt(new Vector2d(1, 1));
        assertNotNull(animal);
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        if (!(map.objectAt(new Vector2d(0, 0)) instanceof Grass)) {
            assertNull(map.objectAt(new Vector2d(0, 0)));
        }
    }

    @Test
    void moveCorrect() {
        var map = new GrassField(10);
        var animal = new Animal(new Vector2d(1, 1));
        assertDoesNotThrow(() -> map.place(animal));
        map.move(animal, MoveDirection.FORWARD);
        assertEquals(new Vector2d(1, 2), animal.getPosition());
        map.move(animal, MoveDirection.RIGHT);
        assertEquals(MapDirection.EAST, animal.getMapDirection());
    }

    @Test
    void moveOccupied() {
        var map = new GrassField(10);
        var animal = new Animal(new Vector2d(1, 1));
        assertDoesNotThrow(() -> map.place(animal));
        assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(1, 2))));
        map.move(animal, MoveDirection.FORWARD);
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        map.move(animal, MoveDirection.RIGHT);
        assertEquals(MapDirection.EAST, animal.getMapDirection());
    }

    @Test
    void grassCountCorrect() {
        var map10 = new GrassField(10);
        var count10 = 0;
        for (WorldElement element : map10.getElements()) {
            if (element instanceof Grass) { // in case more elements are added in the future
                count10 += 1;
            }
        }
        assertEquals(10, count10);

        var map20 = new GrassField(20);
        var count20 = 0;
        for (WorldElement element : map20.getElements()) {
            if (element instanceof Grass) {
                count20 += 1;
            }
        }
        assertEquals(20, count20);
    }
}