package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> activeThreads = new ArrayList<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void runSync() {
        for (Simulation simulation : simulations) {
            simulation.run();
        }
    }

    public void runAsync() {
        for (Simulation simulation : simulations) {
            var thread = new Thread(simulation);
            thread.start();
            this.activeThreads.add(thread);
        }
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for (Thread thread : this.activeThreads) {
            thread.join();
        }
        this.activeThreads.clear();
        
        if (!this.threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
            this.threadPool.shutdownNow();
        }
    }

    public void runAsyncInThreadPool() {
        for (Simulation simulation : simulations) {
            this.threadPool.submit(simulation);
        }
        this.threadPool.shutdown();
    }
}
