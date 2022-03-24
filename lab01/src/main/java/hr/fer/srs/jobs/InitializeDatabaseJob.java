package hr.fer.srs.jobs;

import hr.fer.srs.Utility;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Random;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class InitializeDatabaseJob extends PasswordManagerJob {

  public InitializeDatabaseJob(String masterPassword) {
    super(masterPassword);
  }

  @Override
  public void run() {
    removePreviousStore();
    createFreshStore();

    // Write 32B of random garbage data into the store, to prevent it being empty
    Random rand = new Random();

    byte[] garbagePrefix = new byte[32];
    rand.nextBytes(garbagePrefix);

    BufferedOutputStream bos = createOutputStream();

    writeToStore(bos, garbagePrefix);

    try {
      bos.flush();
      bos.close();
    } catch (IOException e) {
//      e.printStackTrace();
      System.out.println("Could not close stream to data store.");
      System.exit(1);
    }
  }

  private void writeToStore(BufferedOutputStream bos, byte[] garbagePrefix) {
    try {
      bos.write(garbagePrefix);
    } catch (IOException e) {
//      e.printStackTrace();
      System.out.println("Could not write to database store.");
      System.exit(1);
    }
  }

  private BufferedOutputStream createOutputStream() {
    try {
      OutputStream os = Files.newOutputStream(Utility.DATABASE_FILE);
      return new BufferedOutputStream(os);
    } catch (IOException e) {
//      e.printStackTrace();
      System.out.println("Could not open stream to database store.");
      System.exit(1);
    }
    
    return null;
  }

  private void createFreshStore() {
    try {
      Files.createFile(Utility.DATABASE_FILE);
    } catch (IOException e) {
//      e.printStackTrace();
      System.out.println("Could not create the database store. Please check application permissions.");
      System.exit(1);
    }
  }

  private void removePreviousStore() {
    try {
      Files.deleteIfExists(Utility.DATABASE_FILE);
    } catch (IOException e) {
//      e.printStackTrace();
      System.out.println("Could not delete the database store. Please check application and file permissions.");
      System.exit(1);
    }
  }
}
