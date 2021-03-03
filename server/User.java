package server;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class User {

  private String username;
  private String hashedPassword;
  private String name;
  private Account account;

  public User(String name, String username, String password) {
    this.name = name;
    this.username = username;
    this.hashedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    this.account = null;
  }

  public Account createAccount(float balance) {
    this.account = new Account(this.name, balance, new Date());
    return this.account;
  }

  public Account getAccount() {
    return account;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public boolean isPasswordValid(String password) {
    return this.hashedPassword.equals(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString());
  }

}
