package hr.fer.srs.lab02.userManagement;

import hr.fer.srs.lab02.UserManagement;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class ForceUserPasswordResetOperation extends UserManagementOperation {

  public ForceUserPasswordResetOperation(String username) {
    super(username);
  }

  @Override
  public void execute() {
    UserManagement.userManagementInstance.getDatabase().getUser(username).setForceResetPassword(true);
  }
}
