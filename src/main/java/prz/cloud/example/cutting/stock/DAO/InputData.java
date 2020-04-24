package prz.cloud.example.cutting.stock.DAO;

import prz.cloud.example.cutting.stock.DAO.exception.*;

/**
 * Klasa reprezentująca odczytane dane z pliku
 */
public class InputData {
    /**
     * Długość belki głównej (roboczej)
     */
    private int mainBeamLength;

    /**
     * Ilość elementów do cięcia
     */
    private int numberOfElements;

    /**
     * Długości elementów (belek) do cięcia
     */
    private int[] beamLengths;

    // Konstruktor
    private InputData(int mainBeamLength, int numberOfElements, int[] beamLengths)
            throws IncorrectNumberOfElementsException, IncorrectBeamLengthException {
        if (mainBeamLength <=0)
            throw new IncorrectBeamLengthException(mainBeamLength);

        if (beamLengths.length == 0)
            throw new ArrayIndexOutOfBoundsException("Incorrect array length!");

        if (numberOfElements <= 0 || numberOfElements != beamLengths.length)
            throw new IncorrectNumberOfElementsException(numberOfElements);

        this.mainBeamLength = mainBeamLength;
        this.numberOfElements = numberOfElements;
        this.beamLengths = beamLengths;
    }

    public InputData(int mainBeamLength, int[] beamLengths) throws IncorrectBeamLengthException, IncorrectNumberOfElementsException {
        this(mainBeamLength, beamLengths.length, beamLengths);
    }

    public int getMainBeamLength() {
        return mainBeamLength;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int[] getBeamLengths() {
        return beamLengths;
    }

}
