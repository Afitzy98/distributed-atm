package bank;

import java.io.Serializable;
import java.util.Date;
import org.joda.money.Money;

public interface Transaction extends Serializable {

  public Money getAmount();

  public Date getDate();

  public String getDescription();

  public TransactionType getTransactionType();

}
