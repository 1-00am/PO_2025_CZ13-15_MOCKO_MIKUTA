package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DarwinWorldMapTest {

    @Test
    void mapIsCreatedWithCorrectBounds() {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);

        Boundary bounds = map.getCurrentBounds();

        assertEquals(Vector2d.ZERO, bounds.lowerLeft());
        assertEquals(
                new Vector2d(Config.DEFAULT.width() - 1, Config.DEFAULT.height() - 1),
                bounds.upperRight()
        );
    }

    @Test
    void mapContainsStartingAnimals() {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);

        assertFalse(map.getElements().isEmpty());
    }

    @Test
    void isOccupiedReturnsFalseForEmptyField() {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);

        Vector2d pos = new Vector2d(0, 0);

        boolean foundEmpty = false;
        for (int x = 0; x < Config.DEFAULT.width(); x++) {
            for (int y = 0; y < Config.DEFAULT.height(); y++) {
                Vector2d p = new Vector2d(x, y);
                if (!map.isOccupied(p)) {
                    foundEmpty = true;
                    break;
                }
            }
        }

        assertTrue(foundEmpty);
    }

    @Test
    void placeAnimalAddsItToMap() throws Exception {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);

        Vector2d free = null;
        for (int x = 0; x < Config.DEFAULT.width(); x++) {
            for (int y = 0; y < Config.DEFAULT.height(); y++) {
                Vector2d p = new Vector2d(x, y);
                if (!map.isOccupied(p)) {
                    free = p;
                    break;
                }
            }
        }

        assertNotNull(free);

        Animal animal = new Animal(free, Config.DEFAULT, 0);
        map.place(animal);

        assertTrue(map.isOccupied(free));
        assertEquals(animal, map.objectAt(free));
    }

    @Test
    void removeDeadAnimalsRemovesOnlyDeadOnes() {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);

        Animal animal = map.getElements().stream()
                .filter(e -> e instanceof Animal)
                .map(e -> (Animal) e)
                .findFirst()
                .orElseThrow();

        animal.subtractEnergy(10_000);
        map.removeDeadAnimals();

        assertFalse(map.getElements().contains(animal));
    }

    @Test
    void advanceDayIncreasesDayAndUpdatesStats() {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);

        map.advanceDay();

        assertNotNull(map.getStats());
        assertEquals(1, map.getStats().day());
    }
}
