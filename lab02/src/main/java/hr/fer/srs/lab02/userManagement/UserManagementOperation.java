package hr.fer.srs.lab02.userManagement;

import hr.fer.srs.lab02.UserManagement;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public abstract class UserManagementOperation {

  protected final String username;
  protected boolean checkUserExistance;

  public UserManagementOperation(String username) {
    this.username = username;
    this.checkUserExistance = true;
  }

  public UserManagementOperation(String username, boolean checkUserExistance) {
    this(username);
    this.checkUserExistance = checkUserExistance;
  }

  protected abstract void execute();

  public void run() {
    if (checkUserExistance && UserManagement.userManagementInstance.getDatabase().getUser(username) == null) {
      throw new RuntimeException("User does not exist.");
    }

    execute();
  }

}
