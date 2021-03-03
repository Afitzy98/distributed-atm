package server;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.ArrayList;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import bank.Statement;
import bank.Transaction;
import bank.TransactionType;

public class Account {

  private static int ACCOUNT_NUM = 1000000;
  private int accountNum;
  private String name;
  private Money balance;
  private ArrayList<Transaction> transactions;
  private Date joinDate;
  private final CurrencyUnit currency = CurrencyUnit.of("EUR");

  public Account(String name, float balance, Date joinDate) {
    this.accountNum = ACCOUNT_NUM++;
    this.name = name;
    this.balance = Money.of(currency, balance);
    this.joinDate = joinDate;
    this.transactions = new ArrayList<Transaction>();
  }

  public int getAccountNum() {
    return accountNum;
  }

  public String getAccountName() {
    return name;
  }

  public Date getJoinDate() {
    return joinDate;
  }

  public Money getBalance() {
    return balance;
  }

  public CurrencyUnit getCurrency() {
    return currency;
  }

  public ArrayList<Transaction> getTransactions() {
    return transactions;
  }

  public Statement getStatement(Date startDate, Date endDate) {

    ArrayList<Transaction> statementTransactions = transactions.stream()
        .filter(t -> !t.getDate().before(startDate) && !t.getDate().after(endDate))
        .collect(Collectors.toCollection(ArrayList::new));

    return new StatementImpl(startDate, endDate, name, accountNum, statementTransactions);
  }

  public void addTransaction(Transaction t) {
    switch (t.getTransactionType()) {
      case WITHDRAW:
        this.balance = balance.minus(t.getAmount());
        break;

      case DEPOSIT:
        this.balance = balance.plus(t.getAmount());
        break;

      default:
        break;
    }
    transactions.add(t);
  }

}
