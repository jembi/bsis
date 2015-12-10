package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.donationtype.DonationType;

import javax.validation.Valid;


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

  public void setDonationType(DonationType donationType) {
    this.donationType = donationType;
  }

  public String getType() {
    return donationType.getDonationType();
  }

  public void setType(String donationTypeStr) {
    donationType.setDonationType(donationTypeStr);
  }

  public Integer getId() {
    return donationType.getId();
  }

  public void setId(Integer id) {
    donationType.setId(id);
  }

  public void setIsDeleted(Boolean isDeleted) {
    donationType.setIsDeleted(isDeleted);
  }
}
