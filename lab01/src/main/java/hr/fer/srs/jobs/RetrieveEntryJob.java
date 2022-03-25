package hr.fer.srs.jobs;

import hr.fer.srs.PasswordDatabase;
import hr.fer.srs.PasswordManager;
import java.util.Objects;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class RetrieveEntryJob extends PasswordManagerJob {

  private final String entryName;

  public RetrieveEntryJob(String masterPassword, String entryName) {
    super(masterPassword);

    this.entryName = Objects.requireNonNull(entryName);
  }

  @Override
  public void run() {
    PasswordDatabase database = PasswordManager.getPasswordDatabase(getMasterPassword());

    String password = database.getEntry(entryName);

    if (password == null) {
      System.out.println("Database does not contain entry " + entryName);
      return;
    }

    System.out.printf("Password for %s is: %s\n", entryName, password);
  }
}
