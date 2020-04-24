package prz.cloud.example.cutting.stock.algorithm;

import prz.cloud.example.cutting.stock.DAO.InputData;
import prz.cloud.example.cutting.stock.DTO.GASettings;


public interface IGenAlgorithm {
    String getResult(InputData inputData);
    String getResult(InputData inputData, GASettings settings);
}
