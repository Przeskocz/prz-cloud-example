package com.softtechdesign.ga;

public abstract class GAStringsSeq extends GA {
    protected String[] possGeneValues;
    protected int numPossGeneValues;

    public GAStringsSeq(int var1, int var2, double var3, int var5, int var6, int var7, int var8, double var9, int var11, String[] var12, int var13, boolean var14) throws GAException {
        super(var1, var2, var3, var5, var6, var7, var8, var9, var13, var14);
        if (var12.length < 2) {
            throw new GAException("There must be at least 2 possible gene values");
        } else {
            this.possGeneValues = var12;
            this.numPossGeneValues = var12.length;

            for(int var15 = 0; var15 < var2; ++var15) {
                super.chromosomes[var15] = new ChromString(var1);
                super.chromNextGen[var15] = new ChromString(var1);
                super.prelimChrom[var15] = new ChromString(var1);
            }

            this.initPopulation();
        }
    }

    protected ChromString getChromosome(int var1) {
        return (ChromString)super.chromosomes[var1];
    }

    protected String getRandomGeneFromPossGenes() {
        int var1 = this.getRandom(this.numPossGeneValues);
        return this.possGeneValues[var1];
    }

    protected void doRandomMutation(int var1) {
        int var2 = this.getRandom(super.chromosomeDim);
        String var4 = this.getRandomGeneFromPossGenes();
        this.setGeneValue(var1, var2, var4);
    }

    private void setGeneValue(int var1, int var2, String var3) {
        ChromString var4 = (ChromString)super.chromosomes[var1];
        var4.setGene(var3, var2);
    }

    protected void initPopulation() {
        for(int var1 = 0; var1 < super.populationDim; ++var1) {
            for(int var2 = 0; var2 < super.chromosomeDim; ++var2) {
                ((ChromString)super.chromosomes[var1]).setGene(this.getRandomGeneFromPossGenes(), var2);
            }

            super.chromosomes[var1].fitness = this.getFitness(var1);
        }

    }

    protected void doOnePtCrossover(Chromosome var1, Chromosome var2) {
        int var3 = this.getRandom(super.chromosomeDim - 2);
        String var4 = ((ChromString)var1).getGene(var3);
        String var5 = ((ChromString)var2).getGene(var3);
        ((ChromString)var1).setGene(var5, var3);
        ((ChromString)var2).setGene(var4, var3);
    }

    protected void doTwoPtCrossover(Chromosome var1, Chromosome var2) {
        int var5 = 1 + this.getRandom(super.chromosomeDim - 2);
        int var6 = var5 + 1 + this.getRandom(super.chromosomeDim - var5 - 1);
        if (var6 == var5 + 1) {
            this.doOnePtCrossover(var1, var2);
        } else {
            String var9 = ((ChromString)var1).getGene(var5);
            String var10 = ((ChromString)var2).getGene(var5);
            String var11 = ((ChromString)var1).getGene(var6);
            String var12 = ((ChromString)var2).getGene(var6);
            ((ChromString)var1).setGene(var10, var5);
            ((ChromString)var1).setGene(var12, var6);
            ((ChromString)var2).setGene(var9, var5);
            ((ChromString)var2).setGene(var11, var6);
        }

    }

    protected void doUniformCrossover(Chromosome var1, Chromosome var2) {
        for(int var5 = 0; var5 < super.chromosomeDim; ++var5) {
            if (this.getRandom(100) > 50) {
                int var3 = this.getRandom(super.chromosomeDim);
                String var6 = ((ChromString)var1).getGene(var3);
                String var7 = ((ChromString)var2).getGene(var3);
                ((ChromString)var1).setGene(var7, var3);
                ((ChromString)var2).setGene(var6, var3);
            }
        }

    }
}
