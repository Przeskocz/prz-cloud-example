package com.softtechdesign.ga;

public class ChromFloat extends Chromosome {
    protected double[] genes;

    public ChromFloat(int var1) {
        this.genes = new double[var1];
    }

    public double[] getGenes() {
        return this.genes;
    }

    public String toString() {
        return this.getGenesAsString();
    }

    public int getNumGenesInCommon(Chromosome var1) {
        return this.genes.length;
    }

    public String getGenesAsString() {
        String var1 = "";

        for(int var2 = 0; var2 < this.genes.length; ++var2) {
            var1 = var1 + " " + this.genes[var2] + ",";
        }

        return var1;
    }

    public void copyChromGenes(Chromosome var1) {
        ChromFloat var2 = (ChromFloat)var1;

        for(int var3 = 0; var3 < this.genes.length; ++var3) {
            this.genes[var3] = var2.genes[var3];
        }

    }

    public double getGene(int var1) {
        return this.genes[var1];
    }

    public void setGene(int var1, double var2) {
        this.genes[var1] = var2;
    }

    public String getGenesAsStr() {
        String var1 = "";

        for(int var2 = 0; var2 < this.genes.length; ++var2) {
            var1 = var1 + this.genes[var2] + ",";
        }

        return var1;
    }
}
