package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome {
    private final static Random rand = new Random();
    private final List<Integer> genes;
    private int activeGeneIndex;

    public Genome(int geneCount) {
        this.genes = new ArrayList<>();
        for (int i = 0; i < geneCount; i++) {
            this.genes.add(Genome.rand.nextInt(8));
        }
        this.activeGeneIndex = rand.nextInt(geneCount);
    }

    public Genome(Genome genomeDom, Genome genomeSub, float domGenePercentage) {
        int domGeneCount = Math.round(domGenePercentage * genomeDom.genes.size());
        int genomeLength = genomeDom.genes.size();

        this.genes = new ArrayList<>();
        this.activeGeneIndex = rand.nextInt(genomeLength);

        if (Genome.rand.nextFloat() >= 0.5) {
            this.genes.addAll(genomeDom.genes.subList(0, domGeneCount));
            this.genes.addAll(genomeSub.genes.subList(domGeneCount, genomeLength));
        } else {
            this.genes.addAll(genomeSub.genes.subList(0, genomeLength - domGeneCount));
            this.genes.addAll(genomeDom.genes.subList(genomeLength - domGeneCount, genomeLength));
        }
    }

    public int getActiveGene() {
        return this.genes.get(this.activeGeneIndex);
    }

    public void advanceGene() {
        this.activeGeneIndex = (this.activeGeneIndex + 1) % this.genes.size();
    }
}
