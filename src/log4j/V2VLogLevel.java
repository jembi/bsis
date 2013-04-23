package log4j;

import org.apache.log4j.Level;

public class V2VLogLevel extends Level {

	/**
   * 
   */
  private static final long serialVersionUID = 1L;
  public static final int V2V_INT = WARN_INT + 100;

	protected V2VLogLevel(int level, String levelStr, int syslogEquivalent) {
		super(level, levelStr, syslogEquivalent);
	}

	public static final Level V2V = new V2VLogLevel(V2V_INT, "V2V_CLICK_LOG", 7);

	public static Level toLevel(String sArg) {
		return V2V;
	}

	public static Level toLevel(int val) {
		return V2V;
	}

}
