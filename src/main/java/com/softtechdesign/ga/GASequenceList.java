package com.softtechdesign.ga;

public abstract class GASequenceList extends GAString {
    protected double[] sequence;

    protected String getChromWithoutDuplicates(String var1) {
        String var5 = "";

        int var2;
        String var4;
        for(int var7 = 0; var7 < super.possGeneValues.length(); ++var7) {
            var4 = "" + super.possGeneValues.charAt(var7);
            var2 = var1.indexOf(var4);
            if (var2 < 0) {
                var5 = var5 + var4;
            }
        }

        if (var5.length() == 0) {
            return var1;
        } else {
            StringBuffer var8 = new StringBuffer(var1);
            StringBuffer var9 = new StringBuffer(var5);

            for(int var10 = 0; var10 < super.chromosomeDim; ++var10) {
                var4 = "" + var8.charAt(var10);
                String var6 = var8.substring(var10 + 1, super.chromosomeDim);
                var2 = var6.indexOf(var4);
                if (var2 > -1) {
                    int var3 = this.getRandom(var9.length());
                    var8.setCharAt(var2 + var10 + 1, var9.charAt(var3));
                    var9.deleteCharAt(var3);
                }
            }

            return var8.toString();
        }
    }

    protected void initPopulation() {
        for(int var3 = 0; var3 < super.populationDim; ++var3) {
            String var2 = "";

            for(int var4 = 0; var4 < super.chromosomeDim; ++var4) {
                String var1;
                do {
                    var1 = "" + super.possGeneValues.charAt(this.getRandom(super.possGeneValues.length()));
                } while(var2.indexOf(var1) >= 0);

                var2 = var2 + var1;
            }

            ((ChromChars)super.chromosomes[var3]).setGenesFromStr(var2);
        }

    }

    protected void doOnePtCrossover(Chromosome var1, Chromosome var2) {
        super.doOnePtCrossover(var1, var2);
        String var3 = this.getChromWithoutDuplicates(var1.getGenesAsStr());
        String var4 = this.getChromWithoutDuplicates(var2.getGenesAsStr());
        ((ChromChars)var1).setGenesFromStr(var3);
        ((ChromChars)var2).setGenesFromStr(var4);
    }

    protected void doTwoPtCrossover(Chromosome var1, Chromosome var2) {
        super.doTwoPtCrossover(var1, var2);
        String var3 = this.getChromWithoutDuplicates(var1.getGenesAsStr());
        String var4 = this.getChromWithoutDuplicates(var2.getGenesAsStr());
        ((ChromChars)var1).setGenesFromStr(var3);
        ((ChromChars)var2).setGenesFromStr(var4);
    }

    protected void doUniformCrossover(Chromosome var1, Chromosome var2) {
        super.doUniformCrossover(var1, var2);
        String var3 = this.getChromWithoutDuplicates(var1.getGenesAsStr());
        String var4 = this.getChromWithoutDuplicates(var2.getGenesAsStr());
        ((ChromChars)var1).setGenesFromStr(var3);
        ((ChromChars)var2).setGenesFromStr(var4);
    }

    public GASequenceList(int var1, int var2, double var3, int var5, int var6, int var7, int var8, double var9, int var11, String var12, int var13, boolean var14) throws GAException {
        super(var1, var2, var3, var5, var6, var7, var8, var9, var11, var12, var13, var14);
        if (var12.length() != var1) {
            throw new GAException("Number of Possible gene values must equal Chromosome Dimension");
        } else {
            this.sequence = new double[var1];
        }
    }
}