/**
 * DataLayerException.java
 * Group 1
 * Instructor: Jim Habermas
 * ISTE-330
 * Fall 2024
 */

public class DataLayerException extends Exception{
    private String timestamp;
    private String methodName;
    private String additionalInfo;

    public DataLayerException(Throwable cause, String timestamp, String methodName, String... additionalInfo) {
        super(cause);
        this.timestamp = timestamp;
        this.methodName = methodName;
        this.additionalInfo = String.join(", ", additionalInfo);
    }

    @Override
    public String getMessage() {
        return "Exception in method: " + methodName +
               "\nTimestamp: " + timestamp +
               "\nDetails: " + additionalInfo +
               "\nCause: " + super.getCause();
    }
}
