package hr.fer.srs.lab02.userManagement;

import hr.fer.srs.lab02.User;
import hr.fer.srs.lab02.UserManagement;
import java.io.Console;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class AddUserOperation implements UserManagementOperation {

  private String username;

  public AddUserOperation(String username) {
    this.username = username;
  }

  @Override
  public void execute() {
    Console console = System.console();

    char[] pass = console.readPassword("Password:");
    char[] passConfirmation = console.readPassword("Repeat password:");

    if (!new String(pass).equals(new String(passConfirmation))) {
      throw new IllegalStateException("User add failed. Password mismatch.");
    }

    UserManagement.getDatabase().addUser(new User(username, pass));
  }
}
