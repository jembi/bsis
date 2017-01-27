
package org.jembi.bsis.backingform;

public class DonationTypeBackingForm {

  private Long id;
  private String type;
  private Boolean isDeleted = false;

  public String getType() {
    return type;
  }

  public Long getId() {
    return id;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setType(String donationType) {
    this.type = donationType;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
