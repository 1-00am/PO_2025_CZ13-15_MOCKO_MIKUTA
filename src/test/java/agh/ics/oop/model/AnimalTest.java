package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    @Test
    void construction() {
        Animal animal1 = new Animal();
        assertEquals(new Vector2d(2, 2), animal1.getPosition());
        assertEquals(MapDirection.NORTH, animal1.getMapDirection());

        Animal animal2 = new Animal(new Vector2d(3, 3));
        assertEquals(new Vector2d(3, 3), animal2.getPosition());
        assertEquals(MapDirection.NORTH, animal2.getMapDirection());
    }

    @Test
    void moveForwardBackward() {
        RectangularMap validator = new RectangularMap(4, 4);
        Animal animal = new Animal(new Vector2d(1, 0));
        animal.move(MoveDirection.FORWARD, validator);
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        animal.move(MoveDirection.BACKWARD, validator);
        assertEquals(new Vector2d(1, 0), animal.getPosition());
    }

    @Test
    void turn() {
        RectangularMap validator = new RectangularMap(4, 4);
        Animal animal = new Animal(new Vector2d(1, 0));
        animal.move(MoveDirection.RIGHT, validator);
        assertEquals(MapDirection.EAST, animal.getMapDirection());
        animal.move(MoveDirection.LEFT, validator);
        assertEquals(MapDirection.NORTH, animal.getMapDirection());
    }

    @Test
    void moveAfterTurning() {
        RectangularMap validator = new RectangularMap(4, 4);
        Animal animal = new Animal(new Vector2d(0, 0));
        animal.move(MoveDirection.RIGHT, validator);
        animal.move(MoveDirection.FORWARD, validator);
        assertEquals(new Vector2d(1, 0), animal.getPosition());
        animal.move(MoveDirection.RIGHT, validator);
        animal.move(MoveDirection.BACKWARD, validator);
        assertEquals(new Vector2d(1, 1), animal.getPosition());
    }

    @Test
    void toStringTest() {
        Animal animal = new Animal(new Vector2d(1, 0));
        assertEquals("^", animal.toString());
        assertEquals(new Vector2d(1, 0), animal.getPosition());
    }

    @Test
    void isAt() {
        Animal animal = new Animal(new Vector2d(1, 2));
        assertTrue(animal.isAt(new Vector2d(1, 2)));
    }

    @Test
    void constrainedNorthSouth() {
        RectangularMap validator = new RectangularMap(4, 4);
        Animal animal1 = new Animal(new Vector2d(0, 0));
        animal1.move(MoveDirection.BACKWARD, validator);
        assertEquals(new Vector2d(0, 0), animal1.getPosition());

        Animal animal2 = new Animal(new Vector2d(0, 4));
        animal2.move(MoveDirection.FORWARD, validator);
        assertEquals(new Vector2d(0, 4), animal2.getPosition());
    }

    @Test
    void constrainedEastWest() {
        RectangularMap validator = new RectangularMap(4, 4);
        Animal animal1 = new Animal(new Vector2d(0, 0));
        animal1.move(MoveDirection.LEFT, validator);
        animal1.move(MoveDirection.FORWARD, validator);
        assertEquals(new Vector2d(0, 0), animal1.getPosition());

        Animal animal2 = new Animal(new Vector2d(4, 0));
        animal2.move(MoveDirection.RIGHT, validator);
        animal2.move(MoveDirection.FORWARD, validator);
        assertEquals(new Vector2d(4, 0), animal2.getPosition());
    }
}