package model.product;

public enum ProductStatus {

  QUARANTINED, AVAILABLE, EXPIRED, ISSUED, SPLIT, USED, UNSAFE, DISCARDED, PROCESSED;

  /**
   * Alternative to valueOf when we want to assign a default status instead
   * of throwing an exception when an invalid string is passed.
   * @param statusStr
   * @return
   */
  public static ProductStatus lookup(String statusStr) {
    ProductStatus status = null;
    try {
       status = ProductStatus.valueOf(statusStr);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
      status = ProductStatus.QUARANTINED;
    }
    return status;
  }
}
