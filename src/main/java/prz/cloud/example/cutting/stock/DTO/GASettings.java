package prz.cloud.example.cutting.stock.DTO;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Component
@Scope("session")
public class GASettings implements Serializable {
    private int nOfChromToEvo;
    private double crossingProb;
    private int chanceOfRandom;
    private int nMaxOfGeneToEvol;
    private int nOfGeneToEvolToInit;
    private int nMaxOfGeneToInit;
    private double mutationProb;
    private int decimalPlaces;
    private int corossoverType;
    /*  ctOnePoint = 0;
        ctTwoPoint = 1;
        ctUniform = 2;
        ctRoulette = 3; */
    private boolean calculateStats;

    public GASettings() {
        resetSettings();
    }

    public GASettings(int nOfChromToEvo, double crossingProb, int chanceOfRandom, int nMaxOfGeneToEvol, int nOfGeneToEvolToInit, int nMaxOfGeneToInit, double mutationProb, int decimalPlaces, int corossoverType, boolean calculateStats) {
        this.nOfChromToEvo = nOfChromToEvo;
        this.crossingProb = crossingProb;
        this.chanceOfRandom = chanceOfRandom;
        this.nMaxOfGeneToEvol = nMaxOfGeneToEvol;
        this.nOfGeneToEvolToInit = nOfGeneToEvolToInit;
        this.nMaxOfGeneToInit = nMaxOfGeneToInit;
        this.mutationProb = mutationProb;
        this.decimalPlaces = decimalPlaces;
        this.corossoverType = corossoverType;
        this.calculateStats = calculateStats;
    }

    public int getnOfChromToEvo() {
        return nOfChromToEvo;
    }

    public void setnOfChromToEvo(int nOfChromToEvo) {
        this.nOfChromToEvo = nOfChromToEvo;
    }

    public double getCrossingProb() {
        return crossingProb;
    }

    public void setCrossingProb(double crossingProb) {
        this.crossingProb = crossingProb;
    }

    public int getChanceOfRandom() {
        return chanceOfRandom;
    }

    public void setChanceOfRandom(int chanceOfRandom) {
        this.chanceOfRandom = chanceOfRandom;
    }

    public int getnMaxOfGeneToEvol() {
        return nMaxOfGeneToEvol;
    }

    public void setnMaxOfGeneToEvol(int nMaxOfGeneToEvol) {
        this.nMaxOfGeneToEvol = nMaxOfGeneToEvol;
    }

    public int getnOfGeneToEvolToInit() {
        return nOfGeneToEvolToInit;
    }

    public void setnOfGeneToEvolToInit(int nOfGeneToEvolToInit) {
        this.nOfGeneToEvolToInit = nOfGeneToEvolToInit;
    }

    public int getnMaxOfGeneToInit() {
        return nMaxOfGeneToInit;
    }

    public void setnMaxOfGeneToInit(int nMaxOfGeneToInit) {
        this.nMaxOfGeneToInit = nMaxOfGeneToInit;
    }

    public double getMutationProb() {
        return mutationProb;
    }

    public void setMutationProb(double mutationProb) {
        this.mutationProb = mutationProb;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public int getCorossoverType() {
        return corossoverType;
    }

    public void setCorossoverType(int corossoverType) {
        this.corossoverType = corossoverType;
    }

    public boolean setCalculateStats() {
        return calculateStats;
    }

    public boolean isCalculateStats() {
        return calculateStats;
    }

    public void getCalculateStats(boolean calculateStats) {
        this.calculateStats = calculateStats;
    }

    public static void resetSettings(GASettings s) {
        s.nOfChromToEvo = 200;
        s.crossingProb = 0.4;
        s.chanceOfRandom = 10;
        s.nMaxOfGeneToEvol = 1000;
        s.nOfGeneToEvolToInit = 0;
        s.nMaxOfGeneToInit = 20;
        s.mutationProb = 0.4;
        s.decimalPlaces = 0;
        s.corossoverType = 1;
        s.calculateStats = false;
    }

    public void resetSettings() {
        resetSettings(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GASettings settings = (GASettings) o;
        return nOfChromToEvo == settings.nOfChromToEvo &&
                Double.compare(settings.crossingProb, crossingProb) == 0 &&
                chanceOfRandom == settings.chanceOfRandom &&
                nMaxOfGeneToEvol == settings.nMaxOfGeneToEvol &&
                nOfGeneToEvolToInit == settings.nOfGeneToEvolToInit &&
                nMaxOfGeneToInit == settings.nMaxOfGeneToInit &&
                Double.compare(settings.mutationProb, mutationProb) == 0 &&
                decimalPlaces == settings.decimalPlaces &&
                corossoverType == settings.corossoverType &&
                calculateStats == settings.calculateStats;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nOfChromToEvo, crossingProb, chanceOfRandom, nMaxOfGeneToEvol, nOfGeneToEvolToInit, nMaxOfGeneToInit, mutationProb, decimalPlaces, corossoverType, calculateStats);
    }
}
