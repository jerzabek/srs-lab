package hr.fer.srs.jobs;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public abstract class PasswordManagerJob implements Runnable {

  private final String masterPassword;

  public PasswordManagerJob(String masterPassword) {
    this.masterPassword = masterPassword;
  }

  public String getMasterPassword() {
    return masterPassword;
  }
}
