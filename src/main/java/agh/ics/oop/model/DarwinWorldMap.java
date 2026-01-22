package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class DarwinWorldMap {
    private final HashMap<Vector2d, List<Animal>> animalGrid = new HashMap<>();
    private final List<Animal> animalList = new ArrayList<>();
    private final List<Animal> deadAnimalList = new ArrayList<>();

    private final List<Vector2d> jungleFreeGrassFields = new ArrayList<>();
    private final List<Vector2d> steppeFreeGrassFields = new ArrayList<>();
    private final HashMap<Vector2d, Grass> grasses = new HashMap<>();
    private List<Pair<Grass, Integer>> buringGrasses = new ArrayList<>();

    private final Random rand = new Random();
    private final Boundary boundary;

    private final List<MapChangeListener> observers = new ArrayList<>();

    private final Config config;

    private int day = 0;

    public DarwinWorldMap(Config worldConfig) {
        this.config = worldConfig;
        int width = worldConfig.width();
        int height = worldConfig.height();
        this.boundary = new Boundary(Vector2d.ZERO, new Vector2d(width - 1, height - 1));

        int jungleHeight = (int)((float)height * worldConfig.jungleWorldSizePercentage());
        int jungleStartY = (int)((float)height / 2.0 - (float)jungleHeight / 2.0);
        int jungleEndY = jungleStartY + jungleHeight - 1;

        // init free fields
        for (int x = 0; x < width; x++) {
            for (int y = jungleStartY; y <= jungleEndY; y++) {
                this.jungleFreeGrassFields.add(new Vector2d(x, y));
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < jungleStartY; y++) {
                this.steppeFreeGrassFields.add(new Vector2d(x, y));
            }
            for (int y = jungleEndY + 1; y < height; y++) {
                this.steppeFreeGrassFields.add(new Vector2d(x, y));
            }
        }

        for (int i = 0; i < worldConfig.startingGrassCount(); i++) {
            this.spawnGrass();
        }
    }

    public Boundary getCurrentBounds() {
        return this.boundary;
    }

    public boolean isOccupied(Vector2d position) {
        var animals = this.animalGrid.get(position);
        if (animals == null) {
            return this.grasses.containsKey(position);
        } else {
            return !animals.isEmpty() || this.grasses.containsKey(position);
        }
    }

    public WorldElement objectAt(Vector2d position) {
        var cell = this.animalGrid.get(position);
        if (cell == null) {
            return this.grasses.get(position);
        }
        try {
            return cell.getFirst();
        } catch (NoSuchElementException e) {
            return this.grasses.get(position);
        }
    }

    public Collection<WorldElement> getElements() {
        var elements = new ArrayList<WorldElement>(this.animalList);
        elements.addAll(this.grasses.values());
        return elements;
    }

    public void spawnGrass() {
        // randomly select area (jungle|steppe)
        List<Vector2d> freeFields = this.rand.nextFloat() < this.config.jungleGrassGrowthChance() ? this.jungleFreeGrassFields : this.steppeFreeGrassFields;

        if (freeFields.isEmpty()) {
            if (!this.jungleFreeGrassFields.isEmpty()) {
                freeFields = this.jungleFreeGrassFields;
            } else if (!this.steppeFreeGrassFields.isEmpty()) {
                freeFields = this.steppeFreeGrassFields;
            } else {
                return;
            }
        }

        int index = this.rand.nextInt(freeFields.size());

        // swap remove
        Collections.swap(freeFields, index, freeFields.size() - 1);
        var position = freeFields.removeLast();

        this.grasses.put(position, new Grass(position, freeFields == this.jungleFreeGrassFields));
    }

    public void place(Animal animal) throws IncorrectPositionException {
        if (!(animal.getPosition().follows(this.boundary.lowerLeft()) && animal.getPosition().precedes(this.boundary.upperRight()))) {
            throw new IncorrectPositionException(animal.getPosition());
        }
        if (!this.animalGrid.containsKey(animal.getPosition())) {
            this.animalGrid.put(animal.getPosition(), new ArrayList<>());
        }
        var cell = this.animalGrid.get(animal.getPosition());
        cell.add(animal);
        this.animalList.add(animal);
    }

    public void removeAnimal(Animal animal) {
        var cell = this.animalGrid.get(animal.getPosition());
        if (cell == null) {
            return;
        }
        cell.remove(animal);
        animal.setDeathDate(this.day);
        this.animalList.remove(animal);
        this.deadAnimalList.add(animal);
    }

    public void recreateGrid() {
        for (var animal : this.animalList) {
            if (!this.animalGrid.containsKey(animal.getPosition())) {
                this.animalGrid.put(animal.getPosition(), new ArrayList<>());
            }
            var cell = this.animalGrid.get(animal.getPosition());
            cell.add(animal);
        }
    }

    public void addObserver(MapChangeListener observer) {
        if (this.observers.contains(observer)) {
            return;
        }
        this.observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        this.observers.remove(observer);
    }

    private void mapChanged() {
        for (var observer : this.observers) {
            observer.mapChanged(this);
        }
    }

    private Grass grassAt(Vector2d position) {
        return this.grasses.get(position);
    }

    public void removeGrass(Grass grass) {
        this.grasses.remove(grass.getPosition());
        if (grass.isJungle()) {
            this.jungleFreeGrassFields.add(grass.getPosition());
        } else {
            this.steppeFreeGrassFields.add(grass.getPosition());
        }
    }

    public void clearGrid() {
        for (var cell : this.animalGrid.values()) {
            cell.clear();
        }
    }

    public void removeDeadAnimals() {
        this.animalList.stream().filter(Animal::isDead).forEach(animal -> {
            this.deadAnimalList.add(animal);
            animal.setDeathDate(this.day);
        });
        this.animalList.removeIf(Animal::isDead);
    }

    public void handleMovement() {
        for (var animal : this.animalList) {
            animal.rotate();
            animal.move(this.config);
        }

        this.recreateGrid();
    }

    public void handleEating(boolean fires) {
        for (var entry : this.animalGrid.entrySet()) {
            var grass = this.grassAt(entry.getKey());
            if (grass == null || grass.isBurning()) {
                continue;
            }

            var cell = entry.getValue();

            if (cell.isEmpty()) {
                continue;
            }

            if (fires) {
                if (config.fireStartChance() > rand.nextFloat()) {
                    grass.startFire();
                    this.buringGrasses.add(new Pair<>(grass, config.burnTime()));
                    continue;
                }
            }

            var maxEnergy = cell.stream().mapToInt(Animal::getEnergy).max().orElse(0);
            var chosenAnimals = cell.stream().filter((animal) -> animal.getEnergy() == maxEnergy).toList();

            var oldestBirthDate = chosenAnimals.stream().mapToInt(Animal::getBirthDate).min().orElse(0);
            chosenAnimals = chosenAnimals.stream().filter((animal) -> animal.getBirthDate() == oldestBirthDate).toList();

            var maxChildrenCount = chosenAnimals.stream().mapToInt(Animal::getChildrenCount).max().orElse(0);
            chosenAnimals = chosenAnimals.stream().filter((animal) -> animal.getChildrenCount() == maxChildrenCount).toList();

            var index = this.rand.nextInt(chosenAnimals.size());
            var chosenAnimal = chosenAnimals.get(index);
            this.removeGrass(grass);
            chosenAnimal.addEnergy(this.config.grassEnergyValue());
        }
    }

    public void handleReproduction() {
        List<Animal> toPlace =  new ArrayList<>();
        for (var fieldList : this.animalGrid.values()) {
            Animal currAnimal = null;
            for (var animal :  fieldList) {
                if (animal.getEnergy() < config.breedingEnergyNeeded()) {continue;}
                if (currAnimal != null) {
                    Animal newAnimal = currAnimal.reproduce(animal, config.breedingEnergyUsed(), this.day);
                    toPlace.add(newAnimal);
                    animal.subtractEnergy(config.breedingEnergyUsed());
                    currAnimal.subtractEnergy(config.breedingEnergyUsed());
                    currAnimal = null;
                } else {
                    currAnimal = animal;
                }
            }
        }

        for (var animal : toPlace) {
            try {
                this.place(animal);
            } catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }
    };

    public void spawnGrasses() {
        for (int i = 0; i < this.config.newGrassesPerDay(); i++) {
            this.spawnGrass();
        }
    }

    public void advanceDay() {
        for (var animal : this.animalList) {
            animal.advanceDay(this.config);
        }
        this.mapChanged();
        this.day++;
        for (Animal animal : this.animalList) {
            IO.println();
        }
        //IO.println("day: %s,  fires: %s".formatted(this.day, this.buringGrasses));
    }

    public void handleFiresGrass() {
        List<Pair<Grass, Integer>> newBurningGrasses = new ArrayList<>();
        for (Pair<Grass, Integer> pair: buringGrasses) {
            Grass grass = pair.getKey();
            Integer daysLeft = pair.getValue();

            // rozprzestrzenianie
            Vector2d pos = grass.getPosition();
            List<Vector2d> neighbours = List.of(
                    pos.add(MapDirection.NORTH.toUnitVector()),
                    pos.add(MapDirection.EAST.toUnitVector()),
                    pos.add(MapDirection.SOUTH.toUnitVector()),
                    pos.add(MapDirection.WEST.toUnitVector())
            );
            for (Vector2d neighbour : neighbours) {
                Grass neiGrass = grasses.get(neighbour);
                if (neiGrass != null && !neiGrass.isBurning()) {
                    neiGrass.startFire();
                    newBurningGrasses.add(new Pair<>(neiGrass, config.burnTime()));
                }
            }

            // wypalanie
            if (daysLeft > 1) {
                Pair<Grass, Integer> newPair = new Pair<>(grass, daysLeft-1);
                newBurningGrasses.add(newPair);
            } else {
                removeGrass(grass);
            }
        }
        this.buringGrasses = newBurningGrasses;
    }

    public void handleFiresAnimals() {
        for (Pair<Grass, Integer> pair: buringGrasses) {
            Vector2d pos = pair.getKey().getPosition();
            List<Animal> animals = animalGrid.get(pos);
            if (animals == null) { continue; }
            for (Animal animal : animals) {
                animal.startBurning(config.burnTime());
            }
        }
        for (Animal animal:  this.animalList) {
            animal.burn();
        }
    }

    public Stats createStats() {
        int animalCount = this.animalList.size();
        int grassCount = this.grasses.size();

        HashSet<Vector2d> takenFields = new HashSet<>(this.grasses.keySet());
        takenFields.addAll(this.animalList.stream().map(Animal::getPosition).toList());
        int freeFields = this.config.width() * this.config.height() - takenFields.size();

        double avgEnergy = 0.0f;
        double avgChildCount = 0.0f;
        HashMap<Genome, Integer> genomeOccurrences = new HashMap<>();
        for (Animal animal : this.animalList) {
            avgEnergy += animal.getEnergy();
            avgChildCount += animal.getChildrenCount();
            if (!genomeOccurrences.containsKey(animal.getGenome())) {
                genomeOccurrences.put(animal.getGenome(), 0);
            }
            genomeOccurrences.compute(animal.getGenome(), (k, count) -> count + 1);
        }
        int mostPopularGenomeOccurrences = genomeOccurrences.values().stream().max(Integer::compareTo).orElse(0);
        Set<Genome> mostPopularGenomes = genomeOccurrences.entrySet().stream().filter(e -> e.getValue() == mostPopularGenomeOccurrences).map(Map.Entry::getKey).collect(Collectors.toSet());
        avgEnergy = avgEnergy / animalCount;
        avgChildCount = avgChildCount / animalCount;

        double avgAge = 0.0f;
        for (Animal animal : this.deadAnimalList) {
            avgAge += animal.getDeathDate() - animal.getBirthDate();
        }
        avgAge = avgAge / animalCount;

        return new Stats(animalCount, grassCount, freeFields, mostPopularGenomes, avgEnergy, avgChildCount, avgAge);
    }
}
