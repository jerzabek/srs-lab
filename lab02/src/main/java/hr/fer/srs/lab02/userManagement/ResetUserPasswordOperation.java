package hr.fer.srs.lab02.userManagement;

import hr.fer.srs.lab02.SecureStore;
import hr.fer.srs.lab02.UserManagement;
import java.io.Console;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class ResetUserPasswordOperation extends UserManagementOperation {

  public ResetUserPasswordOperation(String username) {
    super(username);
  }

  @Override
  public void execute() {
    Console console = System.console();

    char[] newPass = console.readPassword("Password: ");
    char[] newPassConfirmation = console.readPassword("Repeat password: ");

    if (!new String(newPass).equals(new String(newPassConfirmation))) {
      throw new IllegalStateException("Password change failed. Password mismatch.");
    }

    UserManagement.userManagementInstance.getDatabase().getUser(username).setPassword(UserManagement.userManagementInstance.getStore().getPasswordHash(newPass));
  }
}
