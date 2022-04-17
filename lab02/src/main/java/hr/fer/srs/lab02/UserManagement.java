package hr.fer.srs.lab02;

import static hr.fer.srs.lab02.Utility.MODE_ADD_USER;
import static hr.fer.srs.lab02.Utility.MODE_FORCE_PASSWORD_RESET;
import static hr.fer.srs.lab02.Utility.MODE_REMOVE_USER;
import static hr.fer.srs.lab02.Utility.MODE_RESET_PASSWORD;

import hr.fer.srs.lab02.userManagement.AddUserOperation;
import hr.fer.srs.lab02.userManagement.ForceUserPasswordResetOperation;
import hr.fer.srs.lab02.userManagement.RemoveUserOperation;
import hr.fer.srs.lab02.userManagement.ResetUserPasswordOperation;
import hr.fer.srs.lab02.userManagement.UserManagementOperation;
import java.io.IOException;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class UserManagement {

  public static UserManagement userManagementInstance;

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Invalid number of arguments.");
      System.exit(1);
      return;
    }

    userManagementInstance = new UserManagement(args);
    userManagementInstance.run();
  }

  private final SecureStore store;
  private final UserDatabase database;

  private final String mode, username;

  public UserManagement(String[] args) {
    store = new SecureStore();
    database = new UserDatabase(store);

    this.mode = args[0];
    this.username = args[1];
  }

  private void run() {
    try {
      UserManagementOperation operation = pickOperation(mode, username);

      operation.run();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    try {
      store.storeUsers(database.getUsers());
    } catch (IOException e) {
      System.out.println("Could not save users: " + e.getMessage());
    }
  }

  private UserManagementOperation pickOperation(String mode, String username) {
    // We check which operation mode the user has chosen
    return switch (mode) {
      case MODE_ADD_USER -> new AddUserOperation(username);
      case MODE_REMOVE_USER -> new RemoveUserOperation(username);
      case MODE_RESET_PASSWORD -> new ResetUserPasswordOperation(username);
      case MODE_FORCE_PASSWORD_RESET -> new ForceUserPasswordResetOperation(username);
      default -> throw new IllegalArgumentException("Invalid mode " + mode);
    };
  }

  public SecureStore getStore() {
    return store;
  }

  public UserDatabase getDatabase() {
    return database;
  }
}
