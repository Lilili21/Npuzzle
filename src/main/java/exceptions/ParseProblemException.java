package exceptions;

public class ParseProblemException extends RuntimeException{
    public ParseProblemException(String message) {
        super(message);
    }
}
