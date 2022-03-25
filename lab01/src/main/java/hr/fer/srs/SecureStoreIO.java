package hr.fer.srs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class SecureStoreIO {

  private final Path store;
  private final boolean DEBUG = false;

  private final short IV_LENGTH = 16;
  private final short SALT_LENGTH = 16;

  private final int AES_ITERATION_COUNT = 65536;
  private final short AES_KEY_LENGTH = 256;

  public static final short PREAMBLE_LENGTH = 32;

  public SecureStoreIO(Path store) {
    this.store = store;
  }

  /**
   * Attempts to delete the data store at the target location specified when generating the SecureStoreIO instance. In case of exception the application will
   * halt.
   */
  public void removeStore() {
    try {
      Files.deleteIfExists(store);
    } catch (IOException e) {
      System.out.println("Could not delete the database store. Please check application and file permissions.");

      if (DEBUG) {
        e.printStackTrace();
      }

      System.exit(1);
    }
  }

  /**
   * Attempts to create the data store at the target location specified when generating the SecureStoreIO instance. In case of exception the application will
   * halt.
   */
  public void createStore() {
    try {
      Files.createFile(store);
    } catch (IOException e) {
      System.out.println("Could not create the database store. Please check application permissions.");

      if (DEBUG) {
        e.printStackTrace();
      }

      System.exit(1);
    }
  }

  /**
   * Encrypts the text received then securely writes it to the data store.
   *
   * @param data           The string that must be securely written into the data store
   * @param masterPassword The user submitted password used to encrypt the data
   */
  public void writeToStore(String data, String masterPassword) {
    writeToStore(data.getBytes(StandardCharsets.UTF_8), masterPassword);
  }

  /**
   * Encrypts the data received then securely writes it to the data store.
   *
   * @param data           The bytes that must be securely written into the data store
   * @param masterPassword The user submitted password used to encrypt the data
   */
  public void writeToStore(byte[] data, String masterPassword) {
    try {
      // Algorithm used to generate AES 256bit encryption KEY
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      SecureRandom cryptographicRandomizer = new SecureRandom();

      // We generate the initialization vector
      byte[] iv = new byte[IV_LENGTH];
      cryptographicRandomizer.nextBytes(iv);
      IvParameterSpec initializationVectorSpec = new IvParameterSpec(iv);

      // We generate the salt used to create the AES key
      byte[] salt = new byte[SALT_LENGTH];
      cryptographicRandomizer.nextBytes(salt);

      KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, AES_ITERATION_COUNT, AES_KEY_LENGTH);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

      // Using AES key created from the salt and master password we encrypt the data additionally using the initialization vector
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, initializationVectorSpec);

      Random rand = new Random();

      // Write 32B of random garbage data at the begining of the store, to prevent it being empty
      byte[] garbagePrefix = new byte[PREAMBLE_LENGTH];
      rand.nextBytes(garbagePrefix);

      byte[] finalData = new byte[data.length + PREAMBLE_LENGTH];
      System.arraycopy(garbagePrefix, 0, finalData, 0, garbagePrefix.length);
      System.arraycopy(data, 0, finalData, garbagePrefix.length, data.length);

      BufferedOutputStream bos = createOutputStream();

      // We write the initialization vector, salt and the encrypted bytes into the file
      writeDataToStore(bos, initializationVectorSpec.getIV());
      writeDataToStore(bos, salt);
      writeDataToStore(bos, cipher.doFinal(finalData));

      bos.flush();
      bos.close();
    } catch (Throwable e) {
      System.out.println("Could not encrypt data.");

      if (DEBUG) {
        e.printStackTrace();
      }

      System.exit(1);
    }
  }

  /**
   * Writes raw bytes into the store file.
   *
   * @param os   Output stream to be written to
   * @param data The bytes that will be written into the file
   */
  private void writeDataToStore(OutputStream os, byte[] data) {
    try {
      os.write(data);
    } catch (IOException e) {
      System.out.println("Could not write to database store.");

      if (DEBUG) {
        e.printStackTrace();
      }

      System.exit(1);
    }
  }

  /**
   * Reads and decrypts the textual content stored in the database store file.
   *
   * @param masterPassword Master password supplied by user when launching application, used to decrypt content
   * @return Textual content decrypted from file
   */
  public String readFromStore(String masterPassword) {
    try {
      BufferedInputStream bis = createInputStream();

      // We load both the salt and the initialization vector from the database store file
      byte[] iv = new byte[IV_LENGTH];
      int ivBytesRead = readDataFromStore(bis, iv);

      if (ivBytesRead != IV_LENGTH) {
        System.out.println("Error occurred while decrypting database store. (IV unavailable)");
        System.exit(1);
        return null;
      }

      byte[] salt = new byte[SALT_LENGTH];
      int saltBytesRead = readDataFromStore(bis, salt);

      if (saltBytesRead != SALT_LENGTH) {
        System.out.println("Error occurred while decrypting database store. (salt unavailable)");
        System.exit(1);
        return null;
      }

      IvParameterSpec initializationVectorSpec = new IvParameterSpec(iv);

      // Algorithm used to generate AES 256bit decryption KEY
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

      KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, AES_ITERATION_COUNT, AES_KEY_LENGTH);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, initializationVectorSpec);

      // We expect by default 32B of garbage data at the begining of the decrypted text to prevent an empty cypher
      byte[] decryptedBytes = cipher.doFinal(readAllDataFromStore(bis));

      if (decryptedBytes.length < PREAMBLE_LENGTH) {
        System.out.println("Can not read database. Data corrupted.");
        System.exit(1);
        return null;
      }

      byte[] plainTextBytes = new byte[decryptedBytes.length - PREAMBLE_LENGTH];
      System.arraycopy(decryptedBytes, PREAMBLE_LENGTH, plainTextBytes, 0, plainTextBytes.length);

      // We reconstruct the original text into UTF 8 String
      return new String(plainTextBytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      System.out.println("Error while decrypting: " + e);
      System.exit(1);
    }

    return null;
  }

  /**
   * Utility method for reading raw bytes from the database store file.
   *
   * @param is   {@code InputStream} pointing towards the database store file
   * @param data Byte array which will be filled in its entirety if possible. Equivalent of calling {@code readDataFromStore(is, b, 0, b.length)}
   * @return Number of bytes actually read from the file
   */
  private int readDataFromStore(InputStream is, byte[] data) {
    return readDataFromStore(is, data, 0, data.length);
  }

  /**
   * Utility method for reading raw bytes from the database store file.
   *
   * @param is     {@code InputStream} pointing towards the database store file
   * @param data   Byte array which will be filled in its entirety if possible. Equivalent of calling {@code readDataFromStore(is, b, 0, b.length)}
   * @param offset Where to start writing in the target data byte array
   * @param len    how many bytes to read from the database store file and write into target data byte array
   * @return Number of bytes actually read from the file
   */
  private int readDataFromStore(InputStream is, final byte[] data, int offset, final int len) {
    final int BUFFER_SIZE = Math.min(len, 1024); // We start reading in chunks once reads are over 1 KB

    try {
      int numOfBytesRead, byteCounter = 0;

      do {
        byte[] tempBuffer = new byte[BUFFER_SIZE];

        numOfBytesRead = is.read(tempBuffer);

        System.arraycopy(tempBuffer, 0, data, offset, numOfBytesRead);
        offset += numOfBytesRead;
        byteCounter += numOfBytesRead;

        if (byteCounter >= len) {
          break;
        }
      } while (numOfBytesRead <= 0);

      return numOfBytesRead;
    } catch (IOException e) {
      System.out.println("Error occurred while reading data from database store.");

      if (DEBUG) {
        e.printStackTrace();
      }

      System.exit(1);
    }

    return 0;
  }

  /**
   * Utility method used to read all remaining bytes from a file.
   *
   * @param is {@code InputStream} pointing towards the database store file
   * @return Byte array containing all remaining data in stream
   */
  private byte[] readAllDataFromStore(InputStream is) {
    final int BUFFER_SIZE = 2014;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      int numOfBytesRead;
      while (true) {
        // We read in chunks - we buffer the data in a ByteArrayOutputStream for performance
        byte[] tempBuffer = new byte[BUFFER_SIZE];

        numOfBytesRead = is.read(tempBuffer);

        if (numOfBytesRead <= 0) {
          break;
        }

        baos.write(tempBuffer, 0, numOfBytesRead);
      }
    } catch (IOException e) {
      System.out.println("Error occurred while reading data from database store.");

      if (DEBUG) {
        e.printStackTrace();
      }

      System.exit(1);
    }

    return baos.toByteArray();
  }

  /**
   * Creates a fresh buffered output stream used to write data to the specified data file.
   *
   * @return Buffered output stream ready for writing data
   */
  private BufferedOutputStream createOutputStream() {
    try {
      OutputStream os = Files.newOutputStream(store);
      return new BufferedOutputStream(os);
    } catch (IOException e) {
      System.out.println("Could not open stream to database store.");

      if (DEBUG) {
        e.printStackTrace();
      }

      System.exit(1);
    }

    return null;
  }

  /**
   * Creates a fresh buffered input stream used to read data from the specified data file.
   *
   * @return Buffered input stream ready for reading data
   */
  private BufferedInputStream createInputStream() {
    try {
      InputStream is = Files.newInputStream(store);
      return new BufferedInputStream(is);
    } catch (IOException e) {
      System.out.println("Could not open stream to database store.");

      if (DEBUG) {
        e.printStackTrace();
      }

      System.exit(1);
    }

    return null;
  }
}
