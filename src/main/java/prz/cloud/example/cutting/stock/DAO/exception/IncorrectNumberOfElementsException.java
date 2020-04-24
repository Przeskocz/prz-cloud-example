package prz.cloud.example.cutting.stock.DAO.exception;

public class IncorrectNumberOfElementsException extends Exception {
    private static final String REASON = "Incorrect number of elements! The number of elements cannot be less than or equal to 0!";

    public IncorrectNumberOfElementsException(String ex) {
        super(REASON + ex);
    }

    public IncorrectNumberOfElementsException(int beamLength) {
        super(REASON + "\nEntered  number of elements: "+ beamLength);
    }

    public IncorrectNumberOfElementsException() {
        super(REASON);
    }
}

