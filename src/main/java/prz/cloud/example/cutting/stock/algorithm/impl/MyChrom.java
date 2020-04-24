package prz.cloud.example.cutting.stock.algorithm.impl;

import com.softtechdesign.ga.ChromString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static prz.cloud.example.cutting.stock.algorithm.impl.GACuttingStock.StringToInt;

public class MyChrom implements Comparable<MyChrom> {
    private double fitness;
    private int totalWaste;
    private ChromString chromString;

    private MyChrom(ChromString chromString) {
        this.chromString = new ChromString(chromString.getGenes().length);
        for (int i = 0; i < chromString.getGenes().length; i++) {
            this.chromString.setGene(chromString.getGenes()[i], i);
        }
    }

    public static MyChrom generate(ChromString chromString, Map<Integer, Integer> beamsWithCountMap, int mainBeamLength, int maxPossibleWaste){
        if (!matchBeamsCount(chromString.getGenes(), beamsWithCountMap))
            return null;

        MyChrom myChrom = new MyChrom(chromString);
        fillFitness(myChrom, mainBeamLength, maxPossibleWaste);
        return myChrom;
    }

    private static void fillFitness(MyChrom myChrom, int mainBeamLength, int maxPossibleWaste) {
        String[] genes = myChrom.getGenes();

        ;
        int sumOfWaste = 0;
        int tmpMainBeam = mainBeamLength;
        for (String gen : genes) {
            int val = StringToInt(gen);

            if (tmpMainBeam < val) {
                sumOfWaste += tmpMainBeam;
                tmpMainBeam = mainBeamLength;
            }
            tmpMainBeam -= val;
        }

        if (tmpMainBeam < mainBeamLength && tmpMainBeam > 0) {
            sumOfWaste += tmpMainBeam;
        }


        // scaling the waste from 0 to 10 (for a rate)
        double wasteRate = (sumOfWaste * 10.0) / maxPossibleWaste;
        wasteRate = new BigDecimal(wasteRate).setScale(2, RoundingMode.HALF_UP).doubleValue();

        myChrom.setTotalWaste(sumOfWaste);
        myChrom.setFitness(10.0 - wasteRate);
    }

    private static boolean matchBeamsCount(String[] genes, Map<Integer, Integer> beamsWithCountMap) {
        Set<String> usedBeams = new HashSet<>(Arrays.asList(genes));

        if (beamsWithCountMap == null)
            return false;

        for (String usedBeamStr : usedBeams) {
            int usedBeamInt = StringToInt(usedBeamStr);
            int shouldCount = beamsWithCountMap.get(usedBeamInt);

            int counted = 0;
            for (String gen : genes) {
                int genVal = StringToInt(gen);
                if (genVal == usedBeamInt)
                    counted++;
            }

            if (shouldCount != counted)
                return false;
        }

        return true;
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

    public int getTotalWaste() {
        return totalWaste;
    }

    public void setTotalWaste(int totalWaste) {
        this.totalWaste = totalWaste;
    }

    public ChromString getChromString() {
        return chromString;
    }

    public void setChromString(ChromString chromString) {
        this.chromString = chromString;
    }

    @Override
    public String toString() {
        return this.getGenesAsString() + ", odpad = "+this.totalWaste + ", fitness = " + this.fitness;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyChrom myChrom = (MyChrom) o;
        return Objects.equals(chromString, myChrom.chromString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chromString);
    }
}
