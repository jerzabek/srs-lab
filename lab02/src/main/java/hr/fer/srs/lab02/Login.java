package hr.fer.srs.lab02;

import java.io.Console;
import java.io.IOException;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class Login {

  private static final String ERR = "Invalid username or password";
  private static SecureStore store;
  private static UserDatabase database;

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("No username argument provided.");
      System.exit(1);
      return;
    }

    store = new SecureStore();
    database = new UserDatabase(store);

    String username = args[0];

    // Check if user exists
    if (database.getUser(username) == null) {
      System.out.println(ERR);
      System.exit(1);
      return;
    }

    User user = database.getUser(username);

    // Check user password
    Console console = System.console();

    char[] pass;

    int attemptCounter = 0;
    boolean login = false;

    while (attemptCounter < 3) {
      pass = console.readPassword("Password: ");

      if (user.checkPassword(store.getPasswordHash(pass))) {
        try {
          successfullLogin(user);
        } catch (IOException e) {
          System.out.println("Could not update password: " + e.getMessage());
          return;
        }

        login = true;
        break;
      } else {
        System.out.println(ERR);
      }

      attemptCounter++;
    }

    if (login) {
      return;
    }

    System.out.println("Could not log in.");
  }

  private static void successfullLogin(User user) throws IOException {
    if (user.isForceResetPassword()) {
      System.out.println("You must change your password before continuing.");
      Console console = System.console();

      char[] pass = console.readPassword("Password: ");
      char[] passConfirmation = console.readPassword("Repeat password: ");

      if (!new String(pass).equals(new String(passConfirmation))) {
        System.out.println("Password mismatch.");
        System.exit(1);
        return;
      }

      user.setForceResetPassword(false);
      user.setPassword(store.getPasswordHash(pass));
      store.storeUsers(database.getUsers());
      System.out.println("Password change successfull!");
    }

    System.out.println("Successfully logged in as " + user.getUsername());
  }

}
