package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
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
        this.mutate();
    }

    public void mutate() {
        int n = genes.size();
        int mutationsCount = rand.nextInt(n);
        List<Integer> possibilities = new ArrayList<>();
        for (int i = 0; i < n; i++) {possibilities.add(i);}

        List<Integer> gengesToMutate = new ArrayList<>();
        for (int i = 0; i < mutationsCount; i++) {
            int geneToSwap = possibilities.get(rand.nextInt(n-i));
            Collections.swap(possibilities, geneToSwap, n-i-1);
            gengesToMutate.add(geneToSwap);
        }
        for (int geneId:  gengesToMutate) {
            genes.set(geneId, rand.nextInt(n));
        }
    }

    public int getActiveGene() {
        return this.genes.get(this.activeGeneIndex);
    }

    public void advanceGene() {
        this.activeGeneIndex = (this.activeGeneIndex + 1) % this.genes.size();
    }

    @Override
    public int hashCode() {
        return this.genes.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder genomeStr = new StringBuilder();
        for (int gene : this.genes) {
            genomeStr.append(gene);
        }
        return genomeStr.toString();
    }
}

