package ExceptionClasses;

public class BadRequestException extends Exception {

    private final int statusCode;
   public BadRequestException() {

       super("400 Error: bad request");
       statusCode = 400;
    }

    public int getStatusCode() {
       return statusCode;
    }
}
