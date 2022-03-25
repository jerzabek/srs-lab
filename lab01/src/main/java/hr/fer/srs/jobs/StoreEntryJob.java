package hr.fer.srs.jobs;

import hr.fer.srs.PasswordDatabase;
import hr.fer.srs.PasswordManager;
import java.util.Objects;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class StoreEntryJob extends PasswordManagerJob {

  private final String entryName, password;

  public StoreEntryJob(String masterPassword, String entryName, String password) {
    super(masterPassword);

    this.entryName = Objects.requireNonNull(entryName);
    this.password = Objects.requireNonNull(password);
  }

  @Override
  public void run() {
    PasswordDatabase database = PasswordManager.getPasswordDatabase(getMasterPassword());

    boolean override = database.storeEntry(entryName, password);

    PasswordManager.getSecureStoreIO().writeToStore(database.toString(), getMasterPassword());

    if (override) {
      System.out.println("Successfully updated password entry in database.");
    } else {
      System.out.println("Successfully stored password entry in database.");
    }
  }
}
