
package org.jembi.bsis.backingform;

import java.util.UUID;

public class DonationTypeBackingForm {

  private UUID id;
  private String type;
  private Boolean isDeleted = false;

  public String getType() {
    return type;
  }

  public UUID getId() {
    return id;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setType(String donationType) {
    this.type = donationType;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
