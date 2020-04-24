package com.softtechdesign.ga;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class GAFloat extends GA {
    protected int decPtsPrecision;
    protected boolean positiveNumOnly;

    protected ChromFloat getChromosome(int var1) {
        return (ChromFloat)super.chromosomes[var1];
    }

    protected void doRandomMutation(int var1) {
        int var2 = this.getRandom(super.chromosomeDim);
        double var3 = ((ChromFloat)super.chromosomes[var1]).genes[var2];
        if (this.getRandom(100) > 50) {
            var3 += var3 * this.getRandom(1000.0D) / 1000.0D;
        } else {
            var3 -= var3 * this.getRandom(1000.0D) / 1000.0D;
        }

        ((ChromFloat)super.chromosomes[var1]).genes[var2] = var3;
    }

    protected void initPopulation() {
        for(int var1 = 0; var1 < super.populationDim; ++var1) {
            for(int var2 = 0; var2 < super.chromosomeDim; ++var2) {
                if (!this.positiveNumOnly && this.getRandom(100) <= 50) {
                    ((ChromFloat)super.chromosomes[var1]).genes[var2] = -this.getRandom(1000.0D) / (1.0D + this.getRandom(1000.0D));
                } else {
                    ((ChromFloat)super.chromosomes[var1]).genes[var2] = this.getRandom(1000.0D) / (1.0D + this.getRandom(1000.0D));
                }
            }

            super.chromosomes[var1].fitness = this.getFitness(var1);
        }

    }

    protected void doOnePtCrossover(Chromosome var1, Chromosome var2) {
        for(int var12 = 0; var12 < super.chromosomeDim; ++var12) {
            NumberFormat var13 = NumberFormat.getNumberInstance();
            var13.setMaximumFractionDigits(this.decPtsPrecision);
            double var4 = ((ChromFloat)var1).genes[var12];
            String var8 = var13.format(var4);
            double var6 = ((ChromFloat)var2).genes[var12];
            String var9 = var13.format(var6);

            int var3;
            do {
                var3 = this.getRandom(var8.length());
            } while(var8.charAt(var3) == '.' || var8.charAt(var3) == '.');

            String var10 = var8.substring(0, var3) + var9.substring(var3 + 1, var8.length());
            String var11 = var9.substring(0, var3) + var8.substring(var3 + 1, var8.length());
            var4 = Double.parseDouble(var10);
            var6 = Double.parseDouble(var11);
            ((ChromFloat)var1).genes[var12] = var4;
            ((ChromFloat)var2).genes[var12] = var6;
        }

    }

    protected void doTwoPtCrossover(Chromosome var1, Chromosome var2) {
        String var13 = "000000000";
        if (this.decPtsPrecision > 0) {
            var13 = var13 + ".";
        }

        for(int var14 = 0; var14 < this.decPtsPrecision; ++var14) {
            var13 = var13 + "0";
        }

        for(int var15 = 0; var15 < super.chromosomeDim; ++var15) {
            NumberFormat var16 = NumberFormat.getInstance();
            ((DecimalFormat)var16).applyPattern("00000000.00");
            double var5 = ((ChromFloat)var1).genes[var15];
            String var9 = var16.format(var5);
            if (var9.charAt(0) != '-') {
                var9 = "+" + var9;
            }

            double var7 = ((ChromFloat)var2).genes[var15];
            String var10 = var16.format(var7);
            if (var10.charAt(0) != '-') {
                var10 = "+" + var10;
            }

            int var3;
            do {
                var3 = 1 + this.getRandom(var9.length() - 2);
            } while(var9.charAt(var3) == '.' || var9.charAt(var3) == '+' || var9.charAt(var3) == '-');

            int var4;
            do {
                do {
                    var4 = 1 + var3 + this.getRandom(var9.length() - var3 - 2);
                } while(var9.charAt(var3) == '.');
            } while(var9.charAt(var3) == '+' || var9.charAt(var3) == '-');

            String var11 = var9.substring(0, var3) + var10.substring(var3, var4) + var9.substring(var4, var9.length());
            String var12 = var10.substring(0, var3) + var9.substring(var3, var4) + var10.substring(var4, var10.length());
            var5 = Double.parseDouble(var11);
            var7 = Double.parseDouble(var12);
            ((ChromFloat)var1).genes[var15] = var5;
            ((ChromFloat)var2).genes[var15] = var7;
        }

    }

    protected void doUniformCrossover(Chromosome var1, Chromosome var2) {
        String var9 = "";
        String var10 = "";

        for(int var11 = 0; var11 < super.chromosomeDim; ++var11) {
            NumberFormat var12 = NumberFormat.getNumberInstance();
            var12.setMaximumFractionDigits(this.decPtsPrecision);
            double var3 = ((ChromFloat)var1).genes[var11];
            String var7 = var12.format(var3);
            double var5 = ((ChromFloat)var2).genes[var11];
            String var8 = var12.format(var5);

            while(var7.length() < var8.length()) {
                if (var7.charAt(0) != '-') {
                    var7 = "0" + var7;
                } else {
                    var7 = "-0" + var7.substring(1, super.chromosomeDim);
                }
            }

            while(var8.length() < var7.length()) {
                if (var8.charAt(0) != '-') {
                    var8 = "0" + var8;
                } else {
                    var8 = "-0" + var8.substring(1, super.chromosomeDim);
                }
            }

            if (this.getRandom(100) > 50) {
                var9 = var9 + var8;
                var10 = var10 + var7;
            } else {
                var9 = var9 + var7;
                var10 = var10 + var8;
            }
        }

    }

    public GAFloat(int var1, int var2, double var3, int var5, int var6, int var7, int var8, double var9, int var11, int var12, boolean var13, boolean var14) throws GAException {
        super(var1, var2, var3, var5, var6, var7, var8, var9, var11, var14);
        if (var12 < 0) {
            throw new GAException("decPtsPrecision must be zero (for integers) or greater. It cannot be negative.");
        } else if (var1 < 1) {
            throw new GAException("chromosomeDim must be greater than zero.");
        } else {
            for(int var15 = 0; var15 < var2; ++var15) {
                super.chromosomes[var15] = new ChromFloat(var1);
                super.chromNextGen[var15] = new ChromFloat(var1);
                super.prelimChrom[var15] = new ChromFloat(var1);
            }

            this.decPtsPrecision = var12;
            this.positiveNumOnly = var13;
            this.initPopulation();
        }
    }
}
