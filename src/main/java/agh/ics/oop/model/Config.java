package agh.ics.oop.model;

public record Config(
        // world
        int width,
        int height,
        float jungleWorldSizePercentage,
        float jungleGrassGrowthChance,
        boolean firesEnabled,

        // plants
        int startingGrassCount,
        int newGrassesPerDay,
        int grassEnergyValue,

        // animals
        int startingEnergy,
        int energyLossPerDay,
        int breedingEnergyNeeded,
        int breedingEnergyUsed,
        int minMutationCount,
        int maxMutationCount,
        int geneCount
) {
    public final static Config DEFAULT =  new Config(
            10,
            10,
            0.2f,
            0.8f,
            false,

            10,
            1,
            4,
            5,
            1,
            10,
            5,
            0,
            4,
            7
    );

}
