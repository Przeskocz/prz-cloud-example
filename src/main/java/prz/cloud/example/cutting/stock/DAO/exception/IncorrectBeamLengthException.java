package prz.cloud.example.cutting.stock.DAO.exception;

public class IncorrectBeamLengthException extends Exception {
    private static final String REASON = "Incorrect beam length! The beam mustn't have a length equal to or less than 0!";

    public IncorrectBeamLengthException(String ex) {
        super(REASON + ex);
    }

    public IncorrectBeamLengthException(int beamLength) {
        super(REASON + "\nEntered length: "+ beamLength);
    }

    public IncorrectBeamLengthException() {
        super(REASON);
    }
}

