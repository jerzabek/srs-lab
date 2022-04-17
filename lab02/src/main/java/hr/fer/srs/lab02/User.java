package hr.fer.srs.lab02;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class User {

  private final String username;
  private byte[] passwordHash;
  private boolean forceResetPassword = false;

  public User(String username, char[] password) {
    this(username, UserManagement.userManagementInstance.getStore().getPasswordHash(password));
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

  public boolean isForceResetPassword() {
    return forceResetPassword;
  }

  public void setForceResetPassword(boolean forceResetPassword) {
    this.forceResetPassword = forceResetPassword;
  }

  public void setPassword(byte[] passwordHash) {
    this.passwordHash = passwordHash;
  }

  public boolean checkPassword(byte[] otherHash) {
    if (passwordHash.length != otherHash.length) {
      return false;
    }

    for (int i = 0; i < passwordHash.length; i++) {
      if (passwordHash[i] != otherHash[i]) {
        return false;
      }
    }

    return true;
  }
}
