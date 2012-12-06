package model.request;

/**
 * ISO/IEC 5218
 * 0 = not known, 1 = male, 2 = female, 9 = not applicable
 * NOTE: Try to use valueOf() method to convert enum constants
 * to strings wherever possible.
 */
public enum RequestStatus {

  pending(0), partially_fulfilled(1), fulfilled(2);

  @SuppressWarnings("unused")
  private int value;
  
  private RequestStatus(int value) {
    this.value = value;
  }
}