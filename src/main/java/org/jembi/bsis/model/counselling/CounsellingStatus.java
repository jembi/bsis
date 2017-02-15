package org.jembi.bsis.model.counselling;

public enum CounsellingStatus {

  RECEIVED_COUNSELLING(1L, "Received Counselling"),
  REFUSED_COUNSELLING(2L, "Refused Counselling"),
  DID_NOT_RECEIVE_COUNSELLING(3L, "Did Not Receive Counselling");

  private long id;
  private String label;

  private CounsellingStatus(long id, String label) {
    this.id = id;
    this.label = label;
  }

  public long getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public static CounsellingStatus findById(Long id) {
    if (id != null) {
      for (CounsellingStatus value : CounsellingStatus.values()) {
        if (value.getId() == id) {
          return value;
        }
      }
    }
    return null;
  }
}
