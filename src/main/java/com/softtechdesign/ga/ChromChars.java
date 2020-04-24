package com.softtechdesign.ga;

public class ChromChars extends Chromosome {
    protected char[] genes;

    public ChromChars(int var1) {
        this.genes = new char[var1];
    }

    public String toString() {
        return this.getGenesAsStr();
    }

    public int getNumGenesInCommon(Chromosome var1) {
        int var2 = 0;
        String var3 = var1.getGenesAsStr();

        for(int var4 = 0; var4 < this.genes.length; ++var4) {
            if (this.genes[var4] == var3.charAt(var4)) {
                ++var2;
            }
        }

        return var2;
    }

    public char[] getGenes() {
        return this.genes;
    }

    public String getGenesAsStr() {
        String var1 = "";

        for(int var2 = 0; var2 < this.genes.length; ++var2) {
            var1 = var1 + this.genes[var2];
        }

        return var1;
    }

    public char getGene(int var1) {
        return this.genes[var1];
    }

    public void setGenesFromStr(String var1) {
        for(int var2 = 0; var2 < this.genes.length; ++var2) {
            this.genes[var2] = var1.charAt(var2);
        }

    }

    public void copyChromGenes(Chromosome var1) {
        ChromChars var3 = (ChromChars)var1;

        for(int var2 = 0; var2 < this.genes.length; ++var2) {
            this.genes[var2] = var3.genes[var2];
        }

    }
}
