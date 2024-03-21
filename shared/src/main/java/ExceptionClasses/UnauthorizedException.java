package ExceptionClasses;

public class UnauthorizedException extends Exception {

    private final int statusCode;
    public UnauthorizedException() {
        super("Error: unauthorized");
        statusCode = 401;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
