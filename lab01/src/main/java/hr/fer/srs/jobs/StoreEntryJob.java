package hr.fer.srs.jobs;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class StoreEntryJob extends PasswordManagerJob {

  private final String entryName, password;

  public StoreEntryJob(String masterPassword, String entryName, String password) {
    super(masterPassword);

    this.entryName = entryName;
    this.password = password;
  }

  @Override
  public void run() {

  }
}
