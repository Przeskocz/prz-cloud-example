package com.softtechdesign.ga;

public class ChromString extends Chromosome {
    private String[] genes;

    public ChromString(int var1) {
        this.genes = new String[var1];
    }

    public String toString() {
        return this.getGenesAsStr();
    }

    public void setGene(String var1, int var2) {
        this.genes[var2] = var1;
    }

    public int getNumGenesInCommon(Chromosome var1) {
        int var2 = 0;
        ChromString var3 = (ChromString)var1;

        for(int var4 = 0; var4 < this.genes.length; ++var4) {
            if (this.genes[var4].equals(var3.genes[var4])) {
                ++var2;
            }
        }

        return var2;
    }

    public String[] getGenes() {
        return this.genes;
    }

    public String getGenesAsStr() {
        int var1 = this.genes.length;
        StringBuffer var2 = new StringBuffer(var1);

        for(int var3 = 0; var3 < var1; ++var3) {
            var2.append(this.genes[var3]);
            if (var3 < var1 - 1) {
                var2.append("|");
            }
        }

        return var2.toString();
    }

    public String getGene(int var1) {
        return this.genes[var1];
    }

    public void copyChromGenes(Chromosome var1) {
        ChromString var3 = (ChromString)var1;

        for(int var2 = 0; var2 < this.genes.length; ++var2) {
            this.genes[var2] = var3.genes[var2];
        }

    }
}