package hr.fer.srs.lab02.userManagement;

import hr.fer.srs.lab02.User;
import hr.fer.srs.lab02.UserManagement;
import java.io.Console;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class AddUserOperation extends UserManagementOperation {

  public AddUserOperation(String username) {
    super(username, false);
  }

  @Override
  public void execute() {
    if (UserManagement.userManagementInstance.getDatabase().getUser(username) != null) {
      System.out.println("User already exists.");
      return;
    }

    Console console = System.console();

    char[] pass = console.readPassword("Password: ");
    char[] passConfirmation = console.readPassword("Repeat password: ");

    if (!new String(pass).equals(new String(passConfirmation))) {
      throw new IllegalStateException("User add failed. Password mismatch.");
    }

    UserManagement.userManagementInstance.getDatabase().addUser(new User(username, pass));

    System.out.println("Successfully added user.");
  }
}
