package server;

import java.util.Date;
import org.joda.money.Money;

import bank.Transaction;
import bank.TransactionType;

public class TransactionImpl implements Transaction {
  private static final long serialVersionUID = 1L;

  private Money amount;
  private Date date;
  private String description;
  private TransactionType type;
  private int accountNum;

  public TransactionImpl(Money amount, Date date, String description, TransactionType type, int account) {
    this.accountNum = account;
    this.amount = amount;
    this.date = date;
    this.description = description;
    this.type = type;
  }

  public Money getAmount() {
    return amount;
  }

  public String getDescription() {
    return description;
  }

  public Date getDate() {
    return date;
  }

  public int getAccountNum() {
    return accountNum;
  }

  public TransactionType getTransactionType() {
    return type;
  }

  @Override
  public String toString() {
    return date.toString() + " " + type.toString() + " " + amount.toString();
  }

}
