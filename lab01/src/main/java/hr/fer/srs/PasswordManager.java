package hr.fer.srs;

import hr.fer.srs.jobs.InitializeDatabaseJob;
import hr.fer.srs.jobs.PasswordManagerJob;
import hr.fer.srs.jobs.RetrieveEntryJob;
import hr.fer.srs.jobs.StoreEntryJob;
import java.util.Locale;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class PasswordManager {

  private static SecureStoreIO secureStoreIO;

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Invalid arguments.\nArguments: [command] [master password] (optional arguments...)");
      System.exit(1);
      return;
    }

    String commandName = args[0].toLowerCase(Locale.ROOT),
        masterPassword = args[1];

    PasswordManagerJob job;

    switch (commandName) {
      case Utility.COMMAND_INITIALIZE:
        job = new InitializeDatabaseJob(masterPassword);
        break;

      case Utility.COMMAND_STORE:
        if (args.length < 4) {
          System.out.printf("Missing arguments.\nHelp: %s [master password] [entry name] [password]\n", Utility.COMMAND_STORE);
          System.exit(1);
          return;
        }

        job = new StoreEntryJob(masterPassword, args[2], args[3]);
        break;

      case Utility.COMMAND_RETRIEVE:
        if (args.length < 3) {
          System.out.printf("Missing entry name argument.\nHelp: %s [master password] [entry name]\n", Utility.COMMAND_RETRIEVE);
          System.exit(1);
          return;
        }

        job = new RetrieveEntryJob(masterPassword, args[2]);
        break;

      default:
        System.out.println("Invalid command " + commandName + ".");
        System.exit(1);
        return;
    }

    secureStoreIO = new SecureStoreIO(Utility.DATABASE_FILE);

    job.run();
  }

  /**
   * Returns configured SecureStoreIO instance.
   *
   * @return Configured SecureStoreIO instance.
   */
  public static SecureStoreIO getSecureStoreIO() {
    return secureStoreIO;
  }

  /**
   * Creates a new instance of a {@code PasswordDatabase} used to interact with the password database data model.
   *
   * @param masterPassword Master password supplied by user when launching application
   * @return {@code PasswordDatabase} object loaded with all entries
   */
  public static PasswordDatabase getPasswordDatabase(String masterPassword) {
    try {
      return new PasswordDatabase(PasswordManager.getSecureStoreIO().readFromStore(masterPassword));
    } catch (Throwable e) {
      System.out.println("Could not load database: " + e.getMessage());
      System.exit(1);
      return null;
    }
  }
}
