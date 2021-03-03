package bank;

public class InvalidSession extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InvalidSession(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }
}