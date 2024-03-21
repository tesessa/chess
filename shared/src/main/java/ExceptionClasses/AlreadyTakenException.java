package ExceptionClasses;

public class AlreadyTakenException extends Exception {
    final int statusCode;
    public AlreadyTakenException() {
        super("Error: already taken");
        statusCode = 403;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
