package ExceptionClasses;

public class UnauthorizedException extends Exception {

    private final int statusCode;
    public UnauthorizedException() {
        super("401 Error: unauthorized");
        statusCode = 401;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
