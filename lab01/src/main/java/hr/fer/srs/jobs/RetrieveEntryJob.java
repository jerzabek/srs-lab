package hr.fer.srs.jobs;

import java.util.Objects;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class RetrieveEntryJob extends PasswordManagerJob {

  private final String entryName;

  public RetrieveEntryJob(String masterPassword, String entryName) {
    super(masterPassword);

    this.entryName = Objects.requireNonNull(entryName);
  }

  @Override
  public void run() {

  }
}
