package model.product;

public enum ProductStatus {

  QUARANTINED, AVAILABLE, EXPIRED, ISSUED, UNSAFE, DISCARDED, RETURNED, OTHER;

  public static ProductStatus lookup(String statusStr) {
    ProductStatus status = null;
    try {
       status = ProductStatus.valueOf(statusStr);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
      status = ProductStatus.OTHER;
    }
    return status;
  }
}
