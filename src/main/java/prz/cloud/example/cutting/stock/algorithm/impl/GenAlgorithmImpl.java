package prz.cloud.example.cutting.stock.algorithm.impl;

import com.softtechdesign.ga.GAException;
import org.springframework.stereotype.Service;
import prz.cloud.example.cutting.stock.DAO.InputData;
import prz.cloud.example.cutting.stock.algorithm.IGenAlgorithm;


@Service
public class GenAlgorithmImpl implements IGenAlgorithm {
    @Override
    public String getResult(InputData inputData) {
        try {

            if (inputData == null) {
                throw new RuntimeException("Failed to read an input file!");
            }

            GACuttingStock gaCuttingStock = GACuttingStockFactory.getGaCuttingStockObj(inputData);
            Thread threadGraph = new Thread(gaCuttingStock);
            threadGraph.setPriority(Thread.MAX_PRIORITY);
            threadGraph.start();
            threadGraph.join();
            if (!threadGraph.isAlive()) {
                if (gaCuttingStock.getFittestChromosomesFitness() != 0) {
                    return gaCuttingStock.performResult();
                }
            }
        } catch (InterruptedException | GAException e) {
            e.printStackTrace();
        }
        return "";
    }
}
