package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AnimalTest {

    private Config config;

    @BeforeEach
    void setup() {
        config = Config.DEFAULT;
    }

    @Test
    void animalIsCreatedWithCorrectInitialValues() {
        Vector2d position = new Vector2d(3, 4);
        Animal animal = new Animal(position, config, 5);

        assertEquals(position, animal.getPosition());
        assertEquals(config.startingEnergy(), animal.getEnergy());
        assertEquals(5, animal.getBirthDate());
        assertNotNull(animal.getGenome());
        assertNotNull(animal.getMapDirection());
    }

    @Test
    void isAtWorksCorrectly() {
        Animal animal = new Animal(new Vector2d(2, 2), config, 0);

        assertTrue(animal.isAt(new Vector2d(2, 2)));
        assertFalse(animal.isAt(new Vector2d(3, 2)));
    }

    @Test
    void advanceDayReducesEnergy() {
        Animal animal = new Animal(new Vector2d(2, 2), config, 0);
        int before = animal.getEnergy();

        animal.advanceDay(config);

        assertEquals(before - config.energyLossPerDay(), animal.getEnergy());
    }

    @Test
    void energyNeverGoesBelowZero() {
        Animal animal = new Animal(new Vector2d(2, 2), config, 0);

        animal.subtractEnergy(10_000);

        assertEquals(0, animal.getEnergy());
        assertTrue(animal.isDead());
    }

    @Test
    void moveKeepsAnimalInsideMapBounds() {
        Animal animal = new Animal(new Vector2d(0, 0), config, 0);

        animal.move(config);

        Vector2d pos = animal.getPosition();
        assertTrue(pos.getX() >= 0 && pos.getX() < config.width());
        assertTrue(pos.getY() >= 0 && pos.getY() < config.height());
    }

    @Test
    void burningReducesEnergyAndStops() {
        Animal animal = new Animal(new Vector2d(2, 2), config, 0);
        animal.startBurning(1);

        int before = animal.getEnergy();
        animal.burn();

        assertEquals(before - config.burningDailyPenalty(), animal.getEnergy());
        assertFalse(animal.isBurning());
    }

    @Test
    void reproduceCreatesChildWithCorrectEnergyAndBirthDate() {
        Animal parent1 = new Animal(new Vector2d(2, 2), config, 0);
        Animal parent2 = new Animal(new Vector2d(2, 2), config, 0);

        int energyUsed = config.breedingEnergyUsed();
        int day = 10;

        Animal child = parent1.reproduce(parent2, energyUsed, day);

        assertNotNull(child);
        assertEquals(2 * energyUsed, child.getEnergy());
        assertEquals(day, child.getBirthDate());
        assertEquals(1, parent1.getChildrenCount());
    }

    @Test
    void childGenomeIsCreatedFromParents() {
        Animal parent1 = new Animal(new Vector2d(2, 2), config, 0);
        Animal parent2 = new Animal(new Vector2d(2, 2), config, 0);

        Animal child = parent1.reproduce(parent2, config.breedingEnergyUsed(), 1);

        assertNotNull(child.getGenome());
    }
}
