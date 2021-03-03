package bank;

public class InvalidLogin extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InvalidLogin(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }
}