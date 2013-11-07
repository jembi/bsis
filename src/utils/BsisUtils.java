package utils;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class BsisUtils {

	private static final Logger LOGGER = Logger.getLogger(BsisUtils.class);

	/**
	 * Gets the directory.
	 * 
	 * @param basePath
	 *          the base path
	 * @param subDirStructure
	 *          the sub dir structure
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
	 * @param imageRootDirectory
	 *          the image root directory
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
	 * @param dir
	 *          the dir
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
	 * @param remainingTries
	 *          the remaining tries
	 * @param interval
	 *          the interval
	 * @param errorMsg
	 *          the error msg
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
}
