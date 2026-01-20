package agh.ics.oop.model;

import java.util.Set;

public record Stats(
    int animalCount,
    int grassCount,
    int freeFieldCount,
    Set<Genome> mostPopularGenomes,
    double avgEnergy,
    double avgAge,
    double avgChildCount
) {

}
