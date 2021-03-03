package server;

import java.util.Date;
import java.util.List;

import bank.Statement;
import bank.Transaction;

public class StatementImpl implements Statement {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private String accountName;
  private int accountNum;
  private Date startDate;
  private Date endDate;
  private List<Transaction> transactions;

  public StatementImpl(Date startDate, Date endDate, String accountName, int accountNum,
      List<Transaction> transactions) {
    this.accountName = accountName;
    this.accountNum = accountNum;
    this.startDate = startDate;
    this.endDate = endDate;
    this.transactions = transactions;
  }

  public int getAccountNum() {
    return accountNum;
  } // returns account number associated with this statement

  public Date getStartDate() {
    return startDate;
  } // returns start Date of Statement

  public Date getEndDate() {
    return endDate;
  } // returns end Date of Statement

  public String getAccoutName() {
    return accountName;
  } // returns name of account holder

  public List<Transaction> getTransactions() {
    return transactions;
  } // return list of transactions included in this statement

  @Override
  public String toString() {
    String out = "Statement for " + accountName + "\n";
    out += "From " + startDate.toString() + " to " + endDate.toString() + "\n";

    for (Transaction t : transactions) {
      out += t.toString() + "\n";
    }

    return out;
  }
}
