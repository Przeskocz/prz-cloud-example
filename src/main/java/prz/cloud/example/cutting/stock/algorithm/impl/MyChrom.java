package prz.cloud.example.cutting.stock.algorithm.impl;

import com.softtechdesign.ga.ChromString;

public class MyChrom implements Comparable<MyChrom> {
    private double fitness;
    private ChromString chromString;

    public MyChrom(ChromString chromString) {
        this.chromString = new ChromString(chromString.getGenes().length);
        for (int i = 0; i < chromString.getGenes().length; i++) {
            this.chromString.setGene(chromString.getGenes()[i], i);
        }
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public String[] getGenes() {
        return this.chromString.getGenes();
    }

    public void setGenes(String[] genes) {
        this.chromString = new ChromString(genes.length);
        for (int i = 0; i < genes.length; i++) {
            this.chromString.setGene(genes[i], i);
        }
    }

    public String getGenesAsString() {
        StringBuilder res = new StringBuilder();
        if (getGenes().length > 0) {
            res.append(getGenes()[0]);
            for (int i = 1; i < getGenes().length; i++) {
                res.append("|").append(getGenes()[i]);
            }
        }
        return res.toString();
    }

    @Override
    public String toString() {
        return this.getGenesAsString() + ", fitness = " + this.fitness;
    }


    @Override
    public int compareTo(MyChrom o) {
        String[] otherGenes = o.getGenes();
        for (int i = 0; i < this.getGenes().length; i++) {
            int compare = this.getGenes()[i].compareTo(otherGenes[i]);
            if (compare != 0) {
                return compare;
            }
        }
        if (otherGenes.length == this.getGenes().length)
            return 0;
        else
            return otherGenes.length - this.getGenes().length;
    }
}