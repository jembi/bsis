package model.product;

public enum ProductStatus {

  QUARANTINED, AVAILABLE, EXPIRED, ISSUED, SPLIT, USED, UNSAFE, DISCARDED;

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
