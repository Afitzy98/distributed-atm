package bank;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import org.joda.money.Money;

public interface Bank extends Remote {
    // The login method returns a token that is valid for some time period that must
    // be passed to the other methods as a session identifier
    public long login(String username, String password) throws RemoteException, InvalidLogin;

    public void deposit(int accountNum, Money amount, long sessionID) throws RemoteException, InvalidSession;

    public void withdraw(int accountNum, Money amount, long sessionID) throws RemoteException, InvalidSession;

    public Money getBalance(int accountNum, long sessionID) throws RemoteException, InvalidSession;

    public Statement getStatement(int accountNum, Date from, Date to, long sessionID)
            throws RemoteException, InvalidSession;
}