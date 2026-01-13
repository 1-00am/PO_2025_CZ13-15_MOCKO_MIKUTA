package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DarwinWorldMapTest {
    @Test
    void objectAt() {
        var map = new DarwinWorldMap(Config.DEFAULT);
        var animal = new Animal(new Vector2d(0, 0), Config.DEFAULT, 0);
        try {
            map.place(animal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(animal, map.objectAt(animal.getPosition()));
    }
}