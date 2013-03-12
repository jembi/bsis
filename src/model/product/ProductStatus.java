package model.product;

public enum ProductStatus {

  QUARANTINED, AVAILABLE, EXPIRED, ISSUED, USED, UNSAFE,
  SUBDIVIDED, DISCARDED, RETURNED, IMPORTED, OTHER;

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
