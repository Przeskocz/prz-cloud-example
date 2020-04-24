package prz.cloud.example.cutting.stock.algorithm.impl;

import com.softtechdesign.ga.GAException;
import prz.cloud.example.cutting.stock.DAO.InputData;


import java.util.*;
import java.util.stream.Collectors;

import static prz.cloud.example.cutting.stock.algorithm.impl.GACuttingStock.IntToString;


class GACuttingStockFactory {

    private GACuttingStockFactory(){}

    /**
     * Metoda przygotowuje i zwraca obiekt klasy GACuttingStock
     * @param dataSample Obiekt odczytanego pliku wejściowego
     * @return Obiekt rozszerzony o biblitekę GALib, modelujący sposoby cięcia jako tablice ciągów
     * @throws GAException Wyjątek z biblioteki GALib
     */
    static GACuttingStock getGaCuttingStockObj(InputData dataSample) throws GAException {
        // Przykład: 5,5,5,2,2,2,3,3,3
        List<Integer> allBeams = Arrays.stream(dataSample.getBeamLengths()).boxed().collect(Collectors.toList());

        // Przykład: 5,2,3
        List<Integer> uniqueListBeams = new ArrayList<>(new HashSet<>(allBeams));
        uniqueListBeams.sort(Integer::compareTo);

        // Jakie dłużyce, po ile sztuk
        Map<Integer, Integer> beamsWithCountMap = new LinkedHashMap<>();
        for(Integer tmpBeam : uniqueListBeams) {
            Integer count = (int) allBeams.stream().filter(a -> a.intValue() == tmpBeam.intValue()).count();
            beamsWithCountMap.put(tmpBeam, count);
        }

        // Tablica unikalnych dłużyc (tylko informacja jakie rodzaje dłużyc)
        String[] uniqueBeamsStr = new String[uniqueListBeams.size()];
        int i = 0;
        for (int item: uniqueListBeams) {
            uniqueBeamsStr[i++]=IntToString(item);
        }

        return new GACuttingStock(beamsWithCountMap, uniqueBeamsStr,
                dataSample.getNumberOfElements(), dataSample.getMainBeamLength());
    }
}
