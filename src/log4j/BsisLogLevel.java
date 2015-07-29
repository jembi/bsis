package log4j;

import org.apache.log4j.Level;

public class BsisLogLevel extends Level {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public static final int BSIS_INT = WARN_INT + 100;

  protected BsisLogLevel(int level, String levelStr, int syslogEquivalent) {
    super(level, levelStr, syslogEquivalent);
  }

  public static final Level BSIS = new BsisLogLevel(BSIS_INT, "BSIS_CLICK_LOG", 7);

  public static Level toLevel(String sArg) {
    return BSIS;
  }

  public static Level toLevel(int val) {
    return BSIS;
  }

}
