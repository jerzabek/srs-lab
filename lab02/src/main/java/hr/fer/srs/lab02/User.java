package hr.fer.srs.lab02;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class User {

  private final String username;
  private final byte[] passwordHash;

  public User(String username, char[] password) {
    this(username, SecureStore.getPasswordHash(password));
  }

  public User(String username, byte[] passwordHash) {
    this.username = username;
    this.passwordHash = passwordHash;
  }

  public String getUsername() {
    return username;
  }

  public byte[] getPasswordHash() {
    return passwordHash;
  }
}
