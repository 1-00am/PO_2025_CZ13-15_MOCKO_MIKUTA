package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;

public record Config(
        // world
        int width,
        int height,
        float jungleWorldSizePercentage,
        float jungleGrassGrowthChance,
        boolean firesEnabled,
        int burnTime,
        int burningDailyPenalty,
        float fireStartChance,


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
        int geneCount,
        int startingAnimalCount,

        // misc
        boolean writeToCSV
) {
    public final static Config DEFAULT = new Config(
            5,
            5,
            0.2f,
            0.8f,
            false,
            2,
            1,
            0.1f,

            10,
            1,
            4,
            10,
            1,
            10,
            5,
            0,
            4,
            7,
            3,
            false
    );

    @Override
    public String toString() {
        return "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s".formatted(
            this.width,
            this.height,
            this.jungleWorldSizePercentage,
            this.jungleGrassGrowthChance,
            this.firesEnabled,
            this.burnTime,
            this.burningDailyPenalty,
            this.fireStartChance,
            this.startingGrassCount,
            this.newGrassesPerDay,
            this.grassEnergyValue,
            this.startingEnergy,
            this.energyLossPerDay,
            this.breedingEnergyNeeded,
            this.breedingEnergyUsed,
            this.minMutationCount,
            this.maxMutationCount,
            this.geneCount,
            this.startingAnimalCount,
            this.writeToCSV
        );
    }

    public static Config fromString(String string) throws NumberFormatException, IllegalArgumentException {
        String[] values = string.split(",");
        if (values.length != 20) {
            throw new IllegalArgumentException("Incorrect config format");
        }

        return new Config(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[1]),
                Float.parseFloat(values[2]),
                Float.parseFloat(values[3]),
                Boolean.parseBoolean(values[4]),
                Integer.parseInt(values[5]),
                Integer.parseInt(values[6]),
                Float.parseFloat(values[7]),
                Integer.parseInt(values[8]),
                Integer.parseInt(values[9]),
                Integer.parseInt(values[10]),

                Integer.parseInt(values[11]),
                Integer.parseInt(values[12]),
                Integer.parseInt(values[13]),
                Integer.parseInt(values[14]),
                Integer.parseInt(values[15]),
                Integer.parseInt(values[16]),
                Integer.parseInt(values[17]),
                Integer.parseInt(values[18]),
                Boolean.parseBoolean(values[19])
        );
    }
}
