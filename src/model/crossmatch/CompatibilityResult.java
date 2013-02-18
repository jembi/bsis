package model.crossmatch;

public enum CompatibilityResult {

  COMPATIBLE, NOT_COMPATIBLE, OTHER;

  public static CompatibilityResult lookup(String compatibilityResultStr) {
    CompatibilityResult compatibilityResult = null;
    try {
       compatibilityResult = CompatibilityResult.valueOf(compatibilityResultStr);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
      compatibilityResult = CompatibilityResult.OTHER;
    }
    return compatibilityResult;
  }
}
