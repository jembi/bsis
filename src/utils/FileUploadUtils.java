package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import model.bloodtesting.UploadTTIResultConstant;

import org.apache.log4j.Logger;

public class FileUploadUtils {

  private static final Logger LOGGER = Logger.getLogger(FileUploadUtils.class);

  /**
   * Gets the directory.
   *
   * @param basePath        the base path
   * @param subDirStructure the sub dir structure
   * @return the directory
   */
  public static String getDirectory(String basePath) {
    StringBuilder dirStructure = getRootDirectory(basePath);
    dirStructure.append(File.separator);
    File objectDir = new File(dirStructure.toString());
    mkdirs(objectDir);
    return dirStructure.toString();
  }

  /**
   * Gets the image root directory.
   *
   * @param imageRootDirectory the image root directory
   * @return the image root directory
   */
  public static StringBuilder getRootDirectory(String imageRootDirectory) {
    File rootDir = new File(imageRootDirectory);
    mkdirs(rootDir);
    StringBuilder dirStructure = new StringBuilder(imageRootDirectory);
    dirStructure.append(File.separator);
    return dirStructure;
  }

  /**
   * Mkdirs.
   *
   * @param dir the dir
   */
  public static void mkdirs(File dir) {
    if (dir == null) {
      return;
    }
    AtomicInteger tries = new AtomicInteger(10);
    long interval = 100;
    while (!dir.exists()) {
      if (dir.mkdirs()) {
        break;
      }
      validateAndWait(tries, interval, "Error creating directory: " + dir);
    }
  }

  /**
   * Validate and wait.
   *
   * @param remainingTries the remaining tries
   * @param interval       the interval
   * @param errorMsg       the error msg
   */
  public static void validateAndWait(AtomicInteger remainingTries,
                                     long interval, String errorMsg) {
    if (remainingTries.decrementAndGet() <= 0) {
      LOGGER.error(errorMsg);
    }
    try {
      Thread.sleep(interval);
    } catch (InterruptedException e) {
      LOGGER.error(errorMsg + e.getMessage());
    }
  }

  /**
   * Utility method to split file path
   *
   * @return file split path
   */
  public static String splitFilePath(String fileName) {
    String[] getFileName = fileName.split(UploadTTIResultConstant.TSV_FILE_EXTENTION);
    String fileNameWithExt = getFileName[0] + getCurrentDateAsString() + UploadTTIResultConstant.TSV_FILE_EXTENTION;
    return fileNameWithExt;
  }

  /**
   * utility method that returns current date in YearMonthDateHoursMinuteSecond
   *
   * @return current date in YearMonthDateHoursMinuteSecond format
   */
  public static String getCurrentDateAsString() {
    Date currentDate = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
    return ft.format(currentDate);
  }
}
