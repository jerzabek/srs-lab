package hr.fer.srs.lab02;

import hr.fer.srs.lab02.userManagement.AddUserOperation;
import hr.fer.srs.lab02.userManagement.UserManagementOperation;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class UserManagement {

  public static final String MODE_ADD_USER = "add";
  public static final String MODE_REMOVE_USER = "del";
  public static final String MODE_RESET_PASSWORD = "passwd";
  public static final String MODE_FORCE_PASSWORD_RESET = "forcepass";

  private static SecureStore store;
  private static UserDatabase database;

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Invalid number of arguments.");
      System.exit(1);
      return;
    }

    store = new SecureStore();
    database = new UserDatabase(store);

    try {
      UserManagementOperation operation = pickOperation(args[0], args[1]);

      operation.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static UserManagementOperation pickOperation(String mode, String username) {
    // We check which operation mode the user has chosen
    return switch (mode) {
      case MODE_ADD_USER -> new AddUserOperation(username);
      case MODE_REMOVE_USER -> null;
      case MODE_RESET_PASSWORD -> null;
      case MODE_FORCE_PASSWORD_RESET -> null;
      default -> throw new IllegalArgumentException("Invalid mode " + mode);
    };
  }

  public static SecureStore getStore() {
    return store;
  }

  public static UserDatabase getDatabase() {
    return database;
  }
}
