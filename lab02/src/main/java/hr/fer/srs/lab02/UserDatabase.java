package hr.fer.srs.lab02;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class UserDatabase {

  private final Map<String, User> users;

  public UserDatabase(SecureStore store) {
    this.users = parseStore(store);
  }

  private Map<String, User> parseStore(SecureStore store) {
    Map<String, User> users = new HashMap<>();

    try {
      for (User user : store.readAll()) {
        users.put(user.getUsername(), user);
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not create user database: " + e.getMessage());
    }

    return users;
  }

  public List<User> getUsers() {
    return List.copyOf(users.values());
  }

  public User getUser(String username) {
    return users.get(username);
  }

  public void addUser(User user) {
    users.put(user.getUsername(), user);
  }

  public void removeUser(String username) {
    users.remove(username);
  }
}
