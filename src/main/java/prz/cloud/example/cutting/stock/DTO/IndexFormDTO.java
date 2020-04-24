package prz.cloud.example.cutting.stock.DTO;

import prz.cloud.example.cutting.stock.DAO.InputData;
import prz.cloud.example.cutting.stock.DAO.exception.IncorrectBeamLengthException;
import prz.cloud.example.cutting.stock.DAO.exception.IncorrectNumberOfElementsException;

import java.util.Scanner;


public class IndexFormDTO implements Converter<InputData>{
    private Integer mainBeam;
    private Integer nOfElements;
    private String allElements;

    public IndexFormDTO() {
    }

    public IndexFormDTO(Integer mainBeam, Integer nOfElements, String allElements) {
        this.mainBeam = mainBeam;
        this.nOfElements = nOfElements;
        this.allElements = allElements;
    }

    public Integer getMainBeam() {
        return mainBeam;
    }

    public void setMainBeam(Integer mainBeam) {
        this.mainBeam = mainBeam;
    }

    public Integer getnOfElements() {
        return nOfElements;
    }

    public void setnOfElements(Integer nOfElements) {
        this.nOfElements = nOfElements;
    }

    public String getAllElements() {
        return allElements;
    }

    public void setAllElements(String allElements) {
        this.allElements = allElements;
    }

    @Override
    public InputData convertToDTO() {
        InputData result = null;
        if (nOfElements !=null && nOfElements > 0 && allElements !=null && allElements.trim().length() > 0) {
            Scanner scanner = new Scanner(allElements);
            int[] beamLengths = new int[nOfElements];

            for (int i = 0; scanner.hasNextLine() && i < beamLengths.length; i++) {
                beamLengths[i] = scanner.nextInt();
            }
            try {
                result = new InputData(mainBeam, beamLengths);
            } catch (IncorrectBeamLengthException | IncorrectNumberOfElementsException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
