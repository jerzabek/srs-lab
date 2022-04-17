package hr.fer.srs.lab02.userManagement;

import hr.fer.srs.lab02.UserManagement;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class RemoveUserOperation extends UserManagementOperation {

  public RemoveUserOperation(String username) {
    super(username);
  }

  @Override
  public void execute() {
    UserManagement.userManagementInstance.getDatabase().removeUser(username);
  }
}
