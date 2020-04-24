package prz.cloud.example.cutting.stock.algorithm.impl;

import com.softtechdesign.ga.GAException;
import org.springframework.stereotype.Service;
import prz.cloud.example.cutting.stock.DAO.InputData;
import prz.cloud.example.cutting.stock.DTO.GASettings;
import prz.cloud.example.cutting.stock.algorithm.IGenAlgorithm;


@Service
public class GenAlgorithmImpl implements IGenAlgorithm {
    @Override
    public String getResult(InputData inputData) {
        return this.getResult(inputData, new GASettings());
    }
    @Override
    public String getResult(InputData inputData, GASettings settings) {
        String results = "Nie znaleziono optymalnego rozwiązania";
        long executionTime = -1;
        try {
            if (inputData == null) {
                throw new RuntimeException("Failed to read an input file!");
            }
            long millisActualTime = System.currentTimeMillis();

            GACuttingStock gaCuttingStock = GACuttingStockFactory.getGaCuttingStockObj(inputData, settings);
            Thread threadGraph = new Thread(gaCuttingStock);
            threadGraph.setPriority(Thread.MAX_PRIORITY);
            threadGraph.start();
            threadGraph.join();
            if (!threadGraph.isAlive()) {
                executionTime = System.currentTimeMillis() - millisActualTime;
                if (gaCuttingStock.getFittestChromosomesFitness() != 0) {
                    results = gaCuttingStock.performResult();
                }
            }
        } catch (InterruptedException | GAException e) {
            e.printStackTrace();
        }
        if (executionTime > -1)
            results = "Całkowity czas algorytmu: " + executionTime + " ms.<br>" + results;
        return results;
    }
}
