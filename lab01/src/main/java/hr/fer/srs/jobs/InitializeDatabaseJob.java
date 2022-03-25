package hr.fer.srs.jobs;

import hr.fer.srs.PasswordDatabase;
import hr.fer.srs.PasswordManager;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class InitializeDatabaseJob extends PasswordManagerJob {

  public InitializeDatabaseJob(String masterPassword) {
    super(masterPassword);
  }

  @Override
  public void run() {
    // We remove any previous store and start a fresh one - permanent data loss occurs here
    PasswordManager.getSecureStoreIO().removeStore();
    PasswordManager.getSecureStoreIO().createStore();

    PasswordManager.getSecureStoreIO().writeToStore(new PasswordDatabase().toString(), getMasterPassword());

    System.out.println("Successfully initialized database.");
  }

}
