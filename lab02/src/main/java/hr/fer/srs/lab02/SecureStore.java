package hr.fer.srs.lab02;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class SecureStore {

  private static final short SALT_LENGTH = 16;

  private static final int ITERATION_COUNT = 65536;
  private static final short KEY_LENGTH = 256;

  private static final String storeFile = "passwords.bin";

  private final Path pathToStore;

  public SecureStore() {
    pathToStore = Paths.get(storeFile);
  }

  public static byte[] getPasswordHash(char[] password) {
    try {
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      SecureRandom cryptographicRandomizer = new SecureRandom();

      byte[] salt = new byte[SALT_LENGTH];
      cryptographicRandomizer.nextBytes(salt);

      KeySpec spec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH);
      return factory.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException("Could not hash password");
    }
  }

  public List<User> readAll() throws IOException {
    List<User> users = new ArrayList<>();

    BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(pathToStore));
    DataInputStream dis = new DataInputStream(bis);

    while (true) {
      try {
        String username = dis.readUTF();
        long numOfBytes = dis.readLong();
        byte[] passwordHash = dis.readNBytes(Math.toIntExact(numOfBytes));

        users.add(new User(username, passwordHash));
      } catch (EOFException e) {
        break;
      }
    }

    dis.close();

    return users;
  }

  public void storeUsers(Collection<User> users) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(pathToStore));
    DataOutputStream dos = new DataOutputStream(bos);

    for (User user : users) {
      dos.writeUTF(user.getUsername());
      dos.writeLong(user.getPasswordHash().length);

      for (byte b : user.getPasswordHash()) {
        dos.writeByte(b);
      }
    }

    dos.flush();
    dos.close();
  }
}
