package com.softtechdesign.ga;

public abstract class GAString extends GA {
    protected int chromDecPts;
    protected String possGeneValues;

    protected ChromChars getChromosome(int var1) {
        return (ChromChars)super.chromosomes[var1];
    }

    public GAString(int var1, int var2, double var3, int var5, int var6, int var7, int var8, double var9, int var11, String var12, int var13, boolean var14) throws GAException {
        super(var1, var2, var3, var5, var6, var7, var8, var9, var13, var14);
        if (var12.length() < 2) {
            throw new GAException("There must be at least 2 possible gene values");
        } else {
            this.chromDecPts = var11;
            this.possGeneValues = var12;

            for(int var15 = 0; var15 < var2; ++var15) {
                super.chromosomes[var15] = new ChromChars(var1);
                super.chromNextGen[var15] = new ChromChars(var1);
                super.prelimChrom[var15] = new ChromChars(var1);
            }

            this.initPopulation();
        }
    }

    protected double chromStrToFloat(String var1, int var2) {
        if (var2 == 0) {
            return (double)this.binaryStrToInt(var1);
        } else {
            int var4 = var1.length() - var2;
            String var3 = var1.substring(0, var4) + "." + var1.substring(var4, var4 + var2);
            return Double.parseDouble(var3);
        }
    }

    protected double getChromValAsDouble(String var1) {
        return this.chromStrToFloat(var1, this.chromDecPts);
    }

    protected char getRandomGeneFromPossGenes() {
        int var1 = this.getRandom(this.possGeneValues.length());
        return this.possGeneValues.charAt(var1);
    }

    protected void doRandomMutation(int var1) {
        int var2 = this.getRandom(super.chromosomeDim);
        int var3 = this.getRandom(super.chromosomeDim);
        char var4 = ((ChromChars)super.chromosomes[var1]).genes[var2];
        ((ChromChars)super.chromosomes[var1]).genes[var2] = ((ChromChars)super.chromosomes[var1]).genes[var3];
        ((ChromChars)super.chromosomes[var1]).genes[var3] = var4;
    }

    protected void initPopulation() {
        for(int var1 = 0; var1 < super.populationDim; ++var1) {
            for(int var2 = 0; var2 < super.chromosomeDim; ++var2) {
                ((ChromChars)super.chromosomes[var1]).genes[var2] = this.getRandomGeneFromPossGenes();
            }

            super.chromosomes[var1].fitness = this.getFitness(var1);
        }

    }

    protected void doOnePtCrossover(Chromosome var1, Chromosome var2) {
        int var5 = this.getRandom(super.chromosomeDim - 2);
        String var6 = var1.getGenesAsStr();
        String var7 = var2.getGenesAsStr();
        String var3 = var6.substring(0, var5) + var7.substring(var5, super.chromosomeDim);
        String var4 = var7.substring(0, var5) + var6.substring(var5, super.chromosomeDim);
        ((ChromChars)var1).setGenesFromStr(var3);
        ((ChromChars)var2).setGenesFromStr(var4);
    }

    protected void doTwoPtCrossover(Chromosome var1, Chromosome var2) {
        int var5 = 1 + this.getRandom(super.chromosomeDim - 2);
        int var6 = var5 + 1 + this.getRandom(super.chromosomeDim - var5 - 1);
        if (var6 == var5 + 1) {
            this.doOnePtCrossover(var1, var2);
        } else {
            String var7 = var1.getGenesAsStr();
            String var8 = var2.getGenesAsStr();
            String var3 = var7.substring(0, var5) + var8.substring(var5, var6) + var7.substring(var6, super.chromosomeDim);
            String var4 = var8.substring(0, var5) + var7.substring(var5, var6) + var8.substring(var6, super.chromosomeDim);
            ((ChromChars)var1).setGenesFromStr(var3);
            ((ChromChars)var2).setGenesFromStr(var4);
        }

    }

    protected void doUniformCrossover(Chromosome var1, Chromosome var2) {
        StringBuffer var5 = new StringBuffer(var1.getGenesAsStr());
        StringBuffer var6 = new StringBuffer(var2.getGenesAsStr());

        for(int var7 = 0; var7 < super.chromosomeDim; ++var7) {
            if (this.getRandom(100) > 50) {
                int var3 = this.getRandom(super.chromosomeDim);
                char var4 = var5.charAt(var3);
                var5.setCharAt(var3, var6.charAt(var3));
                var6.setCharAt(var3, var4);
            }
        }

        ((ChromChars)var1).setGenesFromStr(var5.toString());
        ((ChromChars)var2).setGenesFromStr(var6.toString());
    }
}