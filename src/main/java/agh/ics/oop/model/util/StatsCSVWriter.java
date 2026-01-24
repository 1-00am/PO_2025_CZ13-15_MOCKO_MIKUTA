package agh.ics.oop.model.util;

import agh.ics.oop.model.Genome;
import agh.ics.oop.model.Stats;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class StatsCSVWriter {
    private final FileWriter fileWriter;

    public StatsCSVWriter(Path path) throws IOException {
        this.fileWriter = new FileWriter(path.toFile());
        this.fileWriter.write("day,animalCount,grassCount,freeFieldCount, mostPopularGenomes,avgEnergy,avgAge,avgChildCount");
    }

    private static String joinColumns(String... columns) {
        return String.join(",", columns);
    }

    public static String formatList(String[] list) {
        return String.join(";", list);
    }

    public void writeStats(Stats stats) throws IOException {
        String columns = StatsCSVWriter.joinColumns(
                Integer.toString(stats.day()),
                Integer.toString(stats.animalCount()),
                Integer.toString(stats.grassCount()),
                Integer.toString(stats.freeFieldCount()),
                StatsCSVWriter.formatList(stats.mostPopularGenomes().stream().map(Genome::toString).toArray(String[]::new)),
                "%.1f".formatted(stats.avgEnergy()),
                "%.1f".formatted(stats.avgAge()),
                "%.1f".formatted(stats.avgChildCount())
        );
        this.fileWriter.write("\n%s".formatted(columns));
    }

    public void flush() throws IOException {
        this.fileWriter.flush();
    }
}
