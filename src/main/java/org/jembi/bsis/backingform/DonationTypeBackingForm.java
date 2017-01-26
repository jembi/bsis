
package org.jembi.bsis.backingform;

public class DonationTypeBackingForm {

  private Long id;
  private String donationType;
  private Boolean isDeleted = false;

  public String getDonationType() {
    return donationType;
  }

  public Long getId() {
    return id;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setDonationType(String donationType) {
    this.donationType = donationType;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
