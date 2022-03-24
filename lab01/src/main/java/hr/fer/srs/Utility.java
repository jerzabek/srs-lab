package hr.fer.srs;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Ivan Jeržabek - ivan.jerzabek@fer.hr
 */
public class Utility {

  // Command names
  public static final String COMMAND_INITIALIZE = "init";
  public static final String COMMAND_STORE = "put";
  public static final String COMMAND_RETRIEVE = "get";

  // File name of the database
  public static final String DATABASE_FILE_NAME = "passwordManager.bin";
  public static final Path DATABASE_FILE = Paths.get(DATABASE_FILE_NAME); // We assume the database file to be in the same directory

}
