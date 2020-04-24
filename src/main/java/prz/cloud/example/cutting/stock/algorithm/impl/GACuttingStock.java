package prz.cloud.example.cutting.stock.algorithm.impl;

import com.softtechdesign.ga.ChromString;
import com.softtechdesign.ga.GAException;
import com.softtechdesign.ga.GAStringsSeq;
import prz.cloud.example.cutting.stock.DTO.GASettings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@SuppressWarnings({"Duplicates", "SpellCheckingInspection"})
public class GACuttingStock extends GAStringsSeq {
    /**
     * Klucze - belki, wartości - ilość do cięcia
     */
    private Map<Integer, Integer> beamsWithCountMap = null;
    /**
     * Unikalne mniejszych belek do pocięcia
     */
    private String[] possibleCutting = null;
    /**
     * Łączna liczba wszystkich belek do pocięcia
     */
    private int numberOfElements = 0;
    /**
     * Długość belki głównej (roboczej)
     */
    private int mainBeamLength = 0;
    /**
     * Czy algorytm już jest po etapie inicjalizacji?
     */
    private boolean isAfterInit = false;

    private int maxPossibleWaste = 0;

    private Set<MyChrom> chromosomeSet = new HashSet<>();

    GACuttingStock(Map<Integer, Integer> beamsWithCountMap, String[] possibleCutting, int numberOfElements, int mainBeamLength, GASettings settings) throws GAException {
        super(numberOfElements, // wymiar chromosomu (liczba genów)
                settings.getnOfChromToEvo(),         // liczba chromosomów do ewolucji
                settings.getCrossingProb(),         // prawdopodobieństwo wystąpienia krzyżowania podczas kojarzenia genetycznego
                settings.getChanceOfRandom(),         // szansa na losowy wybór % (niezależnie od oceny (fitness))
                settings.getnMaxOfGeneToEvol(),         // maksymalna liczba pokoleń do ewolucji
                settings.getnOfGeneToEvolToInit(),          // liczba inicjalizujących pokoleń do ewolucji
                settings.getnMaxOfGeneToInit(),         // maks. liczba generacji na przebieg inicjalizujący
                settings.getMutationProb(),        // prawdopodobieństwo wystąpienia mutacji podczas kojarzenia genetycznego.
                settings.getDecimalPlaces(),           // (NIEOBSŁUGIWANE)liczba miejsc dziesiętnych w chromie
                possibleCutting, // materiał genetyczny lub pula „dozwolonych” lub „możliwych” wartości genów
                settings.getCorossoverType(), // rodzaj krzyżowania do zastosowania podczas kojarzenia genetycznego
                // (ctOnePoint - crossover jednopunktowy,
                // ctRoulette - crossover w ruletkę (jeden punkt, dwa punkty lub jednolity),
                // ctTwoPoint - crossover dwupunktowy,
                // ctUniform - crossover jednolity )
                settings.isCalculateStats());     // czy obliczać statystyki dla każdego pokolenia podczas ewolucji?

        initPopulation();

        this.beamsWithCountMap = beamsWithCountMap;
        this.possibleCutting = possibleCutting;
        this.numberOfElements = numberOfElements;
        this.mainBeamLength = mainBeamLength;
        this.isAfterInit = true;


        for(Map.Entry<Integer, Integer> entry: beamsWithCountMap.entrySet()) {
            int beam = entry.getKey();
            int count = entry.getValue();

            maxPossibleWaste += (mainBeamLength-beam)*count;
        }
    }

    /**
     * @return Wartość sprawności (ocena) dla danego chromosomu
     * */
    @Override
    protected double getFitness(int chromeIndex) {
        if (!isAfterInit) {
            return 0;
        }
        ChromString chromosome = new ChromString(getChromosome(chromeIndex).getGenes().length);
        for(int i = 0; i<getChromosome(chromeIndex).getGenes().length; i++) {
            chromosome.setGene(getChromosome(chromeIndex).getGenes()[i],i);
        }
        MyChrom chrom = getFitness(chromosome);
        if (chrom != null) {
            chromosomeSet.add(chrom);
            return chrom.getFitness();
        } else {
            return 0;
        }
    }

    private MyChrom getFitness(ChromString chromosome) {

        String[] genes = chromosome.getGenes();

        if (!matchBeamsCount(genes))
            return null;


        MyChrom myChrom = new MyChrom(chromosome);
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
        myChrom.setFitness(10.0 - wasteRate);
        myChrom.setTotalWaste(sumOfWaste);
        return myChrom;
    }

    /**
     * Długość i ilość belek musi odpowiadać początkowym ograniczeniom
     * @param genes Tablica genów, czyli belek w sposobie cięcia
     * @return Czy rodzaj cięcia spełnia początkowe ograniczenia
     * */
    private boolean matchBeamsCount(String[] genes) {
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

    static String IntToString(Integer v) {
        return v.toString();
    }

    private static int StringToInt(String v) {
        return Integer.parseInt(v);
    }

    /**
     * Wyświetla końcowy (najlepszy) rezultat algorytmu
     */
    String performResult(){
        StringBuilder result = new StringBuilder();
        ChromString chromosome = (ChromString) this.getFittestChromosome();
        MyChrom myChrom = new MyChrom(chromosome);
        String[] genes = myChrom.getGenes();
        List<String> resultList = new ArrayList<>();

        int workLengthBeam = this.mainBeamLength;
        int index = 0;
        int sumOfWaste = 0;
        for (String beam : genes) {
            int beamLength = StringToInt(beam);

            if (beamLength > workLengthBeam) {
                String ct = resultList.get(index) + " -> odpad: " + workLengthBeam;
                resultList.set(index, ct);
                sumOfWaste += workLengthBeam;
                workLengthBeam = this.mainBeamLength;
                index++;
            }

            workLengthBeam -= beamLength;

            if (resultList.size() <= index)
                resultList.add(index, "");

            String ct = resultList.get(index).isEmpty() ? IntToString(beamLength) : resultList.get(index) + " | " + beamLength  ;
            resultList.set(index, ct);
        }

        String ct = resultList.get(index) + " -> odpad: " + workLengthBeam;
        resultList.set(index, ct);
        sumOfWaste += workLengthBeam;

        index = 0;
        StringBuilder elements = new StringBuilder();
        for(Map.Entry<Integer, Integer> entry : beamsWithCountMap.entrySet()) {
            int k = entry.getKey();
            int v = entry.getValue();
            elements.append(k).append(" x").append(v).append(", ");
        }
        elements.setLength(elements.length()-2);


        result.append("\nZamówienie:\n\t" + "- długośc belki głównej: ").append(mainBeamLength).append("\n\t").append("- liczba elementów: ").append(numberOfElements).append("\n\t").append("- elementy do pocięcia: ").append(elements.toString()).append("\n\n");
        result.append("Optymalny rezultat rozkroju:\n");
        for (String cut : resultList) {
            result.append("Cięcie#").append(++index).append(": ").append(cut).append("\n");
        }
        result.append("Ocena rozwiązania: ").append(getFitness(chromosome)).append("\n");
        result.append("Łączny odpad: ").append(sumOfWaste).append("\n");



        List<MyChrom> listOfChrom = new ArrayList<>(chromosomeSet);
        listOfChrom.sort((o1, o2) -> Double.compare(o2.getFitness(), o1.getFitness()));
        result.append("\nLista zakwalifikowanych chromosomów (").append(listOfChrom.size()).append("):\n");
        int i=1;
        for(MyChrom chromElem : listOfChrom) {
            StringBuilder res = new StringBuilder("Chrom");
            if (i < 10)
                res.append("0");
            res.append(i++).append(" = ");
            res.append(chromElem.toString());
            result.append(res.toString()).append("\n");
        }
        return result.toString().replace("\n", "<br>");
    }
}