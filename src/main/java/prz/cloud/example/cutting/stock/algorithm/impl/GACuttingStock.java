package prz.cloud.example.cutting.stock.algorithm.impl;

import com.softtechdesign.ga.ChromString;
import com.softtechdesign.ga.Crossover;
import com.softtechdesign.ga.GAException;
import com.softtechdesign.ga.GAStringsSeq;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    GACuttingStock(Map<Integer, Integer> beamsWithCountMap, String[] possibleCutting, int numberOfElements, int mainBeamLength) throws GAException {
        super(numberOfElements, // wymiar chromosomu (liczba genów)
                200,         // liczba chromosomów do ewolucji
                0.4,         // prawdopodobieństwo wystąpienia krzyżowania podczas kojarzenia genetycznego
                10,         // szansa na losowy wybór % (niezależnie od oceny (fitness))
                1000,         // maksymalna liczba pokoleń do ewolucji
                0,          // liczba inicjalizujących pokoleń do ewolucji
                20,         // maks. liczba generacji na przebieg inicjalizujący
                0.4,        // prawdopodobieństwo wystąpienia mutacji podczas kojarzenia genetycznego.
                0,           // (NIEOBSŁUGIWANE)liczba miejsc dziesiętnych w chromie
                possibleCutting, // materiał genetyczny lub pula „dozwolonych” lub „możliwych” wartości genów
                Crossover.ctTwoPoint, // rodzaj skrzyżowania do zastosowania podczas kojarzenia genetycznego
                // (ctOnePoint - crossover jednopunktowy,
                // ctRoulette - crossover w ruletkę (jeden punkt, dwa punkty lub jednolity),
                // ctTwoPoint - crossover dwupunktowy,
                // ctUniform - crossover jednolity )
                false);     // czy obliczać statystyki dla każdego pokolenia podczas ewolucji?

        initPopulation();

        this.beamsWithCountMap = beamsWithCountMap;
        this.possibleCutting = possibleCutting;
        this.numberOfElements = numberOfElements;
        this.mainBeamLength = mainBeamLength;
        this.isAfterInit = true;
    }

    /**
     * @return Wartość sprawności (ocena) dla danego chromosomu
     * */
    @Override
    protected double getFitness(int chromeIndex) {
        if (!isAfterInit) {
            return 0;
        }

        ChromString chromosome = getChromosome(chromeIndex);
        String[] genes = chromosome.getGenes();

        if (!matchBeamsCount(genes))
            return 0;

        int sumOfWaste = 0;
        int belkaWyj = mainBeamLength;
        for (String gen : genes) {
            int val = StringToInt(gen);

            if (belkaWyj < val) {
                sumOfWaste += belkaWyj;
                belkaWyj = mainBeamLength;
            }
            belkaWyj -= val;
        }

        if (belkaWyj < mainBeamLength && belkaWyj > 0) {
            sumOfWaste += belkaWyj;
        }

        return mainBeamLength - sumOfWaste;
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
        ChromString chromosome = (ChromString) this.getFittestChromosome();
        String genes[] = chromosome.getGenes();
        StringBuilder resultString = new StringBuilder();

        int workLengthBeam = this.mainBeamLength;
        int sumOfWaste = 0;
        for (String beam : genes) {
            int beamLength = StringToInt(beam);

            if (beamLength > workLengthBeam) {
                resultString.append(" -> waste: ").append(workLengthBeam).append("<br>");
                sumOfWaste += workLengthBeam;
                workLengthBeam = this.mainBeamLength;
            }

            workLengthBeam -= beamLength;
            resultString.append((workLengthBeam + beamLength == this.mainBeamLength) ? IntToString(beamLength) : " | " + beamLength);
        }

        resultString.append(" -> waste: ").append(workLengthBeam).append("<br>");
        sumOfWaste += workLengthBeam;
        resultString.append("Total waste: ").append(sumOfWaste);

        return resultString.toString();
    }
}