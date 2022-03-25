package hr.fer.srs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Abstraction model used to interact with the password entry data structure.
 *
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class PasswordDatabase {

  private final Map<String, String> database;

  /**
   * We construct an empty database model.
   */
  public PasswordDatabase() {
    this.database = new HashMap<>();
  }

  /**
   * We construct a database model based on the raw data provided.
   *
   * @param database Raw data that will be parsed
   */
  public PasswordDatabase(String database) {
    this.database = new HashMap<>();

    if (database.isBlank()) {
      return;
    }

    String[] lines = database.trim().split("\\n");

    if (lines.length % 2 != 0) {
      throw new RuntimeException("Database is unreadable.");
    }

    for (int i = 0; i < lines.length; i += 2) {
      this.database.put(lines[i], lines[i + 1]);
    }
  }

  /**
   * Attempts to retreive database entry. If no entry is found we return null.
   *
   * @param entryName Entry name to look up
   * @return If a password is tied to the specified entry name it is returned, otherwise null
   */
  public String getEntry(String entryName) {
    return database.get(entryName);
  }

  /**
   * Stores or updates an entry name and password pair in the database model.
   *
   * @param entryName The name of the entry
   * @param password  The password value of the entry
   * @return True if there was already an entry defined for the specified name
   */
  public boolean storeEntry(String entryName, String password) {
    boolean existed = database.containsKey(entryName);

    database.put(entryName, password);

    return existed;
  }

  @Override
  public String toString() {
    // We generate a string representation of the database that can be used to persist the data structure
    StringBuilder output = new StringBuilder();

    for (Entry<String, String> entry : database.entrySet()) {
      output.append(String.format("%s\n%s\n", entry.getKey(), entry.getValue()));
    }

    return output.toString().trim();
  }
}
