package bank;

public enum TransactionType {
  DEPOSIT("deposit"), WITHDRAW("withdraw");

  private final String text;

  /**
   * @param text
   */
  TransactionType(final String text) {
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
