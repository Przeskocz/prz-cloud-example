package com.softtechdesign.ga;

public abstract class Chromosome {
    protected double fitness;
    protected int fitnessRank;

    public Chromosome() {
    }

    abstract String getGenesAsStr();

    abstract void copyChromGenes(Chromosome var1);

    abstract int getNumGenesInCommon(Chromosome var1);
}

