package se.accelerateit.signup6.modelvalidator;

public class DataModelException extends RuntimeException {
  public DataModelException(String message) {
    super(message);
  }

  public DataModelException(String message, Throwable cause) {
    super(message, cause);
  }
}
