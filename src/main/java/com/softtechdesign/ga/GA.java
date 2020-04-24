package com.softtechdesign.ga;

import java.util.Date;

public abstract class GA implements Runnable {
    double mutationProb;
    int maxGenerations;
    int numPrelimRuns;
    int maxPrelimGenerations;
    int randomSelectionChance;
    double crossoverProb;
    protected int chromosomeDim;
    protected int populationDim;
    Chromosome[] chromosomes;
    Chromosome[] chromNextGen;
    Chromosome[] prelimChrom;
    int bestFitnessChromIndex;
    int worstFitnessChromIndex;
    protected int crossoverType;
    double[] genAvgDeviation;
    double[] genAvgFitness;
    boolean computeStatistics;

    protected abstract void initPopulation();

    protected abstract void doRandomMutation(int var1);

    protected abstract void doOnePtCrossover(Chromosome var1, Chromosome var2);

    protected abstract void doTwoPtCrossover(Chromosome var1, Chromosome var2);

    protected abstract void doUniformCrossover(Chromosome var1, Chromosome var2);

    protected abstract double getFitness(int var1);

    public void run() {
        this.evolve();
    }

    public GA(int var1, int var2, double var3, int var5, int var6, int var7, int var8, double var9, int var11, boolean var12) {
        this.randomSelectionChance = var5;
        this.crossoverType = var11;
        this.chromosomeDim = var1;
        this.populationDim = var2;
        this.computeStatistics = var12;
        this.chromosomes = new Chromosome[var2];
        this.chromNextGen = new Chromosome[var2];
        this.prelimChrom = new Chromosome[var2];
        this.genAvgDeviation = new double[var6];
        this.genAvgFitness = new double[var6];
        this.crossoverProb = var3;
        this.maxGenerations = var6;
        this.numPrelimRuns = var7;
        this.maxPrelimGenerations = var8;
        this.mutationProb = var9;
    }

    public double getAvgDeviation(int var1) {
        return this.genAvgDeviation[var1];
    }

    public double getAvgFitness(int var1) {
        return this.genAvgFitness[var1];
    }

    public double getMutationProb() {
        return this.mutationProb;
    }

    public int getMaxGenerations() {
        return this.maxGenerations;
    }

    public int getNumPrelimRuns() {
        return this.numPrelimRuns;
    }

    public int getMaxPrelimGenerations() {
        return this.maxPrelimGenerations;
    }

    public int getRandomSelectionChance() {
        return this.randomSelectionChance;
    }

    public double getCrossoverProb() {
        return this.crossoverProb;
    }

    public int getChromosomeDim() {
        return this.chromosomeDim;
    }

    public int getPopulationDim() {
        return this.populationDim;
    }

    public int getCrossoverType() {
        return this.crossoverType;
    }

    public boolean getComputeStatistics() {
        return this.computeStatistics;
    }

    public Chromosome getFittestChromosome() {
        return this.chromosomes[this.bestFitnessChromIndex];
    }

    public double getFittestChromosomesFitness() {
        return this.chromosomes[this.bestFitnessChromIndex].fitness;
    }

    int getRandom(int var1) {
        int var2 = (int)(Math.random() * (double)var1);
        return var2;
    }

    double getRandom(double var1) {
        double var3 = Math.random() * var1;
        return var3;
    }

    public int evolve() {
        System.out.println("GA start time: " + (new Date()).toString());
        int var1;
        if (this.numPrelimRuns > 0) {
            int var2 = 0;
            int var3 = this.populationDim / this.numPrelimRuns;

            int var5;
            for(int var4 = 1; var4 <= this.numPrelimRuns; ++var4) {
                var1 = 0;
                this.initPopulation();

                for(; var1 < this.maxPrelimGenerations; ++var1) {
                    System.out.println(var4 + " of " + this.numPrelimRuns + " prelim runs --> " + (var1 + 1) + " of " + this.maxPrelimGenerations + " generations");
                    this.computeFitnessRankings();
                    this.doGeneticMating();
                    this.copyNextGenToThisGen();
                    if (this.computeStatistics) {
                        this.genAvgDeviation[var1] = this.getAvgDeviationAmongChroms();
                        this.genAvgFitness[var1] = this.getAvgFitness();
                    }
                }

                this.computeFitnessRankings();
                var5 = 0;

                for(int var6 = 0; var6 < this.populationDim && var5 < var3; ++var6) {
                    if (this.chromosomes[var6].fitnessRank >= this.populationDim - var3) {
                        this.prelimChrom[var2 + var5].copyChromGenes(this.chromosomes[var6]);
                        ++var5;
                    }
                }

                var2 += var5;
            }

            for(var5 = 0; var5 < var2; ++var5) {
                this.chromosomes[var5].copyChromGenes(this.prelimChrom[var5]);
            }

            System.out.println("INITIAL POPULATION AFTER PRELIM RUNS:");
        } else {
            System.out.println("INITIAL POPULATION (NO PRELIM RUNS):");
        }

        this.addChromosomesToLog(0, 10);

        for(var1 = 0; var1 < this.maxGenerations; ++var1) {
            this.computeFitnessRankings();
            this.doGeneticMating();
            this.copyNextGenToThisGen();
            if (this.computeStatistics) {
                this.genAvgDeviation[var1] = this.getAvgDeviationAmongChroms();
                this.genAvgFitness[var1] = this.getAvgFitness();
            }
        }

        System.out.println("GEN " + (var1 + 1) + " AVG FITNESS = " + this.genAvgFitness[var1 - 1] + " AVG DEV = " + this.genAvgDeviation[var1 - 1]);
        this.addChromosomesToLog(var1, 10);
        this.computeFitnessRankings();
        System.out.println("Best Chromosome Found: ");
        System.out.println(this.chromosomes[this.bestFitnessChromIndex].getGenesAsStr() + " Fitness= " + this.chromosomes[this.bestFitnessChromIndex].fitness);
        System.out.println("GA end time: " + (new Date()).toString());
        return var1;
    }

    public double getAvgFitness() {
        double var1 = 0.0D;

        for(int var3 = 0; var3 < this.populationDim; ++var3) {
            var1 += this.chromosomes[var3].fitness;
        }

        return var1 / (double)this.populationDim;
    }

    public void selectTwoParents(int[] var1) {
        int var2 = var1[0];
        int var3 = var1[1];
        boolean var4 = false;

        int var5;
        while(!var4) {
            var5 = this.getRandom(this.populationDim);
            if (this.randomSelectionChance > this.getRandom(100)) {
                var2 = var5;
                var4 = true;
            } else if (this.chromosomes[var5].fitnessRank + 1 > this.getRandom(this.populationDim)) {
                var2 = var5;
                var4 = true;
            }
        }

        var4 = false;

        while(!var4) {
            var5 = this.getRandom(this.populationDim);
            if (this.randomSelectionChance > this.getRandom(100)) {
                if (var5 != var2) {
                    var3 = var5;
                    var4 = true;
                }
            } else if (var5 != var2 && this.chromosomes[var5].fitnessRank + 1 > this.getRandom(this.populationDim)) {
                var3 = var5;
                var4 = true;
            }
        }

        var1[0] = var2;
        var1[1] = var3;
    }

    int getFitnessRank(double var1) {
        int var3 = -1;

        for(int var4 = 0; var4 < this.populationDim; ++var4) {
            if (var1 >= this.chromosomes[var4].fitness) {
                ++var3;
            }
        }

        return var3;
    }

    void computeFitnessRankings() {
        for(int var3 = 0; var3 < this.populationDim; ++var3) {
            this.chromosomes[var3].fitness = this.getFitness(var3);
        }

        for(int var4 = 0; var4 < this.populationDim; ++var4) {
            this.chromosomes[var4].fitnessRank = this.getFitnessRank(this.chromosomes[var4].fitness);
        }

        for(int var9 = 0; var9 < this.populationDim; ++var9) {
            if (this.chromosomes[var9].fitnessRank == this.populationDim - 1) {
                double var5 = this.chromosomes[var9].fitness;
                this.bestFitnessChromIndex = var9;
            }

            if (this.chromosomes[var9].fitnessRank == 0) {
                double var7 = this.chromosomes[var9].fitness;
                this.worstFitnessChromIndex = var9;
            }
        }

    }

    void doGeneticMating() {
        int var3 = -1;
        int var4 = -1;
        byte var1 = 0;
        this.chromNextGen[var1].copyChromGenes(this.chromosomes[this.bestFitnessChromIndex]);
        int var8 = var1 + 1;
        this.chromNextGen[var8].copyChromGenes(this.chromosomes[this.bestFitnessChromIndex]);
        ++var8;
        Object var5;
        Object var6;
        if (this instanceof GAString) {
            var5 = new ChromChars(this.chromosomeDim);
            var6 = new ChromChars(this.chromosomeDim);
        } else if (this instanceof GAFloat) {
            var5 = new ChromFloat(this.chromosomeDim);
            var6 = new ChromFloat(this.chromosomeDim);
        } else {
            var5 = new ChromString(this.chromosomeDim);
            var6 = new ChromString(this.chromosomeDim);
        }

        do {
            int[] var7 = new int[]{var3, var4};
            this.selectTwoParents(var7);
            var3 = var7[0];
            var4 = var7[1];
            ((Chromosome)var5).copyChromGenes(this.chromosomes[var3]);
            ((Chromosome)var6).copyChromGenes(this.chromosomes[var4]);
            if (this.getRandom(1.0D) < this.crossoverProb) {
                if (this.crossoverType == 0) {
                    this.doOnePtCrossover((Chromosome)var5, (Chromosome)var6);
                } else if (this.crossoverType == 1) {
                    this.doTwoPtCrossover((Chromosome)var5, (Chromosome)var6);
                } else if (this.crossoverType == 2) {
                    this.doUniformCrossover((Chromosome)var5, (Chromosome)var6);
                } else if (this.crossoverType == 3) {
                    int var2 = this.getRandom(3);
                    if (var2 < 1) {
                        this.doOnePtCrossover((Chromosome)var5, (Chromosome)var6);
                    } else if (var2 < 2) {
                        this.doTwoPtCrossover((Chromosome)var5, (Chromosome)var6);
                    } else {
                        this.doUniformCrossover((Chromosome)var5, (Chromosome)var6);
                    }
                }

                this.chromNextGen[var8].copyChromGenes((Chromosome)var5);
                ++var8;
                this.chromNextGen[var8].copyChromGenes((Chromosome)var6);
                ++var8;
            } else {
                this.chromNextGen[var8].copyChromGenes((Chromosome)var5);
                ++var8;
                this.chromNextGen[var8].copyChromGenes((Chromosome)var6);
                ++var8;
            }
        } while(var8 < this.populationDim);

    }

    void copyNextGenToThisGen() {
        for(int var1 = 0; var1 < this.populationDim; ++var1) {
            this.chromosomes[var1].copyChromGenes(this.chromNextGen[var1]);
            if (var1 != this.bestFitnessChromIndex && (var1 == this.worstFitnessChromIndex || this.getRandom(1.0D) < this.mutationProb)) {
                this.doRandomMutation(var1);
            }
        }

    }

    void addChromosomesToLog(int var1, int var2) {
        if (var2 > this.populationDim) {
            var2 = this.chromosomeDim;
        }

        for(int var5 = 0; var5 < var2; ++var5) {
            this.chromosomes[var5].fitness = this.getFitness(var5);
            String var3 = "" + var1;
            if (var3.length() < 2) {
                var3 = var3 + " ";
            }

            String var4 = "" + var5;
            if (var4.length() < 2) {
                var4 = var4 + " ";
            }

            System.out.println("Gen " + var3 + ": Chrom" + var4 + " = " + this.chromosomes[var5].getGenesAsStr() + ", fitness = " + this.chromosomes[var5].fitness);
        }

    }

    protected double getAvgDeviationAmongChroms() {
        int var1 = 0;

        for(int var2 = 0; var2 < this.chromosomeDim; ++var2) {
            int var4;
            if (this instanceof GAString) {
                char var9 = ((ChromChars)this.chromosomes[this.bestFitnessChromIndex]).getGene(var2);

                for(var4 = 0; var4 < this.populationDim; ++var4) {
                    char var11 = ((ChromChars)this.chromosomes[var4]).getGene(var2);
                    if (var11 != var9) {
                        ++var1;
                    }
                }
            } else if (this instanceof GAFloat) {
                double var8 = ((ChromFloat)this.chromosomes[this.bestFitnessChromIndex]).getGene(var2);

                for(int var10 = 0; var10 < this.populationDim; ++var10) {
                    double var6 = ((ChromFloat)this.chromosomes[var10]).getGene(var2);
                    if (var6 != var8) {
                        ++var1;
                    }
                }
            } else {
                String var3 = ((ChromString)this.chromosomes[this.bestFitnessChromIndex]).getGene(var2);

                for(var4 = 0; var4 < this.populationDim; ++var4) {
                    String var5 = ((ChromString)this.chromosomes[var4]).getGene(var2);
                    if (!var5.equals(var3)) {
                        ++var1;
                    }
                }
            }
        }

        return (double)var1;
    }

    long binaryStrToInt(String var1) {
        long var4 = 0L;
        int var6 = var1.length();

        for(int var7 = var6 - 1; var7 >= 0; --var7) {
            long var2;
            if (var1.charAt(var7) == '1') {
                var2 = 1L;
            } else {
                var2 = 0L;
            }

            var4 += var2 << var6 - var7 - 1;
        }

        return var4;
    }
}
