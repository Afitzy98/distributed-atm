package server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import org.joda.money.Money;

import bank.Bank;
import bank.InvalidLogin;
import bank.InvalidSession;
import bank.Statement;
import bank.Transaction;
import bank.TransactionType;

public class BankEngine implements Bank {
  private List<Account> accounts; // users accounts
  private List<User> users;
  private final static int SESSION_LENGTH_MINS = 5;

  public long login(String username, String password) throws RemoteException, InvalidLogin {
    try {
      // find the user by username in the list of users
      User user = users.stream().filter(u -> u.getUsername().equals(username)).findAny().orElse(null);

      // if cant find user throw invalid login
      if (user == null) {
        throw new RemoteException("Username not found.");
      }

      // make sure passwords match
      boolean validPassword = user.isPasswordValid(password);

      // throw error if they dont
      if (!validPassword) {
        throw new RemoteException("Incorrect password.");
      }
      // return system nano seconds as login token
      return System.nanoTime();

    } catch (RemoteException e) {
      throw new InvalidLogin("Invalid login credentials.", e);
    }
  }

  public BankEngine() throws RemoteException {
    try {
      // initalise lists
      accounts = new ArrayList<Account>();
      users = new ArrayList<User>();

      // add some users and accounts
      User user1 = new User("John Doe", "jdoe", "secret");
      User user2 = new User("Mary Black", "mblack", "password");
      User user3 = new User("Sarah Collins", "collins", "pwd");

      // add users and accounts to lists to store them
      accounts.add(user1.createAccount(100));
      users.add(user1);
      accounts.add(user2.createAccount(100));
      users.add(user2);
      accounts.add(user3.createAccount(100));
      users.add(user3);
    } catch (Exception e) {
      throw new RemoteException("Error initialising the bank object.", e);
    }

  }

  public void deposit(int accountNum, Money amount, long sessionID) throws RemoteException, InvalidSession {
    try {

      // use func for validating session
      Account acc = validateSession(sessionID, accountNum);

      // add new deposit transaction to account
      acc.addTransaction(
          new TransactionImpl(amount, new Date(), "Deposit " + amount.toString(), TransactionType.DEPOSIT, accountNum));

    } catch (RemoteException e) {
      throw new InvalidSession("Session details are invalid.", e);
    }

  }

  public void addUser(User u) {
    users.add(u);
  }

  public void addAccount(Account a) {
    accounts.add(a);
  }

  public void withdraw(int accountNum, Money amount, long sessionID) throws RemoteException, InvalidSession {
    try {
      // use func for validating session
      Account acc = validateSession(sessionID, accountNum);

      // add new withdraw transaction to account
      acc.addTransaction(new TransactionImpl(amount, new Date(), "Withdraw " + amount.toString(),
          TransactionType.WITHDRAW, accountNum));

    } catch (RemoteException e) {
      throw new InvalidSession("Session details are invalid.", e);
    }
  }

  public Money getBalance(int accountNum, long sessionID) throws RemoteException, InvalidSession {
    try {
      // use func for validating session
      Account acc = validateSession(sessionID, accountNum);

      // return account balance
      return acc.getBalance();

    } catch (RemoteException e) {
      throw new InvalidSession("Session details are invalid.", e);
    }
  }

  public Statement getStatement(int accountNum, Date from, Date to, long sessionID)
      throws RemoteException, InvalidSession {
    try {

      // use func for validating session
      Account acc = validateSession(sessionID, accountNum);

      // return account statement
      return acc.getStatement(from, to);

    } catch (RemoteException e) {
      throw new InvalidSession("Session details are invalid.", e);
    }

  }

  public boolean isSessionValid(long sessionID) throws RemoteException, InvalidSession {
    // make sure session isnt longer than max session duration (in nanoseconds)
    Duration maxSessionLength = Duration.ofMinutes(SESSION_LENGTH_MINS);
    long currentSessionLength = System.nanoTime() - sessionID;

    return currentSessionLength < maxSessionLength.toNanos();
  }

  // method for and returning account
  public Account validateSession(long sessionID, int accountNum) throws RemoteException {

    // if session isn't valid throw exception
    if (!isSessionValid(sessionID)) {
      throw new RemoteException("Invalid Session ID, your token may have expired.");
    }

    // if account doesnt exist throw exception
    Account acc = accounts.stream().filter(a -> a.getAccountNum() == accountNum).findAny().orElse(null);

    if (acc == null) {
      throw new RemoteException("Account does not exist.");
    }

    return acc;
  }

  public static void main(String args[]) throws Exception {
    int registryport = 20345;

    if (args.length > 0)
      registryport = Integer.parseInt(args[0]);

    System.out.println("RMIRegistry port = " + registryport);

    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      String name = "BankServer";
      Bank bank = new BankEngine();
      Bank stub = (Bank) UnicastRemoteObject.exportObject(bank, 0);
      Registry registry = LocateRegistry.getRegistry(registryport);
      registry.rebind(name, stub);
      System.out.println("BankServer bound");
      System.out.println("Server ready for requests!");
    } catch (Exception e) {
      System.err.println("BankServer exception:");
      e.printStackTrace();
    }
  }

}