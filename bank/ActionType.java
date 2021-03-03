package bank;

public enum ActionType {
  BALANCE("balance"), DEPOSIT("deposit"), LOGIN("login"), STATEMENT("statement"), WITHDRAW("withdraw");

  private final String text;

  /**
   * @param text
   */
  ActionType(final String text) {
    this.text = text;
  }

  public String value() {
    return text;
  }

  @Override
  public String toString() {
    return text;
  }
}
