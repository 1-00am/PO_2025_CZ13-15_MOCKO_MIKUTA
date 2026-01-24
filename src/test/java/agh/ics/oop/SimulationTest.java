package agh.ics.oop;

import agh.ics.oop.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void stepAdvancesSimulationAndUpdatesStats() {
        Config config = Config.DEFAULT;
        DarwinWorldMap map = new DarwinWorldMap(config);
        Simulation simulation = new Simulation(map, config);

        simulation.step();

        Stats stats = map.getStats();
        assertNotNull(stats);
        assertEquals(1, stats.day());
    }

    @Test
    void pauseAndResumeWorkCorrectly() {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);
        Simulation simulation = new Simulation(map, Config.DEFAULT);

        assertFalse(simulation.isPaused());

        simulation.pause();
        assertTrue(simulation.isPaused());

        simulation.resume();
        assertFalse(simulation.isPaused());
    }

    @Test
    void exitStopsSimulationThread() throws InterruptedException {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);
        Simulation simulation = new Simulation(map, Config.DEFAULT);

        Thread thread = new Thread(simulation);
        thread.start();

        simulation.exit();
        thread.join(500);

        assertFalse(thread.isAlive());
    }

    @Test
    void setSleepDurationDoesNotThrow() {
        DarwinWorldMap map = new DarwinWorldMap(Config.DEFAULT);
        Simulation simulation = new Simulation(map, Config.DEFAULT);

        assertDoesNotThrow(() -> simulation.setSleepDuration(10));
    }

    @Test
    void simulationWorksWithFiresDisabled() {
        Config config = Config.DEFAULT;
        DarwinWorldMap map = new DarwinWorldMap(config);
        Simulation simulation = new Simulation(map, config);

        assertDoesNotThrow(simulation::step);
    }
}
