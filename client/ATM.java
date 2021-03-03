package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import bank.ActionType;
import bank.Bank;
import bank.InvalidSession;
import bank.InvalidLogin;

public class ATM {
  private static final CurrencyUnit currency = CurrencyUnit.of("EUR");
  private static final SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yy");

  public static void main(String args[]) throws Exception {
    int registryport = 20345;
    long sessionID;
    int accountNum;
    float transactionAmount;
    if (args.length > 0)
      registryport = Integer.parseInt(args[0]);

    System.out.println("RMIRegistry port = " + registryport);

    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      String name = "BankServer";
      Registry registry = LocateRegistry.getRegistry(registryport);
      Bank bank = (Bank) registry.lookup(name);

      switch (ActionType.valueOf(args[1].toUpperCase())) {
        case BALANCE:
          sessionID = Long.parseLong(args[2]);
          accountNum = Integer.parseInt(args[3]);
          Money balance = bank.getBalance(accountNum, sessionID);
          System.out.println("The current balance of account " + accountNum + " is " + balance.toString());
          break;

        case DEPOSIT:
          sessionID = Long.parseLong(args[2]);
          accountNum = Integer.parseInt(args[3]);
          transactionAmount = Float.parseFloat(args[4]);
          Money deposit = Money.of(currency, transactionAmount);
          bank.deposit(accountNum, deposit, sessionID);
          System.out.println("Successfully deposited " + deposit.toString() + " to account " + accountNum);
          break;

        case LOGIN:
          String username = args[2];
          String password = args[3];
          sessionID = bank.login(username, password);
          System.out
              .println("Successful login for " + username + " : session ID " + sessionID + " is valid for 5 minutes");
          break;

        case STATEMENT:
          sessionID = Long.parseLong(args[2]);
          accountNum = Integer.parseInt(args[3]);
          Date from = dateParser.parse(args[4]);
          Date to = dateParser.parse(args[5]);
          System.out.println(bank.getStatement(accountNum, from, to, sessionID).toString());
          break;

        case WITHDRAW:
          sessionID = Long.parseLong(args[2]);
          accountNum = Integer.parseInt(args[3]);
          transactionAmount = Float.parseFloat(args[4]);
          Money withdraw = Money.of(currency, transactionAmount);
          bank.withdraw(accountNum, withdraw, sessionID);
          System.out.println("Successfully withdrew " + withdraw.toString() + " from account " + accountNum);
          break;

        default:
          System.out.println("Error: Invalid Action.");
      }

    } catch (InvalidLogin e) {
      System.out.println(e.getMessage());
    } catch (InvalidSession e) {
      System.out.println(e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Error: Invalid Action.");
    }

    catch (Exception e) {
      System.err.println("ATM Client exception:");
      e.printStackTrace();
    }
  }
}
