
package org.jembi.bsis.backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.Valid;

import org.jembi.bsis.model.donationtype.DonationType;


public class DonationTypeBackingForm {

  @Valid
  @JsonIgnore
  private DonationType donationType;

  public DonationTypeBackingForm() {
    donationType = new DonationType();
  }

  public DonationType getDonationType() {
    return donationType;
  }

  public String getType() {
    return donationType.getDonationType();
  }

  public Long getId() {
    return donationType.getId();
  }

  public void setDonationType(DonationType donationType) {
    this.donationType = donationType;
  }

  public void setId(Long id) {
    donationType.setId(id);
  }

  public void setType(String donationTypeStr) {
    donationType.setDonationType(donationTypeStr);
  }

  public void setIsDeleted(Boolean isDeleted) {
    donationType.setIsDeleted(isDeleted);
  }
}
